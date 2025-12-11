import React, { useState, useEffect } from 'react';
import { chatApi, userApi } from '../api';
import type { DialogueDto, MessageDto, UserDto } from '../types';

const ChatMonitoring: React.FC = () => {
  const [dialogues, setDialogues] = useState<DialogueDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [users, setUsers] = useState<UserDto[]>([]);
  const [selectedDialogue, setSelectedDialogue] = useState<DialogueDto | null>(null);
  const [messages, setMessages] = useState<MessageDto[]>([]);

  useEffect(() => {
    loadDialogues();
    loadUsers();
  }, []);

  const loadDialogues = async () => {
    try {
      setLoading(true);
      // Load dialogues for all users (simplified)
      const userList = await userApi.getUsers();
      const allDialogues: DialogueDto[] = [];
      const seenIds = new Set<number>();

      for (const user of userList.slice(0, 10)) { // Limit to prevent too many requests
        try {
          const userDialogues = await chatApi.getUserDialogues(user.id);
          for (const dialogue of userDialogues) {
            if (dialogue.id && !seenIds.has(dialogue.id)) {
              seenIds.add(dialogue.id);
              allDialogues.push(dialogue);
            }
          }
        } catch (err) {
          // Ignore individual user errors
        }
      }
      setDialogues(allDialogues);
    } catch (err) {
      console.error('Failed to load dialogues:', err);
      setError('加载聊天记录失败');
    } finally {
      setLoading(false);
    }
  };

  const loadUsers = async () => {
    try {
      const userList = await userApi.getUsers();
      setUsers(userList);
    } catch (err) {
      console.error('Failed to load users:', err);
    }
  };

  const getUserById = (userId: number) => {
    return users.find(user => user.id === userId);
  };

  const handleViewMessages = async (dialogue: DialogueDto) => {
    if (!dialogue.id) return;

    setSelectedDialogue(dialogue);
    try {
      const messageList = await chatApi.getDialogueMessages(dialogue.id);
      setMessages(messageList);
    } catch (err) {
      console.error('Failed to load messages:', err);
      setError('加载消息失败');
    }
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full"><div className="text-lg">加载中...</div></div>;
  }

  return (
    <div className="space-y-6">
      {error && <div className="bg-red-50 border border-red-200 rounded-md p-4"><div className="text-red-800">{error}</div></div>}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Dialogues List */}
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-medium text-gray-900">聊天对话列表 ({dialogues.length})</h3>
          </div>
          <div className="overflow-y-auto max-h-96">
            <ul className="divide-y divide-gray-200">
              {dialogues.map((dialogue) => (
                <li
                  key={dialogue.id}
                  className={`px-6 py-4 hover:bg-gray-50 cursor-pointer ${
                    selectedDialogue?.id === dialogue.id ? 'bg-blue-50' : ''
                  }`}
                  onClick={() => handleViewMessages(dialogue)}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <p className="text-sm font-medium text-gray-900">{dialogue.title}</p>
                      <p className="text-sm text-gray-500">
                        {dialogue.participantIds.length} 个参与者
                      </p>
                    </div>
                    <div className="text-sm text-gray-500">
                      {dialogue.createdAt ? new Date(dialogue.createdAt).toLocaleDateString('zh-CN') : '-'}
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* Messages */}
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-medium text-gray-900">
              {selectedDialogue ? `对话: ${selectedDialogue.title}` : '选择对话查看消息'}
            </h3>
          </div>
          <div className="overflow-y-auto max-h-96 p-6">
            {selectedDialogue ? (
              messages.length > 0 ? (
                <div className="space-y-4">
                  {messages.map((message) => (
                    <div
                      key={message.id}
                      className={`flex ${message.senderId === selectedDialogue.participantIds[0] ? 'justify-start' : 'justify-end'}`}
                    >
                      <div
                        className={`max-w-xs px-4 py-2 rounded-lg ${
                          message.senderId === selectedDialogue.participantIds[0]
                            ? 'bg-gray-100 text-gray-900'
                            : 'bg-blue-100 text-blue-900'
                        }`}
                      >
                        <p className="text-xs text-gray-500 mb-1">
                          {getUserById(message.senderId)?.username || `用户 ${message.senderId}`}
                        </p>
                        <p className="text-sm">{message.content}</p>
                        <p className="text-xs text-gray-400 mt-1">
                          {new Date(message.timestamp).toLocaleString('zh-CN')}
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center text-gray-500">暂无消息记录</div>
              )
            ) : (
              <div className="text-center text-gray-500">请从左侧选择一个对话来查看消息</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatMonitoring;
