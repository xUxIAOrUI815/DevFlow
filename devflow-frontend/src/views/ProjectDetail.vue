<template>
  <div style="padding: 20px;">
    <!-- 1. 顶部导航与操作栏 -->
    <el-page-header @back="goBack" title="返回项目列表">
      <template #content>
        <span class="text-large font-600 mr-3"> 项目详情 (ID: {{ projectId }}) </span>
      </template>
      <template #extra>
        <el-button @click="openMemberDialog"> 成员管理 </el-button>
        <!-- 视图切换 -->
        <el-radio-group v-model="viewMode" size="small" style="margin-right: 20px;">
          <el-radio-button label="tree">树形表格</el-radio-button>
          <el-radio-button label="kanban">敏捷看板</el-radio-button>
        </el-radio-group>
        <!-- 新建任务按钮 -->
        <el-button type="primary" @click="openCreateDialog(null)">+ 新建任务</el-button>
      </template>
    </el-page-header>
    <el-divider />

    <!-- 2. 视图 A: 树形表格 -->
    <el-card shadow="never" v-if="viewMode === 'tree'">
      <el-table
        v-loading="loading"
        :data="treeData"
        style="width: 100%;"
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
        <el-table-column prop="priority" label="优先级" width="100" align="center">
          <template #default="scope">
            <span v-if="scope.row.priority === 2" style="color:red; font-weight:bold;">P0</span>
            <span v-else-if="scope.row.priority === 1" style="color:orange;">P1</span>
            <span v-else>P2</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 2" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="warning">进行中</el-tag>
            <el-tag v-else type="info">待办</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="openCreateDialog(scope.row)">添加子任务</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 3. 视图 B: 敏捷看板 -->
    <div v-else class="kanban-container">
      <!-- 列 1: 待办 -->
      <div class="kanban-column">
        <div class="column-header todo-header">待办 ({{ todoList.length }})</div>
        <draggable
          class="drag-area"
          :list="todoList"
          group="tasks"
          item-key="id"
          @change="(e) => onDragChange(e, 0)"
        >
          <template #item="{ element }">
            <div class="kanban-card">
              <div class="card-title">{{ element.title }}</div>
              <div class="card-footer">
                <el-tag size="small" type="info">#{{ element.id }}</el-tag>
                <el-tag size="small" v-if="element.type===2" type="danger" effect="plain">Bug</el-tag>
                <el-tag size="small" v-else type="primary" effect="plain">Feature</el-tag>
              </div>
            </div>
          </template>
        </draggable>
      </div>

      <!-- 列 2: 进行中 -->
      <div class="kanban-column">
        <div class="column-header progress-header">进行中 ({{ progressList.length }})</div>
        <draggable
          class="drag-area"
          :list="progressList"
          group="tasks"
          item-key="id"
          @change="(e) => onDragChange(e, 1)"
        >
          <template #item="{ element }">
            <div class="kanban-card">
              <div class="card-title">{{ element.title }}</div>
              <div class="card-footer">
                <el-tag size="small" type="info">#{{ element.id }}</el-tag>
                <el-avatar :size="20" style="background-color: #409eff">User</el-avatar>
              </div>
            </div>
          </template>
        </draggable>
      </div>

      <!-- 列 3: 已完成 -->
      <div class="kanban-column">
        <div class="column-header done-header">已完成 ({{ doneList.length }})</div>
        <draggable
          class="drag-area"
          :list="doneList"
          group="tasks"
          item-key="id"
          @change="(e) => onDragChange(e, 2)"
        >
          <template #item="{ element }">
            <div class="kanban-card done-card">
              <div class="card-title">{{ element.title }}</div>
              <div class="card-footer">
                <el-tag size="small" type="success">Done</el-tag>
              </div>
            </div>
          </template>
        </draggable>
      </div>
    </div>

    <!-- 4. 新建任务弹窗 -->
    <el-dialog v-model="dialogVisible" title="新建任务" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="父任务" v-if="form.parentId">
          <el-tag type="info">Parent ID: {{ form.parentId }}</el-tag>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入任务标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">需求</el-radio>
            <el-radio :label="2">缺陷</el-radio>
            <el-radio :label="3">任务</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="form.priority">
            <el-option label="P0 - 紧急" :value="2" />
            <el-option label="P1 - 普通" :value="1" />
            <el-option label="P2 - 低" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="待办" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="指派给">
          <el-select v-model="form.assigneeId" placeholder="选择负责人">
            <!-- 这里会循环渲染所有用户 -->
            <el-option
              v-for="u in userList"
              :key="u.id"
              :label="u.realName"
              :value="u.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitTask">确定创建</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 成员管理弹窗 -->
    <el-dialog v-model="memberDialogVisible" title="成员管理" width="600px">
      <!-- 添加区域 -->
      <div style = "display: flex; gap: 10px; margin-bottom: 20px; background: #f5f7fa; padding: 15px; border-radius: 4px;">
        <el-select v-model="newMemberForm.userId" placeholder="选择用户" style="width: 200px;">
          <el-option v-for="u in userList" :key="u.id" :label="u.realName" :value="u.id" />
        </el-select>
        <el-select v-model="newMemberForm.role" placeholder="选择角色" style="width: 120px;">
          <el-option label="普通成员" :value="1" />
          <el-option label="管理员" :value="2" />
        </el-select>
        <el-button type="primary" @click="submitAddMember">添加进入项目</el-button>
      </div>
    </el-dialog>
  </div>

  <!-- 列表区域 -->
  <el-table :data="memberList" border stripe>
    <el-table-column label="姓名" prop="realName"/>
    <el-table-column lable="角色" align="center">
      <template #default="scope">
        <el-tag v-if="scope.row.role === 2" type="danger"> 管理员 </el-tag>
        <el-tag v-else> 普通成员 </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="加入时间" prop="joinedAt" width="180"/>
    <el-table-column ;abel="操作" align="center" width="100">
      <template #default="scope">
        <el-button type="danger" link size="small" @click="handleRemoveMember(scope.row.id)">移除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
