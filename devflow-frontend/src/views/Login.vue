<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">DevFlow {{ isRegister ? '用户注册' : '用户登录' }}</h2>

      <el-form :model="form" label-width="0">
        <!-- 用户名 -->
        <el-form-item>
          <el-input v-model="form.username" placeholder="请输入用户名" size="large">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            show-password
          >
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>

        <!-- 注册特有字段：确认密码 & 真实姓名 -->
        <div v-if="isRegister">
          <el-form-item>
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              size="large"
              show-password
            >
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-input v-model="form.realName" placeholder="真实姓名 (用于显示)" size="large">
              <template #prefix><el-icon><Postcard /></el-icon></template>
            </el-input>
          </el-form-item>
        </div>

        <!-- 按钮 -->
        <el-button type="primary" class="login-btn" size="large" @click="handleSubmit" :loading="loading">
          {{ isRegister ? '立即注册' : '登 录' }}
        </el-button>

        <!-- 切换链接 -->
        <div class="toggle-box">
          <el-link type="primary" @click="toggleMode">
            {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { User, Lock, Postcard } from '@element-plus/icons-vue' // 引入图标

const router = useRouter()
const loading = ref(false)
const isRegister = ref(false) // 控制当前模式

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  realName: ''
})

// 切换模式
const toggleMode = () => {
  isRegister.value = !isRegister.value
  // 清空表单
  form.username = ''
  form.password = ''
  form.confirmPassword = ''
  form.realName = ''
}

// 提交处理
const handleSubmit = () => {
  if (isRegister.value) {
    handleRegister()
  } else {
    handleLogin()
  }
}

// 登录逻辑
const handleLogin = async () => {
  if(!form.username || !form.password) return ElMessage.warning("请输入账号密码")

  loading.value = true
  try {
    const res = await request.post('/auth/login', {
      username: form.username,
      password: form.password
    })

    ElMessage.success("登录成功")
    localStorage.setItem('token', res.token)
    localStorage.setItem('userInfo', JSON.stringify(res.user))
    router.push('/')

  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 注册逻辑
const handleRegister = async () => {
  if(!form.username || !form.password) return ElMessage.warning("请填写完整")
  if(form.password !== form.confirmPassword) return ElMessage.error("两次密码输入不一致")

  loading.value = true
  try {
    const res = await request.post('/auth/register', {
      username: form.username,
      password: form.password,
      realName: form.realName
    })

    // 后端如果返回 code=200 会进入这里 (request.js 拦截器处理过)
    ElMessage.success(res || "注册成功，请登录")

    // 注册成功后，自动切回登录模式，并填好用户名
    isRegister.value = false
    form.password = ''
    form.confirmPassword = ''

  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.login-card {
  width: 400px;
  padding: 30px 20px;
}
.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-weight: 600;
}
.login-btn {
  width: 100%;
  margin-top: 10px;
}
.toggle-box {
  margin-top: 15px;
  text-align: right;
}
</style>
