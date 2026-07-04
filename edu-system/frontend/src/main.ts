import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/global.css'
import App from './App.vue'
import router from './router'

const logoUrl = new URL('../static/zjut.png', import.meta.url).href

document.title = '浙江工业大学反方教务管理系统'

const icon = document.querySelector<HTMLLinkElement>("link[rel='icon']") || document.createElement('link')
icon.rel = 'icon'
icon.href = logoUrl
document.head.appendChild(icon)

createApp(App).use(createPinia()).use(router).use(ElementPlus).mount('#app')
