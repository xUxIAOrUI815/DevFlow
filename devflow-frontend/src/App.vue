<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <h1>DevFlow 研发协作平台</h1>
      </el-header>

      <el-main>
        <div class="project-list" v-if="projectList.length > 0">
          <el-card v-for="item in projectList" :key="item.id" class="project-card">
            <template #header>
              <div class="card-header">
                <span class="project-name">{{ item.name }}</span>
                <el-tag type="success">{{ item.code }}</el-tag>
              </div>
            </template>

            <p class="desc">{{ item.description || '暂无描述' }}</p>

            <div class="stat-info">
              <p>负责人: <strong>{{ item.ownerName }}</strong></p>
              <p>任务进度: {{ item.doneCount }} / {{ item.taskCount }}</p>
            </div>

            <!-- 进度条 -->
            <el-progress
              :percentage="calculateProgress(item.doneCount, item.taskCount)"
              :status="calculateProgress(item.doneCount, item.taskCount) === 100 ? 'success' : ''"
            />

            <div class="footer">
              <el-button type="primary" plain>进入项目</el-button>
            </div>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from "vue"
import axios from 'axios'

interface ProjectVO{
  id:number
  name:string
  code:string
  description:string
  ownerName:string
  taskCount:number
  doneCount:number
}

const projectList = ref<ProjectVO[]>([])

const calculateProgress = (done: number, total: number) => {
  if(!total || total === 0) return 0
  return Math.round(done / total * 100)
}

onMounted(async () =>{
  try{
    // const res = await axios.get('http://localhost:8080/project/list')
    // 使用 vite 的代理转发
    const res = await axios.get('/api/project/list')
    if (res.data.code === 200){
      projectList.value = res.data.data
      console.log('Success', projectList.value)
    }else{
      console.log('Error', res.data.message)
    }
  }
  catch (e){
    console.log('Error', e)
  }
});
</script>


<style scoped>
.header {
  background-color: #001529;
  color: white;
  line-height: 60px;
  display: flex;
  align-items: center;
  padding-left: 20px;
}
.project-list {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  padding: 20px;
}
.project-card {
  width: 300px;
  transition: all 0.3s;
}
.project-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.project-name {
  font-weight: bold;
  font-size: 16px;
}
.desc {
  color: #666;
  font-size: 14px;
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.stat-info {
  margin: 15px 0;
  font-size: 14px;
}
.footer {
  margin-top: 20px;
  text-align: right;
}
</style>
