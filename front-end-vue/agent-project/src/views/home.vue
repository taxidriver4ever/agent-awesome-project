<template>
    <ElContainer class="home-container">
        <ElHeader height="60px" class="header">
            <div class="header-left">
                <ElTooltip content="Login" placement="bottom">
                    <button class="login-button" @click="ToLoginForm">
                        <ElIcon :size="20">
                            <User />
                        </ElIcon>
                    </button>
                </ElTooltip>
                <ElTooltip content="Login by email" placement="right">
                    <button class="login-by-email-button" @click="ToEmailForm">
                        <ElIcon :size="20">
                            <Message />
                        </ElIcon>
                    </button>
                </ElTooltip>
            </div>
            <h2 class="header-center" style="display: flex;">SCAU Ai Helper</h2>
            <div class="header-right">
                <ElTooltip content="Register" placement="left">
                    <button class="register-button" @click="RegisterForm = true">
                        <ElIcon :size="20">
                            <Edit />
                        </ElIcon>
                    </button>
                </ElTooltip>
                <ElTooltip content="Logout" placement="bottom">
                    <button v-if="LoggedIn" class="logout-button" @click="logout">
                        <ElIcon :size="20">
                            <SwitchButton />
                        </ElIcon>
                    </button>
                </ElTooltip>
            </div>
        </ElHeader>
        <ElMain style="margin: 0;padding: 0;">
            <div class="menu">
                <button @click="ChatWithAi">Chat with AI</button>
                <button>Personal Details</button>
                <button @click="Friends">Friends</button>
                <button @click="VoiceToAi">Voice to AI</button>
                <button>Settings</button>
            </div>
            <div style="text-align: center; margin-top: 200px;">
                <h1
                    style="color: white; font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif; font-size: 48px; font-weight: bold; padding-top: 10px;">
                    Welcome to SCAU Ai Helper</h1>
                <p
                    style="color: white; font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif; font-size: 24px;">
                    Your personal AI assistant for everything
                </p>
            </div>
        </ElMain>

        <!-- 登录对话框 -->
        <ElDialog class="login-form" v-model="LoginForm" width="30%" :close-on-click-modal="false" transition="bounce">
            <template #title>Login</template>
            <div style="display: flex; flex-direction: column; margin-top: 10px;">
                <el-input placeholder="Enter your username" v-model="userNameLP"
                    style="margin-bottom: 25px;"></el-input>
                <el-input type="password" show-password placeholder="Enter your password" v-model="userPasswordLP"></el-input>
                <div style="display: flex; margin-top: 20px; justify-content: flex-end;">
                    <ElButton class="first-button" @click="LoginByPassword">Login</ElButton>
                    <ElButton class="second-button" @click="LoginForm = false, userNameLP = '', userPasswordLP = ''"
                        type="warning" :plain="true">Cancel
                    </ElButton>
                </div>
            </div>
        </ElDialog>

        <!-- 注册对话框 -->
        <ElDialog class="register-form" v-model="RegisterForm" width="30%" :close-on-click-modal="false"
            transition="bounce">
            <template #title>Register</template>
            <div style="display: flex; flex-direction: column; margin-top: 10px;">
                <el-input placeholder="Enter your username" v-model="userNameR" style="margin-bottom: 20px;"></el-input>
                <el-input type="password" show-password placeholder="Enter your password" v-model="userPasswordR"
                    style="margin-bottom: 20px;"></el-input>
                <el-input type="password" show-password placeholder="Confirm your password" v-model="userPassword2R"
                    style="margin-bottom: 20px;"></el-input>
                <el-input placeholder="Enter your email" v-model="userEmailR" style="margin-bottom: 12px;"></el-input>
                <div style="display: flex; flex-direction: row;">
                    <el-input placeholder="Enter your code" v-model="codeR"
                        style="height: 33px; margin-top: 9px;"></el-input>
                    <el-button class="third-button" style="margin-left: 150px;" @click="SendCodeR" :round="true"
                        :plain="true" :disabled="isSendButtonDisabledR"
                        :type="isSendButtonDisabledR ? 'info' : 'warning'">
                        {{ sendButtonTextR }}
                    </el-button>
                </div>
                <div style="display: flex; margin-top: 20px; justify-content: flex-end;">
                    <ElButton class="first-button" type="warning" @click="Register">Register</ElButton>
                    <ElButton class="second-button"
                        @click="RegisterForm = false, userNameR = '', userPasswordR = '', userPassword2R = '', userEmailR = '', codeR = ''"
                        type="warning" :plain="true">Cancel</ElButton>
                </div>
            </div>
        </ElDialog>

        <!-- 邮箱登录对话框 -->
        <ElDialog class="email-form" v-model="EmailForm" width="30%" :close-on-click-modal="false" transition="bounce">
            <template #title>Login</template>
            <div style="display: flex; flex-direction: column; margin-top: 10px;">
                <el-input placeholder="Enter your email" v-model="userEmailLE" style="margin-bottom: 12px;"></el-input>
                <div style="display: flex; flex-direction: row;">
                    <el-input placeholder="Enter your code" v-model="codeLE"
                        style="height: 33px; margin-top: 9px;"></el-input>
                    <el-button class="third-button" style="margin-left: 150px;" @click="SendCode" :round="true"
                        :plain="true" :disabled="isSendButtonDisabled"
                        :type="isSendButtonDisabled ? 'info' : 'warning'">
                        {{ sendButtonText }}
                    </el-button>
                </div>
                <div style="display: flex; margin-top: 20px; justify-content: flex-end;">
                    <ElButton class="first-button" type="warning" @click="LoginByEmail">Login</ElButton>
                    <ElButton class="second-button" type="warning" :plain="true"
                        @click="EmailForm = false, codeLE = '', userEmailLE = ''">
                        Cancel
                    </ElButton>
                </div>
            </div>
        </ElDialog>
        <!-- 新增的白色覆盖面板 -->
        <transition name="slide-up">
            <div class="introduce-overlay"
                :style="{ transform: `translateY(${overlayPosition}%)`, borderRadius: calculateBorderRadius() }"
                @wheel="handleWheel">
                <div class="image-container">
                    <div class="card" @click="Gintonic">
                        <div>
                            <h2>Gintonic</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/007-gintonic.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Mojito">
                        <div>
                            <h2>Mojito</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/014-mojito.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Coconutdrink">
                        <div>
                            <h2>Coconutdrink</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/039-coconutdrink.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Milkshake">
                        <div>
                            <h2>Milkshake</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/042-milkshake.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Whiskeysour">
                        <div>
                            <h2>Whiskeysour</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/044-whiskeysour.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Gingerbread">
                        <div>
                            <h2>Gingerbread</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/048-gingerbread.svg"></ElImage>
                        </div>
                    </div>
                    <div class="card" @click="Lemonjuice">
                        <div>
                            <h2>Lemonjuice</h2>
                        </div>
                        <div>
                            <ElImage src="/src/images/050-lemonjuice.svg"></ElImage>
                        </div>
                    </div>
                </div>
                <ElDivider style="border-color: black;"></ElDivider>
                <div style="font-family:monospace; margin: 30vh 0;">
                    <div style="display: flex; justify-content: center; font-size: 100px;">There are</div>
                    <div style="display: flex; justify-content: center; font-size: 100px;">plenty of features</div>
                    <div style="display: flex; justify-content: center; font-size: 100px;">here</div>
                </div>
                <div class="ad-container">
                    <ElImage src="/src/images/advertisement.png" class="ad-image"></ElImage>
                </div>
                <div class="ad-container">
                    <ElImage src="/src/images/advertisement_2.png" class="ad-image"></ElImage>
                </div>
            </div>
        </transition>
        <ElDialog
            style="width: 70vh; color: black; background-image: none; background-color: white !important; border-radius: 20px;"
            v-model="drinkIntroduce" transition="bounce" :show-close="false">
            <div v-if="selectedDrink === 'Gintonic'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Gintonic</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">Meet my mental AI assistant.
                    It's my personalized digital companion,
                    designed to be a safe space for my thoughts. It listens without judgment,
                    helps me organize my ideas, and offers insights when I need a fresh perspective.
                    It's like having a continuous, supportive conversation that helps me navigate my inner world,
                    clarify my emotions, and boost my mental well-being.
                    It’s always there, ready to help me think things through.</div>
            </div>
            <div v-else-if="selectedDrink === 'Mojito'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Mojito</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">Meet my mental AI assistant.
                    Meet my health AI assistant, my personal wellness companion. It's a smart, digital tool designed to
                    support
                    my well-being 24/7. It helps me track my nutrition, monitor my activity levels, and analyze my sleep
                    patterns to provide personalized insights.
                    It offers gentle reminders to stay hydrated, suggests quick workouts based on my goals, and even
                    helps me
                    understand my body's signals. It’s like having a knowledgeable health coach in my pocket, empowering
                    me to
                    make smarter, data-driven decisions for a healthier lifestyle.
                    It’s always there to guide and motivate me on my journey to better health.</div>
            </div>
            <div v-else-if="selectedDrink === 'Coconutdrink'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Coconutdrink</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">Meet my mental AI assistant.
                    This is me, in digital form. Everything you see here—the posts, the likes, the shares—is a genuine
                    representation of who I am and what I care about. Consider this profile my own personal corner of
                    the
                    internet.</div>
            </div>
            <div v-else-if="selectedDrink === 'Milkshake'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Milkshake</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">
                    This is my Campus Life AI Assistant, my go-to guide for everything university life has to offer
                    outside the
                    classroom. Think of it less as a study buddy and more as my personal concierge for campus living.
                    It's the expert on student life. It knows about every club fair, intramural sports sign-up, and
                    campus
                    concert, sending me personalized recommendations based on my hobbies. It helps me navigate the
                    social scene,
                    suggesting events where I can meet new people and try new things.
                    It's also my practical sidekick. It can tell me which dining hall is serving the best menu tonight,
                    where to
                    find the cheapest coffee near campus, and how to get involved in student governance or volunteer
                    opportunities. It even helps me manage a balanced budget for my social activities and explores ways
                    to get
                    discounted tickets for local events.
                    In short, this assistant is all about enhancing my student experience. It's here to help me build
                    community,
                    discover new passions, and make the most of my time on campus, ensuring my university years are as
                    fulfilling socially and personally as they are academically.
                </div>
            </div>
            <div v-else-if="selectedDrink === 'Whiskeysour'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Whiskeysour</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">
                    This is my Thesis and Study AI Assistant, my dedicated partner in academic excellence. It's a
                    specialized
                    tool designed to streamline the research and learning process, helping me work smarter and more
                    efficiently.
                    It assists me in organizing complex research materials, generating outlines for papers, and ensuring
                    my
                    citations are perfectly formatted in any required style (like APA or MLA). When I'm grappling with
                    dense
                    academic texts, it can summarize key points, explain complicated concepts, and help me strengthen my
                    arguments.
                    Beyond writing, it's a powerful study aid. It can create practice quizzes from my notes, generate
                    flashcards
                    for memorization, and help me build structured study plans for upcoming exams. It's like having a
                    personal
                    research librarian, writing tutor, and study coach available 24/7, all working together to help me
                    produce
                    my best work and achieve my academic goals with greater confidence and clarity.
                </div>
            </div>
            <div v-else-if="selectedDrink === 'Gingerbread'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Gingerbread</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">
                    Meet your Error Recovery Assistant, a helpful AI that appears exactly when things go wrong. It's
                    more than
                    just an error message; it's a friendly and proactive guide designed to get you back on track quickly
                    and
                    effortlessly.
                    When a page fails to load or an app crashes, this assistant instantly pops up. Instead of showing a
                    confusing error code, it provides a clear, simple explanation of what might have happened in plain
                    language.
                    It then offers you smart, one-click solutions like "Retry the Connection," "Return to Safety," or
                    "Report
                    the Issue."
                    Powered by context-aware AI, it can often diagnose the problem—whether it's a weak network, a server
                    issue,
                    or a minor glitch—and can even attempt to automatically fix it for you in the background. Its
                    calming
                    presence and practical help transform a moment of frustration into a smooth, assisted recovery,
                    ensuring you
                    never feel lost or stuck.
                </div>
            </div>
            <div v-else-if="selectedDrink === 'Lemonjuice'">
                <h2
                    style="margin: 0;font-size: 30px;font-family:'Courier New', Courier, monospace; color: black; margin-bottom: 30px;">
                    Lemonjuice</h2>
                <div style="font-family: initial;font-weight: 500; color: black;">
                    Struggling with the daunting task of writing your thesis or dissertation? Meet AISistant, your intelligent and reliable thesis writing assistant,
                     designed to streamline your research journey from a blank page to a polished, submission-ready document.
                </div>
            </div>
        </ElDialog>
    </ElContainer>
