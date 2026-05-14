<template>
    <!-- 左侧标签加宽、右侧控件限制最大宽度，避免长标签折行 -->
    <el-form ref="userFormRef" :model="user" :rules="userRules" label-width="220px" class="user-input-form"
        style="margin: auto;">
        <el-form-item label="账号" prop="loginAct">
            <el-input ref="loginActRef" type="text" v-model="user.loginAct" class="user-input-form__control"
                @keydown="onFieldNavKey($event, 0)" />
        </el-form-item>



        <!-- 编辑用户：密码框带眼睛图标，默认密文；点眼睛切换明文（Element Plus show-password） -->
        <el-form-item label="密码" prop="loginPwd2" v-if="user.id > 0">
            <el-input ref="loginPwdRef" type="password" v-model="user.loginPwd2" show-password
                class="user-input-form__control" @keydown="onFieldNavKey($event, 1)" />
        </el-form-item>

        <!-- 新增用户：同上，眼睛控制是否显示密码字符 -->
        <el-form-item label="密码" prop="loginPwd" v-else>
            <el-input ref="loginPwdRef" type="password" v-model="user.loginPwd" show-password
                class="user-input-form__control" @keydown="onFieldNavKey($event, 1)" />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
            <el-input ref="nameRef" type="text" v-model="user.name" class="user-input-form__control"
                @keydown="onFieldNavKey($event, 2)" />
        </el-form-item>

        <el-form-item label="手机" prop="phone">
            <el-input ref="phoneRef" type="text" v-model="user.phone" class="user-input-form__control"
                @keydown="onFieldNavKey($event, 3)" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
            <el-input ref="emailRef" type="text" v-model="user.email" class="user-input-form__control"
                @keydown="onFieldNavKey($event, 4)" />
        </el-form-item>

        <el-form-item label="账号是否未过期" prop="accountNoExpired">
            <el-select ref="accountNoExpiredRef" v-model="user.accountNoExpired" placeholder="请选择"
                class="user-input-form__control" @keydown="onFieldNavKey($event, 5)">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </el-form-item>

        <el-form-item label="密码是否未过期" prop="credentialsNoExpired">
            <el-select ref="credentialsNoExpiredRef" v-model="user.credentialsNoExpired" placeholder="请选择"
                class="user-input-form__control" @keydown="onFieldNavKey($event, 6)">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </el-form-item>

        <el-form-item label="账户是否未锁定" prop="accountNoLocked">
            <el-select ref="accountNoLockedRef" v-model="user.accountNoLocked" placeholder="请选择"
                class="user-input-form__control" @keydown="onFieldNavKey($event, 7)">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </el-form-item>

        <el-form-item label="账户是否可用" prop="accountEnabled">
            <el-select ref="accountEnabledRef" v-model="user.accountEnabled" placeholder="请选择"
                class="user-input-form__control" @keydown="onFieldNavKey($event, 8)">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="userSubmit(userFormRef)">提交</el-button>
            <el-button type="primary" plain @click="goBack">返回</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { ref, onMounted, computed, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showMessage } from '../util/message'
import { doPost, doGet, doPut } from '../http/httpRequest'


let user = ref({})

let options = [
    { label: '否', value: '0' },
    { label: '是', value: '1' }
]

const router = useRouter()
const route = useRoute()

// 表单项顺序与模板中 @keydown 的 index 一致：0 账号 … 8 账户是否可用（密码在 1，新增/编辑互斥仍共用 index 1）
const loginActRef = ref(null) // 账号输入框组件引用
const loginPwdRef = ref(null) // 密码输入框组件引用（编辑/新增二选一挂载）
const nameRef = ref(null) // 姓名输入框组件引用
const phoneRef = ref(null) // 手机输入框组件引用
const emailRef = ref(null) // 邮箱输入框组件引用
const accountNoExpiredRef = ref(null) // 账号是否未过期下拉组件引用
const credentialsNoExpiredRef = ref(null) // 密码是否未过期下拉组件引用
const accountNoLockedRef = ref(null) // 账户是否未锁定下拉组件引用
const accountEnabledRef = ref(null) // 账户是否可用下拉组件引用

// 按顺序取组件实例数组，用于调用 Element Plus 的 focus()
const fieldRefs = () =>
    [
        loginActRef, // 第 0 项：账号
        loginPwdRef, // 第 1 项：密码
        nameRef, // 第 2 项：姓名
        phoneRef, // 第 3 项：手机
        emailRef, // 第 4 项：邮箱
        accountNoExpiredRef, // 第 5 项：账号是否未过期
        credentialsNoExpiredRef, // 第 6 项：密码是否未过期
        accountNoLockedRef, // 第 7 项：账户是否未锁定
        accountEnabledRef, // 第 8 项：账户是否可用
    ]

// 将焦点移到指定序号的控件（越界则忽略）
const focusFieldAt = (index) => {
    const list = fieldRefs() // 当前所有字段 ref 列表
    if (index < 0 || index >= list.length) {
        // 序号无效则不做任何事
        return
    }
    const comp = list[index].value // 取出对应子组件实例
    if (comp && typeof comp.focus === 'function') {
        // 有 focus 方法则聚焦（Element Plus 的 input/select 均支持）
        comp.focus()
    }
}

