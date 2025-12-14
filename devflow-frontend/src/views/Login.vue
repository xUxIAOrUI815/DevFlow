<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">DevFlow 登录</h2>
      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名 (admin)" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码 (123456)" size="large" show-password />
        </el-form-item>
        <el-button type="primary" class="login-btn" size="large" @click="handleLogin" :loading="loading">
          登 录
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>


<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const handleLogin = async () => {
  if(!form.username || !form.password) return ElMessage.warning("请输入账号密码")

  loading.value = true
  try {
    // 发送登录请求
    const res = await request.post('/auth/login', form)

    // 登录成功提示
    ElMessage.success("登录成功")

    // 存储 Token 和用户信息
    // 注意：这里要确保后端返回的数据结构里有 token 和 user 字段
    if (res && res.token) {
      localStorage.setItem('token', res.token)
      localStorage.setItem('userInfo', JSON.stringify(res.user))

      // 跳转到首页
      router.push('/')
    } else {
      ElMessage.error("登录失败：返回数据格式不正确")
    }

  } catch (err) {
    console.error(err)
    // 错误已经在 request.js 拦截器里弹出了，这里可以不处理，或者再次打印
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
  padding: 20px;
}
.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.login-btn {
  width: 100%;
}
</style>