</template>

<script lang="ts" setup>
import { ElButton, ElContainer, ElDialog, ElDivider, ElHeader, ElInput, ElMessage, ElMessageBox, ElIcon, ElTooltip, ElMain, ElImage } from 'element-plus';
import { User, Message, Edit, SwitchButton, Setting, Help, ChatDotRound } from '@element-plus/icons-vue';
import { computed, onUnmounted, ref, onMounted } from 'vue';
import axios from 'axios';

const LoginForm = ref(false)
const RegisterForm = ref(false)
const EmailForm = ref(false)
const drinkIntroduce = ref(false)
const selectedDrink = ref('')
const userNameLP = ref('')
const userPasswordLP = ref('')
const userEmailLE = ref('')
const codeLE = ref('')
const userNameR = ref('')
const userPasswordR = ref('')
const userPassword2R = ref('')
const userEmailR = ref('')
const codeR = ref('')
const LoggedIn = ref(localStorage.getItem('id') !== null && localStorage.getItem('uuid') !== null);
const standardEmailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
// 添加倒计时相关变量
const countdown = ref(0); // 倒计时秒数
const countdownR = ref(0); // 倒计时秒数
let countdownTimer: number | null = null; // 定时器引用
let countdownTimerR: number | null = null; // 定时器引用
const isAtTop = ref(false)
const isFullyCovered = computed(() => overlayPosition.value <= 0)
// 添加白色面板相关代码
const overlayPosition = ref(70) // 100%表示完全隐藏在底部
const container = ref<HTMLElement | null>(null)
let isScrolling = false

