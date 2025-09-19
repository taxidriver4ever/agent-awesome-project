<template>
    <div class="voice-to-ai">
        <ElContainer>
            <ElAside style="width: 50vh;background-color: black;">

            </ElAside>
            <ElAside
                style="background-color: rgba(0, 0, 0, 0.5); display: flex; flex-direction: column;width: 9vh; border-radius: 20px;">
                <div style="display: flex; text-align: center; margin: 0 auto;">
                    <ElButton type="text" class="back-button">
                        <ElIcon size="25" style="margin-top: 40px;" @click="$router.back()">
                            <Back />
                        </ElIcon>
                    </ElButton>
                </div>
                <div style="display: flex; margin-top: 100px;">
                    <ElAvatar class="currentAvatar" :class="{ 'currentAvatarA': currentAvatarA }"
                        src="/src/images/007-gintonic.svg" fit="contain" />
                </div>
                <div style="display: flex; margin-top: 20px;">
                    <ElAvatar class="currentAvatar" :class="{ 'currentAvatarB': currentAvatarB }"
                        src="/src/images/014-mojito.svg" fit="contain" />
                </div>
                <div style="display: flex; margin-top: 20px;">
                    <ElAvatar class="currentAvatar" :class="{ 'currentAvatarC': currentAvatarC }"
                        src="/src/images/042-milkshake.svg" fit="contain" />
                </div>
                <div style="display: flex; margin-top: 20px;">
                    <ElAvatar class="currentAvatar" :class="{ 'currentAvatarD': currentAvatarD }"
                        src="/src/images/044-whiskeysour.svg" fit="contain" />
                </div>
                <div style="display: flex; margin-top: 20px;">
                    <ElAvatar class="currentAvatar" :class="{ 'currentAvatarE': currentAvatarE }"
                        src="/src/images/050-lemonjuice.svg" fit="contain" />
                </div>
            </ElAside>
            <ElContainer style="background-color: rgba(0, 0, 0, 0);">
                <ElHeader style="height: 20vh; color: black;display: flex; flex-direction: row; text-align: center;">
                    <div
                        style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 100%;">
                        <h2>Voice to AI</h2>
                        <div style="margin-bottom: 20px;">Speak. It Understands</div>
                        <el-icon size="30">
                            <Service />
                        </el-icon>
                    </div>
                </ElHeader>
                <ElMain class="chat-main">
                    <div style="display: flex;" v-for="(transcriptForVoice, index) in transcripts" :key="index">
                        <div style="margin-left: auto; margin-top: 10px;" v-if="transcriptForVoice.status === 'SENT'">
                            {{ transcriptForVoice.content }}
                        </div>
                        <div style="margin-right: auto; margin-top: 10px;" v-else>
                            {{ transcriptForVoice.content }}
                            <br></br>
                            <ElImage style="height: auto;width: auto;" v-if="transcriptForVoice.image !== null && transcriptForVoice.image !== ''" :src="transcriptForVoice.image"></ElImage>
                        </div>
                    </div>
                </ElMain>
                <ElFooter style="height: 23vh;">
                    <!-- <div style="display: flex; justify-content: center; margin-top: 10px;">
                        <ElButton v-show="currentAvatarA || currentAvatarB ||currentAvatarC ||currentAvatarD || currentAvatarE " @click="stopService" type="danger" :round="true" plain>
                            <el-icon size="20"><SwitchButton /></el-icon>
                        </ElButton> 
                    </div> -->
                    <div class="voice-container">

                        <button @click="isListening ? stopRecognition() : startRecognition()"
                            :class="{ 'listening': isListening }" class="record-btn">
                            <el-icon v-if="isListening" style="color: black;" size="30">
                                <Microphone />
                            </el-icon>
                            <ElIcon v-else style="color: black;" size="30">
                                <Mute />
                            </ElIcon>
                        </button>

                        <div v-if="isListening" class="status">● listening...</div>

                        <!-- <div v-else-if="error" class="error">{{ error }}</div> -->
                    </div>
                </ElFooter>
            </ElContainer>
            <ElAside style="width: 50vh;background-color: black;">

            </ElAside>
        </ElContainer>
    </div>
</template>

