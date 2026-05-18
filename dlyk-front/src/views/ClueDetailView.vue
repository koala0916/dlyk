<template>
  <div class="clue-detail-wrap">
    <h3>线索详情</h3>
    <el-table :data="detailRows" border stripe>
      <el-table-column prop="label" label="项目" width="200" />
      <el-table-column prop="value" label="内容" min-width="280" show-overflow-tooltip />
    </el-table>
    <h3>编辑记录</h3>
    <el-table :data="editLogs" border stripe empty-text="暂无编辑记录">
      <el-table-column type="index" label="序号" width="65" />
      <el-table-column prop="editTime" label="编辑时间" width="180" />
      <el-table-column prop="editByDO.name" label="编辑人" width="120" />
      <el-table-column prop="changeContent" label="修改内容" min-width="320" show-overflow-tooltip />
    </el-table>
    <div class="actions">
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet } from '../http/httpRequest'
import { showMessage } from '../util/message'

const route = useRoute()
const router = useRouter()
const clue = ref({ createByDO: {} })
const editLogs = ref([])

const detailRows = computed(() => {
  const c = clue.value
  return [
    { label: 'ID', value: c.id },
    { label: '姓名', value: c.name },
    { label: '电话', value: c.phone },
    { label: '年龄', value: c.age },
    { label: '意向课程', value: c.intentionCourse },
    { label: '意向强度', value: c.intentionStrength },
    { label: '来源', value: c.source },
    { label: '体验课时间', value: c.trialClassTime },
    { label: '线索状态', value: c.clueStatus },
    { label: '备注', value: c.remark },
    { label: '创建人', value: c.createByDO?.name },
    { label: '创建时间', value: c.createTime },
  ]
})

onMounted(() => {
  const id = route.params.clueId
  doGet('/api/clue/' + id).then((resp) => {
    if (resp.data.code === 200) clue.value = resp.data.info
    else showMessage('加载失败', 'error')
  })
  doGet('/api/clue/' + id + '/edit-logs').then((resp) => {
    if (resp.data.code === 200) editLogs.value = resp.data.info || []
  })
})

const goBack = () => {
  const page = route.query.page
  router.push({ path: '/dashboard/clue', ...(page ? { query: { page: String(page) } } : {}) })
}
</script>

<style scoped>
.clue-detail-wrap { padding: 16px; max-width: 960px; }
h3 { margin: 16px 0 8px; font-size: 16px; }
.actions { margin-top: 16px; }
</style>