function Gintonic() { drinkIntroduce.value = true; selectedDrink.value = "Gintonic" }
function Mojito() { drinkIntroduce.value = true; selectedDrink.value = "Mojito" }
function Coconutdrink() { drinkIntroduce.value = true; selectedDrink.value = "Coconutdrink" }
function Milkshake() { drinkIntroduce.value = true; selectedDrink.value = "Milkshake" }
function Whiskeysour() { drinkIntroduce.value = true; selectedDrink.value = "Whiskeysour" }
function Gingerbread() { drinkIntroduce.value = true; selectedDrink.value = "Gingerbread" }
function Lemonjuice() {drinkIntroduce.value = true; selectedDrink.value = 'Lemonjuice'}


// 添加计算圆角的函数
const calculateBorderRadius = () => {
    const position = overlayPosition.value
    // 当面板位置在 0-100% 之间时，圆角从 0px 到 40px 平滑过渡
    const radius = Math.min(60, Math.max(0, (position / 100) * 60))
    return `${radius}px ${radius}px 0 0`
}

// 修改滚轮事件处理
const handleWheel = (event: WheelEvent) => {
    if (isScrolling) return

    // 阻止默认滚动行为
    event.preventDefault()
    isScrolling = true

    // 计算当前面板可见比例 (0-100%)
    const visiblePercentage = 100 - overlayPosition.value

    // 向下滚动 - 向上移动面板
    if (event.deltaY > 0) {
        // 只有当面板未完全覆盖或处于顶部时允许继续向上滑动
        if (visiblePercentage < 100 || isAtTop.value) {
            const newPosition = Math.max(0, overlayPosition.value - event.deltaY * 1)
            overlayPosition.value = newPosition
        }
    }
    // 向上滚动 - 向下移动面板
    else {
        // 只有当面板完全覆盖且处于顶部时允许向下滑动
        if (isFullyCovered.value && isAtTop.value) {
            const newPosition = Math.min(70, overlayPosition.value - event.deltaY * 1)
            overlayPosition.value = newPosition
        }
    }

    // 防抖处理
    setTimeout(() => {
        isScrolling = false
    }, 50)
}