<script lang="ts" setup>
import { ElAside, ElAvatar, ElButton, ElContainer, ElFooter, ElHeader, ElIcon, ElImage, ElMain, ElMessage } from 'element-plus'
import { ref, onUnmounted, nextTick, onMounted, computed } from 'vue'
import { Microphone, Mute, Back, Service, SwitchButton } from '@element-plus/icons-vue'
import axios from 'axios'
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs'

// 声明 SpeechRecognition 类型（兼容 Chrome 前缀）
declare global {
    interface Window {
        SpeechRecognition: any
        webkitSpeechRecognition: any
    }
}
type SpeechRecognition = typeof window.SpeechRecognition

const currentAvatarA = computed(() => {
    return currentAvatar.value === '/src/images/007-gintonic.svg'
})

const currentAvatarB = computed(() => {
    return currentAvatar.value === '/src/images/014-mojito.svg'
})

const currentAvatarC = computed(() => {
    return currentAvatar.value === '/src/images/042-milkshake.svg'
})

const currentAvatarD = computed(() => {
    return currentAvatar.value === '/src/images/044-whiskeysour.svg'
})

const currentAvatarE = computed(() => {
    return currentAvatar.value === '/src/images/050-lemonjuice.svg'
})

interface VoiceChatMessage {
    userId: string
    content: string
    timestamp: string
    status: string
    avatar: string
    image: string
}

class VoiceChatMessage {
    userId: string;
    content: string;
    timestamp: string;
    status: string;
    avatar: string;
    image: string;

    constructor(userId: string, content: string, timestamp: string, status: string, avatar: string, image: string) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
        this.avatar = avatar;
        this.image = image
    }
}

// 使用Ref创建响应式变量
const isListening = ref(false)
const transcript = ref('')
const error = ref('')
const recognition = ref<SpeechRecognition | null>(null)
const transcripts = ref<VoiceChatMessage[]>([])
const isSpeaking = ref(false)
const speechSynthesis = window.speechSynthesis
const stompClient = ref<Client | null>(null); // STOMP客户端
const isConnected = ref(false); // WebSocket连接状态
const websocketUrl = ref('http://localhost:8080/ws-chat'); // WebSocket服务器URL
const currentAvatar = ref(''); // 当前用户头像

// 朗读文本函数
const speakText = (text: string, lang = 'en-US') => {
    // 停止当前正在进行的朗读
    stopSpeaking()

    // 创建新的语音实例
    const utterance = new SpeechSynthesisUtterance(text)

    // 1. 首先获取所有可用语音
    const voices = window.speechSynthesis.getVoices();

    utterance.lang = lang // 设置语言
    utterance.rate = 1.0  // 语速 (0.1 到 10)
    utterance.pitch = 1.0 // 音调 (0 到 2)
    utterance.volume = 1.0 // 音量 (0 到 1)

    // 事件监听
    utterance.onstart = () => {
        isSpeaking.value = true
        console.log('开始朗读')
    }

    utterance.onend = () => {
        isSpeaking.value = false
        console.log('朗读结束')
    }

    utterance.onerror = (event) => {
        isSpeaking.value = false
        console.error('朗读出错:', event)
    }

    // 开始朗读
    speechSynthesis.speak(utterance)
}

// 停止朗读函数
const stopSpeaking = () => {
    if (speechSynthesis.speaking) {
        speechSynthesis.cancel()
        isSpeaking.value = false
    }
}

// 检查浏览器支持性
const isSpeechRecognitionSupported = () => {
    return 'SpeechRecognition' in window || 'webkitSpeechRecognition' in window
}

// 自动滚动到底部
const scrollToBottom = () => {
    nextTick(() => {
        const container = document.querySelector('.chat-main');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    });
};

