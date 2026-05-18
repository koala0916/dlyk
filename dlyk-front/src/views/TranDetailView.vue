<template>
  <div class="tran-detail-wrap">
    <h3>交易详情</h3>
    <el-table :data="rows" border stripe>
      <el-table-column prop="label" label="项目" width="200" />
      <el-table-column prop="value" label="内容" min-width="280" show-overflow-tooltip />
    </el-table>
    <div class="actions"><el-button type="primary" @click="goBack">返回</el-button></div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet } from '../http/httpRequest'

const route = useRoute()
const router = useRouter()
const tran = ref({ createByDO: {} })

const rows = computed(() => {
  const t = tran.value
  return [
    { label: '流水号', value: t.tranNo },
    { label: '姓名', value: t.studentName },
    { label: '交易金额', value: t.money },
    { label: '课程类型', value: t.courseType },
    { label: '成交时间', value: t.dealTime },
    { label: '交易备注', value: t.tranRemark },
    { label: '创建人', value: t.createByDO?.name },
    { label: '记录创建时间', value: t.createTime },
  ]
})

onMounted(() => {
  doGet('/api/tran/' + route.params.tranId).then((r) => {
    if (r.data.code === 200) tran.value = r.data.info
  })
})

const goBack = () => {
  const page = route.query.page
  router.push({ path: '/dashboard/tran', ...(page ? { query: { page: String(page) } } : {}) })
}
</script>

<style scoped>
.tran-detail-wrap { padding: 16px; max-width: 960px; }
.actions { margin-top: 16px; }
</style>