// 添加滚动事件监听器来检测是否在顶部
onMounted(() => {
    const overlayElement = document.querySelector('.introduce-overlay') as HTMLElement

    if (overlayElement) {
        overlayElement.addEventListener('scroll', () => {
            // 检测是否滚动到顶部
            isAtTop.value = overlayElement.scrollTop === 0
        })
    }

    window.addEventListener('wheel', handleWheel, { passive: false })
})

onUnmounted(() => {
    window.removeEventListener('wheel', handleWheel)
})

// 计算属性：按钮是否禁用
const isSendButtonDisabled = computed(() => countdown.value > 0);

const isSendButtonDisabledR = computed(() => countdownR.value > 0);

// 计算属性：按钮显示的文本
const sendButtonText = computed(() => {
    return countdown.value > 0 ? `Resend(${countdown.value}s)` : 'Send Code';
});

const sendButtonTextR = computed(() => {
    return countdownR.value > 0 ? `Resend(${countdownR.value}s)` : 'Send Code';
});

onMounted(()=>{
    localStorage.removeItem("receiverId")
    localStorage.removeItem("receiverName")
})

// 组件卸载时清除定时器
onUnmounted(() => {
    if (countdownTimer) {
        clearInterval(countdownTimer);
    }
});

// 组件卸载时清除定时器
onUnmounted(() => {
    if (countdownTimerR) {
        clearInterval(countdownTimerR);
    }
});

