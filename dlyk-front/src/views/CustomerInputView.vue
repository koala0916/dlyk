<template>
  <el-form
    ref="customerFormRef"
    :model="customer"
    :rules="customerRules"
    label-width="220px"
    class="customer-input-form"
  >
    <el-form-item label="姓名" prop="name">
      <el-input v-model="customer.name" class="customer-input-form__control" />
    </el-form-item>

    <el-form-item label="电话" prop="phone">
      <el-input v-model="customer.phone" class="customer-input-form__control" />
    </el-form-item>

    <el-form-item label="年龄" prop="age">
      <el-input-number
        v-model="customer.age"
        :min="1"
        :max="120"
        controls-position="right"
        class="customer-input-form__control"
      />
    </el-form-item>

    <el-form-item label="课程类型" prop="courseType">
      <el-input v-model="customer.courseType" placeholder="如：少儿武术基础班" class="customer-input-form__control" />
    </el-form-item>

    <el-form-item label="剩余课时" prop="remainingLessons">
      <!-- 编辑时使用带加减号的数字框 -->
      <el-input-number
        v-if="customer.id"
        v-model="customer.remainingLessons"
        :min="0"
        :max="9999"
        controls-position="both"
        class="customer-input-form__control"
      />
      <el-input-number
        v-else
        v-model="customer.remainingLessons"
        :min="0"
        :max="9999"
        controls-position="right"
        class="customer-input-form__control"
      />
    </el-form-item>

    <el-form-item label="课程到期时间" prop="courseExpireTime">
      <el-date-picker
        v-model="customer.courseExpireTime"
        type="datetime"
        placeholder="选择到期时间"
        value-format="YYYY-MM-DD HH:mm:ss"
        class="customer-input-form__control"
      />
    </el-form-item>

    <el-form-item label="来源" prop="source">
      <el-input v-model="customer.source" placeholder="如：门店咨询、转介绍" class="customer-input-form__control" />
    </el-form-item>

    <el-form-item label="是否正在学习" prop="studying">
      <el-select v-model="customer.studying" placeholder="请选择" class="customer-input-form__control">
        <el-option v-for="item in studyingOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>

    <el-form-item label="备注" prop="remark">
      <el-input v-model="customer.remark" type="textarea" :rows="5" class="customer-input-form__control" />
    </el-form-item>

    <!-- 仅编辑时展示，且不可修改 -->
    <el-form-item v-if="customer.id" label="创建人">
      <el-input :model-value="createByName" disabled class="customer-input-form__control" />
    </el-form-item>
    <el-form-item v-if="customer.id" label="创建时间">
      <el-input v-model="customer.createTime" disabled class="customer-input-form__control" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="customerSubmit(customerFormRef)">提交</el-button>
      <el-button type="primary" plain @click="goBack">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showMessage } from '../util/message'
import { doPost, doGet, doPut } from '../http/httpRequest'

const customer = ref({
  studying: 1,
})
const studyingOptions = [
  { label: '是', value: 1 },
  { label: '否', value: 0 },
]
const router = useRouter()
const route = useRoute()
const customerFormRef = ref()

// 创建人姓名（来自关联对象）
const createByName = computed(() => {
  if (customer.value.createByDO && customer.value.createByDO.name) {
    return customer.value.createByDO.name
  }
  return ''
})

const customerRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { pattern: /\S/, message: '姓名不能只包含空格', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { min: 11, max: 11, message: '手机号码必须是11位', trigger: 'blur' },
    { pattern: /^1(3|4|5|6|7|8|9)\d{9}$/, message: '手机号码格式有误', trigger: 'blur' },
  ],
  studying: [{ required: true, message: '请选择是否正在学习', trigger: 'change' }],
}

const goBack = () => {
  const page = route.query.page
  router.push({
    path: '/dashboard/customer',
    ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
  })
}

onMounted(() => {
  const id = route.params.customerId
  if (id) {
    loadCustomer(id)
  }
})

const loadCustomer = (id) => {
  doGet('/api/customer/' + id, {}).then((resp) => {
    if (resp.data.code === 200) {
      customer.value = resp.data.info
      if (!customer.value.createByDO) {
        customer.value.createByDO = {}
      }
    } else {
      showMessage(resp.data.msg || '加载失败', 'error')
    }
  })
}

// 组装 FormData 提交（与用户管理一致）
const buildFormData = () => {
  const formData = new FormData()
  const c = customer.value
  if (c.id) {
    formData.append('id', c.id)
  }
  formData.append('name', c.name)
  formData.append('phone', c.phone)
  if (c.age != null && c.age !== '') {
    formData.append('age', c.age)
  }
  if (c.courseType) {
    formData.append('courseType', c.courseType)
  }
  if (c.remainingLessons != null && c.remainingLessons !== '') {
    formData.append('remainingLessons', c.remainingLessons)
  }
  if (c.courseExpireTime) {
    formData.append('courseExpireTime', c.courseExpireTime)
  }
  if (c.source) {
    formData.append('source', c.source)
  }
  if (c.remark) {
    formData.append('remark', c.remark)
  }
  formData.append('studying', c.studying != null ? c.studying : 1)
  return formData
}

const customerSubmit = (formEl) => {
  if (!formEl) {
    return
  }
  formEl.validate((valid) => {
    if (!valid) {
      showMessage('请检查表单填写', 'error')
      return
    }
    const formData = buildFormData()
    const page = route.query.page
    const backQuery = page != null && String(page) !== '' ? { query: { page: String(page) } } : {}

    if (customer.value.id) {
      doPut('/api/customer', formData).then((resp) => {
        if (resp.data.code === 200) {
          showMessage(resp.data.msg || '保存成功', 'success')
          router.push({ path: '/dashboard/customer', ...backQuery })
        } else {
          showMessage(resp.data.msg || '保存失败', 'error')
        }
      })
    } else {
      doPost('/api/customer/add', formData).then((resp) => {
        if (resp.data.code === 200) {
          showMessage(resp.data.msg || '添加成功', 'success')
          router.push({ path: '/dashboard/customer', ...backQuery })
        } else {
          showMessage(resp.data.msg || '添加失败', 'error')
        }
      })
    }
  })
}
</script>

<style scoped>
.customer-input-form {
  max-width: 560px;
  padding: 8px 0;
}
.customer-input-form__control {
  width: 100%;
  max-width: 320px;
}
</style>
