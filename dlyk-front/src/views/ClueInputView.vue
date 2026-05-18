<template>
  <el-form ref="formRef" :model="clue" :rules="rules" label-width="220px" class="clue-input-form">
    <el-form-item label="姓名" prop="name"><el-input v-model="clue.name" class="clue-input-form__control" /></el-form-item>
    <el-form-item label="电话" prop="phone"><el-input v-model="clue.phone" class="clue-input-form__control" /></el-form-item>
    <el-form-item label="年龄"><el-input-number v-model="clue.age" :min="1" :max="120" class="clue-input-form__control" /></el-form-item>
    <el-form-item label="意向课程"><el-input v-model="clue.intentionCourse" class="clue-input-form__control" /></el-form-item>
    <el-form-item label="意向强度">
      <el-slider v-model="clue.intentionStrength" :min="1" :max="10" show-stops class="clue-input-form__control" />
    </el-form-item>
    <el-form-item label="来源"><el-input v-model="clue.source" class="clue-input-form__control" /></el-form-item>
    <el-form-item label="体验课时间">
      <el-date-picker v-model="clue.trialClassTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="clue-input-form__control" />
    </el-form-item>
    <el-form-item label="线索状态" prop="clueStatus">
      <el-select v-model="clue.clueStatus" class="clue-input-form__control">
        <el-option label="未联系" value="未联系" />
        <el-option label="已联系" value="已联系" />
        <el-option label="已转客户" value="已转客户" disabled />
      </el-select>
    </el-form-item>
    <el-form-item label="备注"><el-input v-model="clue.remark" type="textarea" :rows="5" class="clue-input-form__control" /></el-form-item>
    <el-form-item v-if="clue.id" label="创建人"><el-input :model-value="createByName" disabled class="clue-input-form__control" /></el-form-item>
    <el-form-item v-if="clue.id" label="创建时间"><el-input v-model="clue.createTime" disabled class="clue-input-form__control" /></el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(formRef)">提交</el-button>
      <el-button plain @click="goBack">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { doGet, doPost, doPut } from '../http/httpRequest'
import { showMessage } from '../util/message'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const clue = ref({ clueStatus: '未联系', intentionStrength: 5 })

const createByName = computed(() => clue.value.createByDO?.name ?? '')

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { min: 11, max: 11, message: '手机号为11位', trigger: 'blur' },
  ],
  clueStatus: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

onMounted(() => {
  const id = route.params.clueId
  if (id) loadClue(id)
})

const loadClue = (id) => {
  doGet('/api/clue/' + id).then((resp) => {
    if (resp.data.code === 200) {
      clue.value = resp.data.info
      if (!clue.value.intentionStrength) clue.value.intentionStrength = 5
    }
  })
}

const buildFormData = () => {
  const fd = new FormData()
  const c = clue.value
  if (c.id) fd.append('id', c.id)
  fd.append('name', c.name)
  fd.append('phone', c.phone)
  if (c.age != null) fd.append('age', c.age)
  if (c.intentionCourse) fd.append('intentionCourse', c.intentionCourse)
  if (c.intentionStrength != null) fd.append('intentionStrength', c.intentionStrength)
  if (c.source) fd.append('source', c.source)
  if (c.remark) fd.append('remark', c.remark)
  if (c.trialClassTime) fd.append('trialClassTime', c.trialClassTime)
  fd.append('clueStatus', c.clueStatus || '未联系')
  return fd
}

const submit = (el) => {
  el.validate((ok) => {
    if (!ok) return
    const page = route.query.page
    const back = { path: '/dashboard/clue', ...(page ? { query: { page: String(page) } } : {}) }
    const req = clue.value.id ? doPut('/api/clue', buildFormData()) : doPost('/api/clue/add', buildFormData())
    req.then((resp) => {
      if (resp.data.code === 200) {
        showMessage(resp.data.msg || '保存成功', 'success')
        router.push(back)
      } else {
        showMessage(resp.data.msg || '保存失败', 'error')
      }
    })
  })
}

const goBack = () => {
  const page = route.query.page
  router.push({ path: '/dashboard/clue', ...(page ? { query: { page: String(page) } } : {}) })
}
</script>

<style scoped>
.clue-input-form { max-width: 560px; }
.clue-input-form__control { width: 100%; max-width: 320px; }
</style>