// 开始倒计时函数
const startCountdown = () => {
    countdown.value = 60; // 设置60秒倒计时

    if (countdownTimer) {
        clearInterval(countdownTimer);
    }

    countdownTimer = window.setInterval(() => {
        if (countdown.value > 0) {
            countdown.value--;
        } else {
            if (countdownTimer) {
                clearInterval(countdownTimer);
                countdownTimer = null;
            }
        }
    }, 1000);
};

// 开始倒计时函数
const startCountdownR = () => {
    countdownR.value = 60; // 设置60秒倒计时

    if (countdownTimerR) {
        clearInterval(countdownTimerR);
    }

    countdownTimerR = window.setInterval(() => {
        if (countdownR.value > 0) {
            countdownR.value--;
        } else {
            if (countdownTimerR) {
                clearInterval(countdownTimerR);
                countdownTimerR = null;
            }
        }
    }, 1000);
};

async function ToLoginForm() {
    if (localStorage.getItem('id') === null && localStorage.getItem('uuid') === null) {
        LoginForm.value = true;
        return;
    }
    else {
        await axios.post('http://localhost:8080/login/CheckIdWithUUID', {
            id: localStorage.getItem('id'),
            uuid: localStorage.getItem('uuid')
        }).then(response => {
            if (response.data.code === 200) {
                ElMessage.info('You are already logged in.');
            } else {
                ElMessage.error('Session expired. Please log in again.');
                localStorage.removeItem('id');
                localStorage.removeItem('uuid');
                LoginForm.value = true;
            }
        }).catch(error => {
            ElMessage.error('Network error. Please try again later.');
            console.error('Network error:', error);
            localStorage.removeItem('id');
            localStorage.removeItem('uuid');
            LoginForm.value = true;
        });
    }
}
async function ToEmailForm() {
    if (localStorage.getItem('id') === null && localStorage.getItem('uuid') === null) {
        EmailForm.value = true;
        return;
    }
    else {
        await axios.post('http://localhost:8080/login/CheckIdWithUUID', {
            id: localStorage.getItem('id'),
            uuid: localStorage.getItem('uuid')
        }).then(response => {
            if (response.data.code === 200) {
                ElMessage.info('You are already logged in.');
            } else {
                ElMessage.error('Session expired. Please log in again.');
                localStorage.removeItem('id');
                localStorage.removeItem('uuid');
                EmailForm.value = true;
            }
        }).catch(error => {
            ElMessage.error('Network error. Please try again later.');
            console.error('Network error:', error);
            localStorage.removeItem('id');
            localStorage.removeItem('uuid');
            EmailForm.value = true;
        });
    }
}
async function LoginByPassword() {
    // 在这里添加登录逻辑，例如发送请求到服务器进行验证
    await axios.post('http://localhost:8080/login/LoginByPassword', {
        userName: userNameLP.value,
        userPassword: userPasswordLP.value
    }).then(response => {
        if (response.data.code !== 200) {
            ElMessage.error('Login failed. Please check your username and password.');
            return;
        }
        userNameLP.value = '';
        userPasswordLP.value = '';
        LoginForm.value = false; // 关闭登录对话框
        ElMessage.info('Login successful!');
        console.log('Login successful:', response.data);
        localStorage.setItem('id', response.data.data.id);
        localStorage.setItem('uuid', response.data.data.uuid);
        LoggedIn.value = true;
        // 处理登录成功后的逻辑，例如存储用户信息、跳转页面等
    })
        .catch(error => {
            ElMessage.error('Login failed. Please try again later.');
            console.error('Login failed:', error);
            // 处理登录失败的逻辑，例如显示错误消息等
        });
}
async function LoginByEmail() {
    if (!standardEmailRegex.test(userEmailLE.value)) {
        ElMessage.error('Please enter a valid email address.');
        return;
    }
    else if (codeLE.value === '') {
        ElMessage.error('Please enter your code.');
        return;
    }
    await axios.post('http://localhost:8080/login/LoginByEmail', {
        userEmail: userEmailLE.value,
        code: codeLE.value
    }).then(response => {
        if (response.data.code !== 200) {
            ElMessage.error('Login failed. Please check your email and code.');
            return;
        }
        userEmailLE.value = '';
        codeLE.value = '';
        EmailForm.value = false; // 关闭登录对话框
        ElMessage.info('Login successful!');
        console.log('Login successful:', response.data);
        localStorage.setItem('id', response.data.data.id);
        localStorage.setItem('uuid', response.data.data.uuid);
        LoggedIn.value = true;
        // 处理登录成功后的逻辑，例如存储用户信息、跳转页面等
    })
        .catch(error => {
            ElMessage.error('Login failed. Please try again later.');
            console.error('Login failed:', error);
            // 处理登录失败的逻辑，例如显示错误消息等
        });
}
async function SendCode() {
    if (!standardEmailRegex.test(userEmailLE.value)) {
        ElMessage.error('Please enter a valid email address.');
        return;
    }
    await axios.post('http://localhost:8080/login/GetVerificationCode', {
        userEmail: userEmailLE.value
    }).then(response => {
        if (response.data.code === 200) {
            ElMessage.info("The verification code was successfully sent")
            startCountdown();
            return;
        }
        else {
            ElMessage.error("This email was not found")
            return;
        }
    })
        .catch(error => {
            ElMessage.error('Fail to send. Please try again later.');
            console.error('Send failed:', error);
            // 处理登录失败的逻辑，例如显示错误消息等
        });
}
async function logout() {
    ElMessageBox.confirm(
        'Are you sure you want to logout?',
        'Warning',
        {
            cancelButtonText: 'Cancel',
            confirmButtonText: 'OK',
            type: 'warning',
            customClass: 'custom-message-box'
        }
    ).then(() => {
        axios.post('http://localhost:8080/login/Logout', {
            id: localStorage.getItem('id'),
        }).then(response => {
            if (response.data.code !== 200) {
                ElMessage.error('Logout failed. Please try again later.');
                window.location.href = "/";
                return;
            }
            localStorage.removeItem('id');
            localStorage.removeItem('uuid');
            LoggedIn.value = false;
            ElMessage({
                type: 'info',
                message: 'You have been logged out.',
            })
            console.log('Logout successful:', response.data);
            // 处理登出成功后的逻辑，例如清除用户信息、跳转页面等
        })
            .catch(error => {
                ElMessage.error('Logout failed. Please try again later.');
                console.error('Logout failed:', error);
                // 处理登出失败的逻辑，例如显示错误消息等
                window.location.href = "/";
            });
    }).catch(() => {
        ElMessage({
            type: 'info',
            message: 'Logout canceled',
        })
    })
}
async function SendCodeR() {
    if (!standardEmailRegex.test(userEmailR.value)) {
        ElMessage.error('Please enter a valid email address.');
        return;
    }
    await axios.post('http://localhost:8080/register/GetVerificationCodeForRegister', {
        userEmail: userEmailR.value
    }).then(response => {
        if (response.data.code === 200) {
            ElMessage.success("The verification code was successfully sent")
            startCountdownR();
            return;
        }
        else {
            ElMessage.error(response.data.message)
            return;
        }
    })
        .catch(error => {
            ElMessage.error('Fail to send. Please try again later.');
            console.error('Send failed:', error);
            // 处理登录失败的逻辑，例如显示错误消息等
        });
}
async function Register() {
    if (userNameR.value === '' || userPasswordR.value === '' || userPassword2R.value === '' || userEmailR.value === '' || codeR.value === '') {
        ElMessage.error('Please fill in all fields.');
        return;
    }
    else if (userPasswordR.value !== userPassword2R.value) {
        ElMessage.error('Passwords do not match.');
        return;
    }
    else if (!standardEmailRegex.test(userEmailR.value)) {
        ElMessage.error('Please enter a valid email address.');
        return;
    }
    else if(userNameR.value.length > 18 && userNameR.value.length < 1) {
        ElMessage.error("Username must be between 1 and 18 characters long.");
        return;
    }
    else if(userPasswordR.value.length < 6 && userPasswordR.value.length > 18){
        ElMessage.error("Password must be between 6 and 18 characters.");
        return;
    }
    await axios.post('http://localhost:8080/register/Register', {
        userName: userNameR.value,
        userPassword: userPasswordR.value,
        userEmail: userEmailR.value,
        code: codeR.value
    }).then(response => {
        if (response.data.code !== 200) {
            ElMessage.error("Verification code is incorrect or The username or email address is already registered");
            return;
        }
        userNameR.value = '';
        userPasswordR.value = '';
        userPassword2R.value = '';
        userEmailR.value = '';
        codeR.value = '';
        RegisterForm.value = false; // 关闭注册对话框
        ElMessage.info('Registration successful! You can now log in.');
        console.log('Registration successful:', response.data);
        // 处理注册成功后的逻辑，例如存储用户信息、跳转页面等
    })
        .catch(error => {
            ElMessage.error('Registration failed. Please try again later.');
            console.error('Registration failed:', error);
            // 处理注册失败的逻辑，例如显示错误消息等
        });
}
async function ChatWithAi() {
    await axios({
        url: "http://127.0.0.1:8080/ai/nothing",
        method: "Get",
        headers: {
            'id': localStorage.getItem("id"),
            'uuid': localStorage.getItem("uuid")
        }
    }).then(res => {
        if (res.data === "Not logged in") {
            LoginForm.value = true;
            localStorage.removeItem("id");
            localStorage.removeItem("uuid");
        }
        else window.location.href = "ChatWithAi"
    }).catch(e => {
        ElMessage.error(e);
    })
}
async function VoiceToAi() {
    await axios({
        url: "http://127.0.0.1:8080/ai/nothing",
        method: "Get",
        headers: {
            'id': localStorage.getItem("id"),
            'uuid': localStorage.getItem("uuid")
        }
    }).then(res => {
        if (res.data === "Not logged in") {
            LoginForm.value = true;
            localStorage.removeItem("id");
            localStorage.removeItem("uuid");
        }
        else window.location.href = "/VoiceToAi"
    }).catch(e => {
        ElMessage.error(e);
    })
}
async function Friends() {
    await axios({
        url: "http://127.0.0.1:8080/ai/nothing",
        method: "Get",
        headers: {
            'id': localStorage.getItem("id"),
            'uuid': localStorage.getItem("uuid")
        }
    }).then(res => {
        if (res.data === "Not logged in") {
            LoginForm.value = true;
            localStorage.removeItem("id");
            localStorage.removeItem("uuid");
        }
        else {
            localStorage.setItem("featureSelected","chat")
            window.location.href = "/Friends/Session/Nothing"
        }
    }).catch(e => {
        ElMessage.error(e);
    })
}
</script>

