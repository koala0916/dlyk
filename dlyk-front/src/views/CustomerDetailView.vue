<template>
  <div class="customer-detail-wrap">
    <h3 class="customer-detail-title">学员详情</h3>
    <el-table :data="detailTableRows" border stripe class="customer-detail-table" empty-text="暂无数据">
      <el-table-column prop="label" label="项目" width="200" align="left" />
      <el-table-column label="内容" min-width="280">
        <template #default="{ row }">
          <el-link v-if="row.linkType === 'clue' && customerDetail.clueId" type="primary" @click="goClue(customerDetail.clueId)">
            查看线索 #{{ customerDetail.clueId }}
          </el-link>
          <template v-else-if="row.linkType === 'tran'">
            <el-link v-if="customerDetail.id" type="primary" @click="goTranList(customerDetail.id)">
              查看该客户全部交易（{{ tranCount }} 笔）
            </el-link>
            <div v-if="tranCount > 0" class="tran-mini-list">
              <el-link v-for="t in customerDetail.tranList" :key="t.id" type="info" @click="goTranDetail(t.id)">
                {{ t.tranNo || ('交易#' + t.id) }}
              </el-link>
            </div>
          </template>
          <span v-else>{{ row.value }}</span>
        </template>
      </el-table-column>
    </el-table>

    <h3 class="customer-detail-title">编辑记录</h3>
    <el-table :data="editLogList" border stripe empty-text="暂无编辑记录">
      <el-table-column type="index" label="序号" width="65" />
      <el-table-column prop="editTime" label="编辑时间" width="180" />
      <el-table-column prop="editByDO.name" label="编辑人" width="120" />
      <el-table-column prop="changeContent" label="修改内容" min-width="320" show-overflow-tooltip />
    </el-table>

    <div class="customer-detail-actions">
      <el-button type="primary" @click="goBack">返 回</el-button>
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
const customerDetail = ref({ createByDO: {}, tranList: [] })
const editLogList = ref([])

const studyingText = (v) => {
  if (v === 1) return '是'
  if (v === 0) return '否'
  return ''
}

const tranCount = computed(() => (customerDetail.value.tranList || []).length)

const detailTableRows = computed(() => {
  const c = customerDetail.value
  return [
    { label: 'ID', value: c.id },
    { label: '姓名', value: c.name },
    { label: '电话', value: c.phone },
    { label: '年龄', value: c.age },
    { label: '课程类型', value: c.courseType },
    { label: '剩余课时', value: c.remainingLessons },
    { label: '课程到期时间', value: c.courseExpireTime },
    { label: '来源', value: c.source },
    { label: '是否正在学习', value: studyingText(c.studying) },
    { label: '备注', value: c.remark },
    { label: '来源线索', linkType: 'clue', value: c.clueId ? '查看' : '无' },
    { label: '相关交易', linkType: 'tran', value: '' },
    { label: '创建时间', value: c.createTime },
    { label: '创建人', value: c.createByDO?.name ?? '' },
  ]
})

onMounted(() => {
  loadCustomerDetail()
  loadEditLogs()
})

const loadCustomerDetail = () => {
  const id = route.params.customerId
  doGet('/api/customer/' + id, {}).then((resp) => {
    if (resp && resp.data.code === 200) {
      customerDetail.value = resp.data.info || {}
      if (!customerDetail.value.createByDO) customerDetail.value.createByDO = {}
      if (!customerDetail.value.tranList) customerDetail.value.tranList = []
    } else {
      showMessage(resp.data.msg || '数据加载失败', 'error')
    }
  })
}

const loadEditLogs = () => {
  const id = route.params.customerId
  doGet('/api/customer/' + id + '/edit-logs', {}).then((resp) => {
    if (resp && resp.data.code === 200) editLogList.value = resp.data.info || []
  })
}

const goClue = (clueId) => {
  router.push({ path: '/dashboard/clue/' + clueId, query: { page: route.query.page || '1' } })
}

const goTranList = (customerId) => {
  router.push({ path: '/dashboard/tran', query: { customerId: String(customerId), page: '1' } })
}

const goTranDetail = (tranId) => {
  router.push({ path: '/dashboard/tran/' + tranId, query: { page: '1' } })
}

const goBack = () => {
  const page = route.query.page
  router.push({
    path: '/dashboard/customer',
    ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
  })
}
</script>

<style scoped>
.customer-detail-wrap { padding: 16px; max-width: 960px; }
.customer-detail-title { margin: 16px 0 8px; font-size: 16px; font-weight: 600; }
.customer-detail-title:first-child { margin-top: 0; }
.customer-detail-table { width: 100%; border-radius: 8px; overflow: hidden; }
.customer-detail-actions { margin-top: 16px; }
.tran-mini-list { margin-top: 8px; display: flex; flex-wrap: wrap; gap: 8px; }
</style>
