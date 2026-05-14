<script setup>
import { ref, nextTick } from 'vue'
import { doPost } from "../http/httpRequest.js"

import { TOKEN_NAME } from "../util/constant"

import { showMessage } from '../util/message.js'

//定义user对象
let user = ref({})
//定义ref对象
let userFormRef = ref()

// 账号、密码输入框组件引用（上下键顺序：0 账号，1 密码）
const loginActRef = ref(null)
const loginPwdRef = ref(null)
// 按顺序返回各输入框 ref，供上下键切换焦点
const fieldRefs = () => [loginActRef, loginPwdRef]

// 将焦点移到指定序号输入框（越界则忽略）
const focusFieldAt = (index) => {
  const list = fieldRefs()
  if (index < 0 || index >= list.length) {
    return
  }
  const comp = list[index].value
  if (comp && typeof comp.focus === 'function') {
    comp.focus()
  }
}

// 上下键：在账号与密码之间移动焦点
const onFieldNavKey = (e, index) => {
  if (e.key !== 'ArrowUp' && e.key !== 'ArrowDown') {
    return
  }
  e.preventDefault()
  const delta = e.key === 'ArrowDown' ? 1 : -1
  nextTick(() => focusFieldAt(index + delta))
}

//定义表单校验规则对象
let userRules = {
  //给loginAct添加校验规则
  //required: true  表示必填项，不能为空
  //min: 3  表示最小长度为3
  //max: 10  表示最大长度为10
  loginAct: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在3到20个字符之间', trigger: 'blur' },
  ],
  //给loginPwd添加校验规则
  loginPwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' },
  ],
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
        } else { //登录失败
          //提示用户登录失败
          showMessage(response.data.msg, 'error')
        }
      })
    }
  })
}

// 回车键：等同于点击「登录」（须写在 login 定义之后）
const onLoginEnter = () => {
  login(userFormRef.value)
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
          <el-input ref="loginActRef" v-model="user.loginAct" @keydown="onFieldNavKey($event, 0)"
            @keyup.enter="onLoginEnter" />
        </el-form-item>
        <el-form-item label="密码" prop="loginPwd">
          <!-- show-password：右侧眼睛图标，默认密文（带斜线）；点击切换明文 -->
          <el-input ref="loginPwdRef" v-model="user.loginPwd" type="password" show-password
            @keydown="onFieldNavKey($event, 1)" @keyup.enter="onLoginEnter" />
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
  /* 优先使用 Noto Sans SC（index.html 引入），不可用时回退到系统高质量黑体 */
  font-family: "Noto Sans SC", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei UI", "Microsoft YaHei",
    -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
  font-weight: 600;
  font-size: 26px;
  letter-spacing: 0.08em;
  line-height: 1.35;
  text-align: center;
  color: #141414;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.el-button {
  width: 100%;
}
</style>