<style scoped>
.home-container {
    height: 100vh;
    width: 100%;
    background-repeat: no-repeat;
    background-size: cover;
    position: fixed;
    background: rgb(0, 0, 0);
}

.header h2 {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    color: white;
    font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif
}

.header {
    background-color: rgb(0, 0, 0);
}

.menu {
    display: flex;
    justify-content: center;
}

.menu button {
    padding: 10px 20px;
    font-size: 16px;
    border: none;
    background-color: rgba(0, 0, 0, 0);
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.menu button:hover {
    background-color: rgba(255, 255, 255, 0.2);
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    /* 确保内容分布在两端 */
}

.header-left {
    display: flex;
    align-items: center;
}

.header-center {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    margin: 0;
    color: white;
    font-size: 30px;
}

.header-right {
    display: flex;
    align-items: center;
}

.login-button,
.login-by-email-button {
    margin-right: 20px;
    font-size: 14px;
    border: none;
    background-color: rgba(0, 0, 0, 0);
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
    border-radius: 5px;
}

.register-button,
.logout-button {
    margin-left: 20px;
    font-size: 14px;
    border: none;
    background-color: rgba(0, 0, 0, 0);
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
    border-radius: 5px;
}

.login-button:hover,
.register-button:hover,
.login-by-email-button:hover,
.logout-button:hover {
    color: rgb(150, 150, 150);
}

.login-form {

    .el-button--primary {
        margin-left: 245px;
    }

    .el-button {
        margin-top: 10px;
    }
}

.register-form {
    .el-button--primary {
        margin-left: 226px;
    }

    .el-button {
        margin-top: 10px;
    }
}

.email-form {

    display: flex;

    .el-button--primary {
        margin-left: 245px;
    }

    .el-button {
        margin-top: 10px;
    }
}


/* 自定义弹跳效果 */
:deep(.bounce-enter-active) {
    animation: bounce-in 0.5s;
}

@keyframes bounce-in {
    0% {
        transform: scale(0);
    }

    50% {
        transform: scale(1.1);
    }

    100% {
        transform: scale(1);
    }
}

/* 使用内联SVG背景图片 */
:deep(.el-dialog) {
    background-color: white;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    overflow: hidden;
    color: black;
    font-weight: 800;
    font-size: 24px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 去除所有 el-input 的焦点边框 */
:deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px black !important;
}

:deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px black !important;
    outline: none !important;
}

