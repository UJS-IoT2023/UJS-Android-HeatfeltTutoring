import { Client } from '@stomp/stompjs';
import WebSocket from 'ws';

// 配置监听参数
const WS_URL = 'ws://localhost:8080/ws';
const DIALOGUE_ID = 1; // 监听对话ID 1

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
    console.log('✅ Connected to WebSocket server');
    console.log(`🎧 Listening to dialogue ${DIALOGUE_ID}...`);
    console.log('📝 Press Ctrl+C to stop listening\n');

    // 订阅对话消息
    client.subscribe(`/topic/dialogue/${DIALOGUE_ID}`, (message: any) => {
        try {
            const messageData = JSON.parse(message.body);
            const timestamp = new Date(messageData.createdAt).toLocaleString('zh-CN');

            console.log('📨 Received message:');
            console.log(`   🆔 ID: ${messageData.id}`);
            console.log(`   👤 Sender: ${messageData.senderUsername} (ID: ${messageData.senderId})`);
            console.log(`   💬 Content: ${messageData.content}`);
            console.log(`   🕒 Time: ${timestamp}`);
            console.log('   ──────────────────────────────────\n');
        } catch (error) {
            console.error('❌ Failed to parse message:', error);
            console.log('Raw message:', message.body);
        }
    });

    console.log(`📡 Subscribed to /topic/dialogue/${DIALOGUE_ID}`);
};

// 连接错误回调
client.onStompError = (frame: any) => {
    console.error('❌ STOMP error:', frame.headers['message']);
    console.error('Details:', frame.body);
};

// 连接断开回调
client.onDisconnect = () => {
    console.log('🔌 Disconnected from WebSocket server');
};

// 处理程序退出
process.on('SIGINT', () => {
    console.log('\n🛑 Stopping listener...');
    client.deactivate();
    process.exit(0);
});

process.on('SIGTERM', () => {
    console.log('\n🛑 Stopping listener...');
    client.deactivate();
    process.exit(0);
});

// 开始连接
console.log('🚀 Starting WebSocket chat listener...');
console.log(`🔗 Connecting to: ${WS_URL}`);
console.log(`💭 Dialogue ID: ${DIALOGUE_ID}`);
console.log('⏳ Connecting...\n');

client.activate();
