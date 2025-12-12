<template>
  <div style="padding: 20px;">
    <!-- 顶部导航 -->
    <el-page-header @back="goBack" title="返回项目列表">
      <template #content>
        <span class="text-large font-600 mr-3"> 项目详情 (ID: {{ projectId }}) </span>
      </template>
    </el-page-header>

    <el-divider />

    <!-- 树形表格 -->
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>任务结构树</span>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="treeData"
        style="width: 100%; margin-top: 20px"
        row-key="id"
        border
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="title" label="任务标题" min-width="300" />

        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 1">需求</el-tag>
            <el-tag v-else-if="scope.row.type === 2" type="danger">Bug</el-tag>
            <el-tag v-else type="info">任务</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 2" type="success">已完成</el-tag>
            <el-tag v-else type="warning">进行中</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getIssueTree } from '@/api/issue'

// 必须在 setup 顶层调用
const route = useRoute()
const router = useRouter()
const projectId = route.params.id

const loading = ref(false)
const treeData = ref([])

const goBack = () => {
  router.push('/')
}

const loadData = async () => {
  loading.value = true
  try {
    // 临时测试：写死查 ID=3 的树
    const rootIssueId = 3
    const res = await getIssueTree(rootIssueId)
    if (res) {
      treeData.value = [res]
    }
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