:deep(.el-input__inner:focus) {
    outline: none !important;
    box-shadow: none !important;
}

.introduce-overlay {
    position: fixed;
    width: 100%;
    height: 100%;
    background: rgb(255, 255, 255);
    z-index: 1000;
    transition: transform 0.6s ease-out, border-radius 0.6s ease-out;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    will-change: transform, border-radius;
    /* 优化性能 */
    /* 隐藏滚动条但保留滚动功能 */
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* IE and Edge */
}

.custom-message-box {
    border-radius: 12px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.custom-message-box .el-message-box__title {
    color: white;
}

.custom-message-box .el-message-box__message {
    color: white;
}

.image-container {
    max-width: 200vh;
    margin-left: 20px;
    flex-direction: row;
    display: flex;
    /* 使用flex布局 */
    justify-content: space-between;
    /* 水平居中 */
    flex-wrap: wrap;
    /* 允许换行 */
    width: 100%;
}

.card {
    padding: 0 40px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-direction: column;
}

:deep(.image-container .el-image) {
    width: 100px;
    height: 100px;
}

:deep(.image-container .el-image__inner) {
    width: 100%;
    height: 100%;
}

/* 广告容器样式 */
.ad-container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
    margin-top: auto;
    /* 将广告推到底部 */
    margin-bottom: 100px;
    /* 添加底部间距 */
}