// 连接到WebSocket服务器
const connect = () => {
    try {
        stompClient.value = new Client({
            webSocketFactory: () => new SockJS(websocketUrl.value),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            connectHeaders: {
                "id": localStorage.getItem("id") || "",
                "uuid": localStorage.getItem("uuid") || ""
            },
            onConnect: () => {
                console.log('STOMP连接已建立');
                // 订阅私聊消息
                stompClient.value?.subscribe(
                    `/user/queue/VoiceChat.private`,
                    (message) => {
                        const chatMessage = JSON.parse(message.body);
                        console.log(message);
                        handleIncomingMessage(chatMessage);
                    }
                );

                isConnected.value = true;
            },
            onStompError: (frame) => {
                console.error('STOMP协议错误:', frame);
                alert('STOMP协议错误: ' + frame.headers['message']);
            },
            onWebSocketError: (event) => {
                console.error('WebSocket错误:', event);
                alert('WebSocket连接错误，请检查URL和服务器状态');
            },
            onDisconnect: () => {
                console.log('STOMP连接已断开');
            }
        });

        // 激活客户端
        stompClient.value.activate();
    } catch (error) {
        console.error('连接失败:', error);
    }
};

function handleIncomingMessage(message: any) {
    console.log('收到消息:', message);
    transcripts.value.push(new VoiceChatMessage(message.userId, message.content, message.timestamp, message.status, message.avatar, message.image));
    currentAvatar.value = "/src/images/" + message.avatar; // 更新当前用户头像
    console.log("当前的状态" + message.status);
    // console.log(message.status === null);
    if (message.status === null) speakText(message.content, 'zh-CN'); // 朗读AI回复
    scrollToBottom(); // 滚动到底部
}

// function stopService() {
//     // 创建消息对象
//         const chatMessage = new VoiceChatMessage(
//             localStorage.getItem("id") || "",
//             "",
//             new Date().toISOString(),
//             "SENT",
//             ""
//         );
//     // 发送消息到服务器
//     if (stompClient.value && isConnected.value) {
//         // alert("Stopping the service");
//         stompClient.value.publish({
//             destination: '/app/VoiceChat.stopService',
//             body: JSON.stringify(chatMessage)
//         });
//     }
// }

// 初始化语音识别
const initSpeechRecognition = () => {
    // 处理浏览器前缀
    const SpeechRecognition = window.SpeechRecognition || (window as any).webkitSpeechRecognition

    if (!SpeechRecognition) {
        error.value = '您的浏览器不支持语音识别功能'
        return null
    }

    const recognizer = new SpeechRecognition()
    recognizer.continuous = true // 持续监听
    recognizer.interimResults = true // 实时返回中间结果
    recognizer.lang = 'zh-CN' // 设置中文识别

    recognizer.onresult = (event: any) => {
        let finalTranscript = ''
        for (let i = event.resultIndex; i < event.results.length; i++) {
            const transcript = event.results[i][0].transcript
            if (event.results[i].isFinal) {
                finalTranscript += transcript + ' '
            } else {
                // 实时显示中间结果
                transcript.value = transcript
            }
        }
        if (finalTranscript) {
            transcript.value += finalTranscript
            console.log('Final Transcript:', finalTranscript)
            stopRecognition() // 识别完成后停止
            sendMessage(finalTranscript.trim()) // 发送消息到服务器
            scrollToBottom() // 滚动到底部
        }
    }

    recognizer.onerror = (event: any) => {
        console.error('语音识别错误:', event.error)
        error.value = `识别错误: ${event.error}`
        ElMessage.error(`${event.error}`)
        stopRecognition()
    }

    recognizer.onend = () => {
        if (isListening.value) {
            // 如果还在监听状态，重新启动（实现持续监听）
            recognizer.start()
        }
    }

    return recognizer
}

// 发送消息
async function sendMessage(message: string) {
    if (stompClient.value && isConnected.value) {
        // 创建消息对象
        const chatMessage = new VoiceChatMessage(
            localStorage.getItem("id") || "",
            message,
            new Date().toISOString(),
            "SENT",
            "",
            ""
        );

        // 发送消息到服务器
        stompClient.value.publish({
            destination: '/app/VoiceChat.private',
            body: JSON.stringify(chatMessage)
        });

    } else {
        ElMessage.error('未连接到聊天服务器');
    }
}

