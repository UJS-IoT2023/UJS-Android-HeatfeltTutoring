import { Client } from '@stomp/stompjs';
import WebSocket from 'ws';

// 配置测试参数
const WS_URL = 'ws://localhost:8080/ws';
const DIALOGUE_ID = 1; // 使用对话ID 1进行测试
const SENDER_ID = 5; // 使用发送者ID 1进行测试
const SENDER_USERNAME = 'TestUser';

// 创建STOMP客户端
const client = new Client({
    webSocketFactory: () => new WebSocket(WS_URL) as any,
    debug: (str: string) => console.log('STOMP Debug:', str),
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

// 连接成功回调
client.onConnect = (frame: any) => {
    console.log('Connected to WebSocket server:', frame);

    // 订阅对话消息
    const subscription = client.subscribe(`/topic/dialogue/${DIALOGUE_ID}`, (message: any) => {
        console.log('Received message:', message.body);

        try {
            const messageData = JSON.parse(message.body);
            console.log('Parsed message:', {
                id: messageData.id,
                senderId: messageData.senderId,
                senderUsername: messageData.senderUsername,
                content: messageData.content,
                createdAt: messageData.createdAt
            });
        } catch (error) {
            console.error('Failed to parse message:', error);
        }
    });

    console.log(`Subscribed to dialogue ${DIALOGUE_ID}`);

    // 模拟发送几条测试消息
    setTimeout(() => {
        sendTestMessage('Hello from TypeScript test!');
    }, 1000);

    setTimeout(() => {
        sendTestMessage('This is a real-time chat test message.');
    }, 3000);

    setTimeout(() => {
        sendTestMessage('Testing WebSocket functionality with STOMP.');
    }, 5000);

    // 7秒后断开连接
    setTimeout(() => {
        console.log('Test completed, disconnecting...');
        subscription.unsubscribe();
        client.deactivate();
    }, 7000);
};

// 连接错误回调
client.onStompError = (frame: any) => {
    console.error('STOMP error:', frame.headers['message']);
    console.error('Details:', frame.body);
};

// 发送测试消息的函数
function sendTestMessage(content: string): void {
    if (!client.connected) {
        console.error('Client not connected, cannot send message');
        return;
    }

    const message = {
        dialogueId: DIALOGUE_ID,
        senderId: SENDER_ID,
        senderUsername: SENDER_USERNAME,
        content: content
    };

    console.log('Sending message:', message);

    client.publish({
        destination: `/app/chat/${DIALOGUE_ID}/send`,
        body: JSON.stringify(message)
    });
}

// 连接断开回调
client.onDisconnect = () => {
    console.log('Disconnected from WebSocket server');
};

// 开始连接
console.log('Starting WebSocket chat test...');
console.log(`Connecting to: ${WS_URL}`);
console.log(`Testing dialogue ID: ${DIALOGUE_ID}`);

client.activate();