// 1. 引入所有必要的 Vue 工具
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import draggable from 'vuedraggable'
import { ElMessage } from 'element-plus'
import { getProjectMembers, addMember, removeMember} from "@/api/user";

// 2. 引入所有 API (包括用户接口)
import { getIssueTree, addIssue, getIssueList, updateIssue } from '@/api/issue'
import { getUserList } from '@/api/user'

const route = useRoute()
const router = useRouter()
const projectId = route.params.id

// 状态控制
const viewMode = ref('tree') // tree 或 kanban
const loading = ref(false)
const dialogVisible = ref(false)
const memberDialogVisible = ref(false)
const memberList = ref([])
const newMemberForm = reactive({userId: null, role: 1})

// 数据源
const treeData = ref([])  // 树形数据
const flatList = ref([])  // 扁平数据 (看板用)
const userList = ref([])  // 用户列表 (下拉框用)

// 表单数据
const form = reactive({
  projectId: parseInt(projectId),
  title: '',
  type: 1,
  priority: 1,
  status: 0,
  parentId: null,
  assigneeId: null
})

// === 计算属性：看板分组 ===
const todoList = computed(() => flatList.value.filter(i => i.status === 0))
const progressList = computed(() => flatList.value.filter(i => i.status === 1))
const doneList = computed(() => flatList.value.filter(i => i.status === 2))

// === 方法定义 ===

const goBack = () => router.push('/')

// 加载任务数据
const loadData = async () => {
  loading.value = true
  try {
    if (viewMode.value === 'tree') {
      // 树形模式：查 ID=3 的根任务 (测试用)
      const res = await getIssueTree(projectId)
      treeData.value = res || []
    } else {
      // 看板模式：查全量
      const res = await getIssueList(projectId)
      flatList.value = res || []
    }
  } catch (err) { console.error(err) }
  finally { loading.value = false }
}

// 加载用户列表
const loadUsers = async () => {
  try {
    const res = await getUserList()
    userList.value = res || []
  } catch (err) { console.error("加载用户失败", err) }
}

// 打开弹窗
const openCreateDialog = (parentNode) => {
  // 1. 重置表单
  form.title = ''
  form.type = 1
  form.priority = 1
  form.status = 0
  form.assigneeId = null

  // 2. 设置父子关系
  if (parentNode) {
    form.parentId = parentNode.id
  } else {
    // 如果是新建根任务，为了测试树形结构，建议这里还是临时填 3
    // 否则你可以填 null，但在树里可能看不见（因为我们只查了 ID=3 的树）
    form.parentId = null
  }

  // 3. 显示弹窗
  dialogVisible.value = true
}

// 提交任务
const submitTask = async () => {
  if(!form.title) return ElMessage.warning("标题必填")
  try {
    await addIssue(form)
    ElMessage.success("创建成功")
    dialogVisible.value = false
    loadData() // 刷新列表
  } catch (err) { console.error(err) }
}

// 看板拖拽变更
const onDragChange = (evt, targetStatus) => {
  if (evt.added) {
    const item = evt.added.element
    updateIssue({ id: item.id, status: targetStatus }).then(() => {
      ElMessage.success("状态更新成功")
      item.status = targetStatus // 更新本地状态
    })
  }
}

// 打开成员管理弹窗
const openMemberDialog = async () => {
  memberDialogVisible.value = true
  loadMembers()
}

// 加载成员列表
const loadMembers = async () => {
  try {
    const res = await getProjectMembers(projectId)
    memberList.value = res
  }catch(err){ console.error(err)}
}

// 添加成员
const submitAddMember = async () => {
  if(!newMemberForm.userId) return ElMessage.warning("请选择用户")
  try {
    await addMember({
      projectId: projectId,
      userId: newMemberForm.userId,
      role: newMemberForm.role
    })
    ElMessage.success("添加成功")
    loadMembers()
  }catch (err){ console.error(err)}
}

// 移除成员
const removeMember = async (id) => {
  try {
    await removeMember(id)
    ElMessage.success("已移除")
    loadMembers()
  }catch (err) { console.error(err)}
}

// 监听视图切换
watch(viewMode, () => {
  loadData()
})

// 初始化
onMounted(() => {
  loadData()
  loadUsers() // 别忘了加载用户！
})
</script>

<style scoped>
/* 看板容器 */
.kanban-container {
  display: flex;
  gap: 20px;
  height: calc(100vh - 200px);
  overflow-x: auto;
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
}
.kanban-column {
  flex: 1;
  min-width: 280px;
  background-color: #ebecf0;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  padding: 10px;
}
.drag-area {
  flex: 1;
  min-height: 100px;
}
.column-header {
  font-weight: bold;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
  color: white;
  text-align: center;
}
.todo-header { background-color: #909399; }
.progress-header { background-color: #e6a23c; }
.done-header { background-color: #67c23a; }

.kanban-card {
  background: white;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(0,0,0,0.1);
  cursor: grab;
}
.card-title {
  font-weight: 500;
  margin-bottom: 8px;
  color: #303133;
}
.card-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}
</style>
