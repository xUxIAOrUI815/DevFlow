<template>
  <!-- Vue Router 会把 Dashboard 或 ProjectDetail 渲染在这里 -->
  <router-view />
</template>

<script setup>
// 这里什么都不用引，交给 router 去处理
</script>

<style>
/* 全局样式，去掉默认的 margin */
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
</style>
