//import ... from ... 语句导入，从vue框架中导入createApp()函数
import { createApp } from 'vue'

//import ... 语句，没有from，这个是专门导入css样式的
import './style.css'

//import ... from ... 语句导入，从element-plus这个UI框架中导入ElementPlus组件
import ElementPlus from 'element-plus'

//import ... 语句，没有from，这个是专门导入css样式的，导入element-plus的css样式
import 'element-plus/dist/index.css'

//import ... from ... 语句导入，从element-plus这个项目下的icons-vue包中导入所有的图标组件
//import * as ElementPlusIconsVue from '@element-plus/icons-vue'

//import ... from ... 语句导入，从App.vue页面导入App组件，组件名就是页面的文件名
import App from './App.vue'
//import LoginView from "./views/LoginView.vue";
//import DashboardView from "./views/DashboardView.vue";
import {router} from "./router/router.js";

//国际化
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

//js代码
createApp(App)  //相当于java中的main方法开始启动运行vue项目代码
    .use(ElementPlus,{
        locale:zhCn
    }) //使用ElementPlus组件，让ElementPlus生效
    .use(router)  //使用router路由组件，让路由生效
    .mount('#app') //把App页面运行后的结果在index.html页面的<div id="app">下面显示</div>