<template>
  <div style="padding: 20px;">
    <h2> DevFlow </h2>

    <!-- 卡片容器 hover 表示鼠标悬停时才显示阴影，提升视觉交互 -->
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span> 我的项目 </span>
          <el-button type="primary"> 新建项目 </el-button>
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
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {getProjectList} from '@/api/project.js'

// 实例化
const router = useRouter()
const tableData = ref([])

// 3. 点击处理函数
const handleRowClick = (row) => {
  // 4. 加一行日志，按 F12 看控制台有没有输出
  console.log("正在尝试跳转，项目ID:", row.id)

  if (!row.id) {
    console.error("错误：当前行没有 ID，无法跳转")
    return
  }
  // 5. 执行跳转
  router.push(`/project/${row.id}`)
}

// const handleRowClick = (row) => {
//   // 1. 先打印，证明函数进来了
//   console.log("=============== 点击触发 ===============")
//   console.log("当前行数据:", row)
//
//   // 2. 强行判断 ID
//   if (!row || !row.id) {
//     alert("错误：该行数据没有 ID，无法跳转！")
//     return
//   }
//
//   // 3. 执行跳转
//   console.log("准备跳转到:", `/project/${row.id}`)
//   router.push(`/project/${row.id}`)
//
//   // 4. 如果控制台打印了上面那句，但页面没动，那就是路由配置(router/index.js)有问题
// }

// 页面加载时，去后端拿数据
onMounted(async () => {
  try {
    const res = await getProjectList()
    tableData.value = res
  }catch (err){
    console.error('获取项目列表失败:', err)
  }
})
</script>

<style scoped>
.clickable-row {
  cursor: pointer;
}
</style>
