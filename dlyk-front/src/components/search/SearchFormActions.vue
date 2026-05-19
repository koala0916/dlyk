<template>
  <el-form-item class="search-actions">
    <!-- type=submit：在表单内按回车也会触发搜索 -->
    <el-button type="primary" native-type="submit">
      <el-icon><Search /></el-icon> 搜索
    </el-button>
    <!-- type=button：避免点重置时误提交表单 -->
    <el-button native-type="button" @click="$emit('reset')">
      <el-icon><Refresh /></el-icon> 重置
    </el-button>
  </el-form-item>
</template>

<script setup>
import { Search, Refresh } from '@element-plus/icons-vue'
defineEmits(['search', 'reset'])
</script>

<style scoped>
.search-actions :deep(.el-form-item__content) { display: flex; gap: 8px; }
</style>
