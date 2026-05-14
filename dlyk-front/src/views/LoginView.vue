<script setup>
import { ref } from 'vue'
import { doPost } from "../http/httpRequest.js"

import { TOKEN_NAME } from "../util/constant"

import { showMessage } from '../util/message.js'

//定义user对象
let user = ref({})
//定义ref对象
let userFormRef = ref()

//定义表单校验规则对象
let userRules = {
  //给loginAct添加校验规则
  //required: true  表示必填项，不能为空
  //min: 3  表示最小长度为3
  //max: 10  表示最大长度为10
  loginAct: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在3到20个字符之间', trigger: 'blur' }
  ],
  //给loginPwd添加校验规则
  loginPwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' }
  ]
}


//定义登录方法
const login = (formEl) => {
  if (!formEl) {
    return//如果formEl为空，则直接返回
  }

  //判断是否校验通过
  formEl.validate((valid) => {
    if (valid) {
      //校验通过，发送登录请求
      //获取表单数据
      let loginAct = user.value.loginAct
      let loginPwd = user.value.loginPwd

      //将数据拼接到表单对象中
      let formData = new FormData() //js提供的表单对象，用于封装表单数据
      formData.append('loginAct', loginAct) //将表单数据添加到表单对象中
      formData.append('loginPwd', loginPwd) //将表单数据添加到表单对象中

      //发送登录请求
      doPost("/api/login", formData).then((response) => {
        if (response.data.code == 200) { //登录成功
          //获取后端返回的token，存入sessionStorage中
          sessionStorage.setItem(TOKEN_NAME, response.data.info)

          //跳转到首页
          location.href = '/dashboard'
        }else{ //登录失败
          //提示用户登录失败
          showMessage(response.data.msg, 'error')
        }
      })
    }
  })
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="welcome">欢迎使用杜力优课</div>

      <!-- 
        ref="userFormRef"  绑定表单元素的ref属性
        :model="user"  绑定表单数据对象
        :rules="userRules"  绑定表单校验规则对象
      -->
      <el-form ref="userFormRef" :model="user" :rules="userRules" label-width="55px">
        <el-form-item label="账号" prop="loginAct">
          <el-input v-model="user.loginAct" />
        </el-form-item>
        <el-form-item label="密码" prop="loginPwd">
          <el-input v-model="user.loginPwd" type="password" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="login(userFormRef)">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
  padding-top: 15vh;
}

.login-box {
  width: 400px;
  padding: 40px;
}

.welcome {
  margin-bottom: 30px;
  font-weight: bold;
  font-size: 24px;
  text-align: center;
}

.el-button {
  width: 100%;
}
</style>