/* 广告图片样式 */
.ad-image {
    max-width: 80%;
    /* 控制广告图片大小 */
    height: auto;
    border-radius: 50px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 平滑过渡动画 */
.overlay-transition-enter-active,
.overlay-transition-leave-active {
    transition: all 0.5s cubic-bezier(0.33, 1, 0.68, 1);
}

.overlay-transition-enter-from,
.overlay-transition-leave-to {
    transform: translateY(100%);
    border-radius: 40px 40px 0 0;
}

.overlay-transition-enter-to,
.overlay-transition-leave-from {
    transform: translateY(0%);
    border-radius: 0;
}

.image-container {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    width: 100%;
    padding: 30px 20px 20px 20px;
}

.first-button {
    background-color: black;
    color: white;
    border: 0;
}

.first-button:hover {
    background-color: rgb(50, 50, 50);
    border-color: black;
    color: white;
}

.second-button {
    background-color: rgb(200, 200, 200);
    color: black;
    border: 0;
}

.second-button:hover {
    background-color: rgb(160, 160, 160);
    border-color: white;
    color: white;
}

.third-button {
    background-color: black;
    color: white;
    border: 0;
}

.third-button:hover {
    background-color: rgb(220, 220, 220);
    border-color: white;
    color: black;
}
</style>