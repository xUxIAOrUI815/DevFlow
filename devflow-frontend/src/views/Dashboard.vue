<template>
  <div style="padding: 20px;">
    <h2> DevFlow 研发协作平台 </h2>

    <!-- 卡片容器 hover 表示鼠标悬停时才显示阴影，提升视觉交互 -->
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span> 我的项目 </span>
          <el-button type="primary" @click="dialogVisible=true"> 新建项目 </el-button>
        </div>
      </template>

      <!-- 项目列表
           @row-click="handleRowClick": 点击某一行触发跳转
      -->
      <el-table
        :data="tableData"
        style="width: 100%"
        stripe
        border
        @row-click="handleRowClick"
        :row-style="{ cursor: 'pointer' }"
      >
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="name" label="项目名称" width="180"/>
        <el-table-column prop="code" label="项目代号" width="100"/>
        <el-table-column prop="ownerName" label="负责人" width="120"/>
        <!-- 展示后端计算出来的统计数据 -->
        <el-table-column label="任务进度" align="center">
          <template #default="scope">
            <el-tag effect="plain">总任务数: {{ scope.row.taskCount }}</el-tag>
            <span style="margin: 0 10px">/</span>
            <el-tag type="success" effect="plain"> 已完成: {{ scope.row.doneCount }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createdAt" label="创建时间"/>
      </el-table>
    </el-card>

    <!-- 新建项目对话框 -->
    <el-dialog v-model="dialogVisible" title="创建新项目" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="项目名称">
          <el-input v-model="form.name" placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item label="项目代号">
          <el-input v-model="form.code" placeholder="请输入项目代号"/>
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input v-model="form.desc" placeholder="请输入项目描述"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProject">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {addProject, getProjectList} from '@/api/project.js'
import {ElMessage} from "element-plus";

// 实例化
const router = useRouter()
const tableData = ref([])
const dialogVisible = ref(false)

// 表单数据
const form = reactive({
  name: '',
  code: '',
  desc: '',
  ownerId : 1
})

// 3. 点击处理函数
const handleRowClick = (row) => {
  // 5. 执行跳转
  router.push(`/project/${row.id}`)
}

const loadData = async () => {
  try {
    const res = await getProjectList()
    tableData.value = res
  }catch (err){
    console.error('获取项目列表失败:', err)
  }
}

// 提交创建
const submitProject = async () => {
  if(!form.name || !form.code) return ElMessage.warning("请填写必填项名称和代号")
  try {
    console.log("正在提交数据: ",form)  // F12

    await addProject(form)
    ElMessage.success("创建成功")
    dialogVisible.value = false

    // 清空表单
    form.name = ''
    form.code = ''
    form.desc = ''
    loadData()  // 刷新列表
  }catch(err) {console.error(err)}
}

// 页面加载时，去后端拿数据
onMounted(async () => {
  loadData()
})
</script>

<style scoped>
.clickable-row {
  cursor: pointer;
}
</style>
