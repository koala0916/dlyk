import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],

  //配置vite工具内嵌的http服务器的端口信息
  server:{
    //host:'0.0.0.0' ,//ip地址， 0.0.0.0保留IP，代表任何IP
    port: 8080, // 设置服务启动端口号
    open: true, // 设置服务启动时是否自动打开浏览器
  },
  resolve: {  
    alias: {  
      '@': '/src'  
    }  
  }  
})
