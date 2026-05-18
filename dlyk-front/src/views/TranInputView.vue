<template>
  <el-form ref="formRef" :model="tran" :rules="rules" label-width="220px" class="tran-input-form">
    <el-form-item label="学员姓名" prop="studentName"><el-input v-model="tran.studentName" class="tran-input-form__control" /></el-form-item>
    <el-form-item label="交易金额" prop="money">
      <el-input-number v-model="tran.money" :min="0" :precision="2" class="tran-input-form__control" />
    </el-form-item>
    <el-form-item label="课程类型"><el-input v-model="tran.courseType" class="tran-input-form__control" /></el-form-item>
    <el-form-item label="成交时间" prop="dealTime">
      <el-date-picker v-model="tran.dealTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="tran-input-form__control" />
    </el-form-item>
    <el-form-item label="交易备注"><el-input v-model="tran.tranRemark" type="textarea" :rows="4" class="tran-input-form__control" /></el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(formRef)">提交</el-button>
      <el-button plain @click="goBack">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet, doPost, doPut } from '../http/httpRequest'
import { showMessage } from '../util/message'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const tran = ref({})

const rules = {
  studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  money: [{ required: true, message: '请输入交易金额', trigger: 'blur' }],
  dealTime: [{ required: true, message: '请选择成交时间', trigger: 'change' }],
}

onMounted(() => {
  const id = route.params.tranId
  if (id) {
    doGet('/api/tran/' + id).then((r) => {
      if (r.data.code === 200) tran.value = r.data.info
    })
  }
})

const buildFormData = () => {
  const fd = new FormData()
  const t = tran.value
  if (t.id) fd.append('id', t.id)
  fd.append('studentName', t.studentName)
  fd.append('money', t.money)
  if (t.courseType) fd.append('courseType', t.courseType)
  if (t.dealTime) fd.append('dealTime', t.dealTime)
  if (t.tranRemark) fd.append('tranRemark', t.tranRemark)
  return fd
}

const submit = (el) => {
  el.validate((ok) => {
    if (!ok) return
    const page = route.query.page
    const back = { path: '/dashboard/tran', ...(page ? { query: { page: String(page) } } : {}) }
    const req = tran.value.id ? doPut('/api/tran', buildFormData()) : doPost('/api/tran/add', buildFormData())
    req.then((r) => {
      if (r.data.code === 200) {
        showMessage(r.data.msg || '保存成功', 'success')
        router.push(back)
      } else showMessage(r.data.msg || '保存失败', 'error')
    })
  })
}

const goBack = () => {
  const page = route.query.page
  router.push({ path: '/dashboard/tran', ...(page ? { query: { page: String(page) } } : {}) })
}
</script>

<style scoped>
.tran-input-form { max-width: 560px; }
.tran-input-form__control { width: 100%; max-width: 320px; }
</style>