// 上下键：上移到上一项，下移下一项（阻止默认，单行输入框无行内光标歧义）
const onFieldNavKey = (e, index) => {
    if (e.key !== 'ArrowUp' && e.key !== 'ArrowDown') {
        // 非上下键交给浏览器默认行为
        return
    }
    e.preventDefault() // 拦截上下键默认行为
    const delta = e.key === 'ArrowDown' ? 1 : -1 // 下键 +1，上键 -1
    const nextIndex = index + delta // 计算下一个要聚焦的序号
    nextTick(() => focusFieldAt(nextIndex)) // 等 DOM 更新后再聚焦，避免切换分支时 ref 未就绪
}

// 返回用户列表，保留列表页码（来自路由 query.page）
const goBack = () => {
    const page = route.query.page
    router.push({
        path: '/dashboard/user',
        ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
    })
}

// 表单校验：编辑只校验 loginPwd2，新增只校验 loginPwd，避免隐藏项参与校验导致无法提交
const userRules = computed(() => {
    const common = {
        // 登录账号：必填且不能只含空格
        loginAct: [
            { required: true, message: '请输入账号', trigger: 'blur' },
            { pattern: /\S/, message: '账号不能只包含空格', trigger: 'blur' },
        ],
        // 姓名：必填、最少 2 个字符，不再要求必须为中文汉字
        name: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
            { min: 2, message: '姓名至少是两个字符', trigger: 'blur' },
        ],
        phone: [
            { required: true, message: '请输入手机号', trigger: 'blur' },
            { min: 11, max: 11, message: '手机号码必须是11位', trigger: 'blur' },
            { pattern: /^1(3|4|5|6|7|8|9)\d{9}$/, message: '手机号码格式有误', trigger: 'blur' },
        ],
        email: [
            {
                // 邮箱选填；有内容时再校验格式，避免空串触发 type:email 报错
                validator: (_rule, value, callback) => {
                    const v = value === undefined || value === null ? '' : String(value).trim()
                    if (v === '') {
                        callback()
                        return
                    }
                    const emailReg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
                    if (!emailReg.test(v)) {
                        callback(new Error('邮箱格式有误'))
                    } else {
                        callback()
                    }
                },
                trigger: 'blur',
            },
        ],
        accountNoExpired: [{ required: true, message: '请选择', trigger: 'change' }],
        credentialsNoExpired: [{ required: true, message: '请选择', trigger: 'change' }],
        accountNoLocked: [{ required: true, message: '请选择', trigger: 'change' }],
        accountEnabled: [{ required: true, message: '请选择', trigger: ['blur', 'change'] }],
    }
    if (user.value.id) {
        return {
            ...common,
            loginPwd2: [
                { required: true, message: '请输入密码', trigger: 'blur' },
                { pattern: /\S/, message: '密码不能只包含空格', trigger: 'blur' },
            ],
        }
    }
    return {
        ...common,
        loginPwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { pattern: /\S/, message: '密码不能只包含空格', trigger: 'blur' },
        ],
    }
})


//提交表单
let userFormRef = ref({})

const userSubmit = (formEl) => {
    if (!formEl) {
        return; // 如果 formEl 未定义，直接返回，避免报错
    }

    formEl.validate(valid => {
        if (valid) {//验证通过
            //处理表单数据
            let formData = new FormData();

            //遍历user对象的属性，将属性名和属性值添加到formData中
            // {loginAct: "admin",loginPwd:"123"}
            for (let prop in user.value) {
                //prop是user对象的属性名，user.value[prop]是属性值
                console.log(prop, user.value[prop]);
                //将属性名和属性值添加到formData中
                formData.append(prop, user.value[prop]);
            }

            //判断是修改还是添加
            if (user.value.id) {//修改
                doPut('api/user', formData).then(resp => {
                    if (resp.data.code == 200) {
                        showMessage(resp.data.msg, 'success')
                        // 跳转到用户列表，并带上返回页码
                        const page = route.query.page
                        router.push({
                            path: '/dashboard/user',
                            ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
                        })
                    } else {
                        showMessage(resp.data.msg, 'error')
                    }
                })
            } else {
                //提交表单数据到后端
                doPost('api/user', formData).then(resp => {
                    if (resp.data.code == 200) {
                        showMessage(resp.data.msg, 'success')
                        const page = route.query.page
                        router.push({
                            path: '/dashboard/user',
                            ...(page != null && String(page) !== '' ? { query: { page: String(page) } } : {}),
                        })
                    } else {
                        showMessage(resp.data.msg, 'error')
                    }
                })
            }


        } else {//验证失败
            showMessage('表单提交失败', 'error')
        }
    })

}



//页面渲染之后，查询用户信息，回显到页面中
onMounted(() => {
    //获取用户的主键id
    let id = route.params.userId
    if (id) {
        //修改 查询用户信息，回填到页面中
        loadUser(id)
    }
})

const loadUser = (id) => {
    doGet('api/user/' + id, {}).then(resp => {
        if (resp.data.code == 200) {
            user.value = resp.data.info
        } else {
            showMessage(resp.data.msg, 'error')
        }
    })
}
</script>

<style scoped>
/* 表单整体最大宽度，居中 */
.user-input-form {
    max-width: 560px;
    padding: 8px 0;
}

/* 右侧输入框、下拉：固定较窄宽度，左侧由 label-width 留出更宽标签区 */
.user-input-form__control {
    width: 100%;
    max-width: 260px;
}
</style>