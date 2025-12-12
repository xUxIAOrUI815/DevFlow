<template>
  <div style="padding: 20px;">
    <!-- 顶部导航 -->
    <el-page-header @back="goBack" title="返回项目列表">
      <template #content>
        <span class="text-large font-600 mr-3"> 项目详情 (ID: {{ projectId }}) </span>
      </template>
      <template #extra>
        <!-- 打开弹窗的按钮 -->
        <el-button type="primary" @click="openCreateDialog(null)">+ 新建根任务</el-button>
      </template>
    </el-page-header>

    <el-divider />

    <!-- 树形表格 -->
    <el-card shadow="never">
<!--      <template #header>-->
<!--        <div class="card-header">-->
<!--          <span>任务结构树</span>-->
<!--        </div>-->
<!--      </template>-->

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

        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="openCreateDialog(scope.row)">
              添加子任务
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增任务弹窗 -->
    <el-dialog title="新增任务" v-model:visible="dialogVisible" width="500px">
      <el-form model="form" label-width="80px">

        <el-form-item label="父任务" v-if="form-parentId">
          <el-tag type="info">Id: {{form.parentId}} </el-tag>
        </el-form-item>
        <el-form-item label="标题">
          <el-radio-group v-model:aria-label="form.type">
            <el-radio label="1">需求</el-radio>
            <el-radio label="2">Bug</el-radio>
            <el-radio label="3">任务</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="优先级">
          <el-select v-model="form.priority" placeholder="请选择">
            <el-option label="P0-紧急" value="2" />
            <el-option label="P1-普通" value="1" />
            <el-option label="P2-低" value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="指派">
          <el-select v-model="form.assigneeId" placeholder="选择负责人">
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.realName"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible=false">取消</el-button>
          <el-button type="primary" @click="sumbitTask">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {addIssue, getIssueTree} from '@/api/issue'
import { getUserList} from "@/api/user"
import { ElMessage } from "element-plus";


// 必须在 setup 顶层调用
const route = useRoute()
const router = useRouter()
const projectId = route.params.id

const loading = ref(false)
const treeData = ref([])
const userList = ref([])

// 弹窗控制
const dialogVisible = ref(false)
const form = reactive({
  projectId: parseInt(projectId), // 关联当前项目
  title: '',
  type: 1,
  priority: 1,
  parentId: null,  // 如果为空则是根任务，不为空则是子任务
  assigneeId: null
})

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

// 加载用户列表
const loadUsers = async () => {
  try {
    const res = await getUserList()
    if (res) {
      userList.value = res
    }
  } catch (err) {
    console.error(err)
  }
}

// 打开弹窗
const openCreateDialog = (parseMode) => {
  form.title=''
  form.type=1
  form.priority=1
  form.assigneeId=null

  if (parseMode) {
    // 如果有父任务，说明是创建子任务
    form.parentId = parseMode.id
  } else {
    // 否则是创建根任务
    form.parentId = null
  }
  dialogVisible.value = true
}

// 提交表单
const submitTask =  async () => {
  if(!form.title) return ElMessage.error('标题为必填项')

  try{
    await addIssue(form)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    loadData()  // 刷新列表
  }catch(err){
    console.error(err)
  }
}

onMounted(() => {
  loadData()
  loadUsers()
})
</script>