// 开始语音识别
const startRecognition = () => {
    stopSpeaking() // 停止当前朗读

    if (stompClient.value && isConnected.value) {
        // 创建消息对象
        const chatMessage = new VoiceChatMessage(
            localStorage.getItem("id") || "",
            "",
            new Date().toISOString(),
            "SENT",
            "",
            ""
        );

        // 发送消息到服务器
        stompClient.value.publish({
            destination: '/app/VoiceChat.StopService',
            body: JSON.stringify(chatMessage)
        });

    } else {
        ElMessage.error('未连接到聊天服务器');
    }

    transcript.value = ' ' // 清空之前的文本

    if (!isSpeechRecognitionSupported()) {
        error.value = '浏览器不支持语音识别，请使用Chrome、Edge等现代浏览器'
        return
    }

    if (!recognition.value) {
        recognition.value = initSpeechRecognition()
    }

    if (recognition.value) {
        try {
            recognition.value.start()
            isListening.value = true
            error.value = ''
        } catch (err) {
            error.value = '无法启动语音识别'
            console.error(err)
        }
    }
}

// 停止语音识别
const stopRecognition = () => {
    if (recognition.value && isListening.value) {

        recognition.value.stop()
        isListening.value = false
    }
}
onMounted(() => {
    ElMessage.info("Your can speak Stop。 or 暂停。 to stop the service.");
    connect(); // 连接WebSocket服务器
});

// 组件卸载时清理资源
onUnmounted(() => {
    stopRecognition()
    startRecognition();
})
</script>

<style scoped>
.voice-container {
    padding: 20px;
    max-width: 600px;
    margin: 0 auto;
    text-align: center;
}

.record-btn {
    width: 80px;
    height: 80px;
    font-size: 18px;
    background-color: white;
    color: white;
    border: none;
    border-radius: 100%;
    cursor: pointer;
    transition: all 0.3s;
    margin: 20px 0;
}

.record-btn:hover {
    background-color: white;
    transform: scale(1.05);
}

.record-btn.listening {
    background-color: #f44336;
    animation: pulse 1.5s infinite;
}

.record-btn.listening:hover {
    background-color: #da190b;
}

@keyframes pulse {
    0% {
        transform: scale(1);
    }

    50% {
        transform: scale(1.1);
    }

    100% {
        transform: scale(1);
    }
}

.status {
    color: #f44336;
    font-weight: bold;
}

.error {
    color: #f44336;
    background-color: #ffebee;
    padding: 10px;
    border-radius: 5px;
    margin-top: 15px;
}

.back-button {
    color: black;
}

.back-button:hover {
    color: rgba(0, 0, 0, 0.4) !important;
}

.voice-to-ai {
    background-image: url("/src/images/bg.png");
    background-size: cover;
    overflow: hidden;
    height: 100vh;
}

.chat-main {
    display: flex;
    height: 57vh;
    color: black;
    flex-direction: column;
    overflow-y: auto;
    padding: 10px;
    display: flex;
    height: 57vh;
    flex-direction: column;
}

.chat-main::-webkit-scrollbar {
    color: #000;
}

.currentAvatar {
    width: 50px;
    height: 50px;
    margin-top: 20px;
    margin-left: 10px;
}

.currentAvatar.currentAvatarA {
    border: 2px solid #f44336;
    /* 红色边框 */
    box-shadow: 0 0 10px rgba(244, 67, 54, 0.7);
    /* 红色光晕 */
    animation: pulse 1.5s infinite;
}

.currentAvatar.currentAvatarB {
    border: 2px solid #f44336;
    /* 红色边框 */
    box-shadow: 0 0 10px rgba(244, 67, 54, 0.7);
    /* 红色光晕 */
    animation: pulse 1.5s infinite;
}

.currentAvatar.currentAvatarC {
    border: 2px solid #f44336;
    /* 红色边框 */
    box-shadow: 0 0 10px rgba(244, 67, 54, 0.7);
    /* 红色光晕 */
    animation: pulse 1.5s infinite;
}

.currentAvatar.currentAvatarD {
    border: 2px solid #f44336;
    /* 红色边框 */
    box-shadow: 0 0 10px rgba(244, 67, 54, 0.7);
    /* 红色光晕 */
    animation: pulse 1.5s infinite;
}

.currentAvatar.currentAvatarE {
    border: 2px solid #f44336;
    /* 红色边框 */
    box-shadow: 0 0 10px rgba(244, 67, 54, 0.7);
    /* 红色光晕 */
    animation: pulse 1.5s infinite;
}
</style>