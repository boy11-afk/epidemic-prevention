import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store";
import Home from '../views/Manage.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children:[
      {path: 'home', name: 'Home', component: () => import('../views/Home.vue')},
      {path: 'admin', name: '管理员管理', component: () => import('../views/Admin.vue')},
      {path: 'person', name: '管理员信息', component: () => import('../views/Person.vue')},
      { path: 'file', name: '文件管理', component: () => import('../views/File.vue')},
      { path: 'student', name: '学生管理', component: () => import('../views/Student.vue')},
      { path: 'report', name: '每日填报（学生）', component: () => import('../views/Report.vue')},
      { path: 'out', name: '出校申请（学生）', component: () => import('../views/Out.vue')},
      { path: 'enter', name: '入校申请（学生）', component: () => import('../views/Enter.vue')},
      { path: 'acid', name: '核酸预约（学生）', component: () => import('../views/Acid.vue')},
      { path: 'teacher', name: '教师管理', component: () => import('../views/Teacher.vue')},
      { path: 'treport', name: '每日填报（教师）', component: () => import('../views/Treport.vue')},
      { path: 'tout', name: '出校登记（教师）', component: () => import('../views/Tout.vue')},
      { path: 'tenter', name: '入校登记（教师）', component: () => import('../views/Tenter.vue')},
      { path: 'tacid', name: '核酸预约（教师）', component: () => import('../views/Tacid.vue')},
      { path: 'article', name: '防疫公告（学生）', component: () => import('../views/Article.vue')},
      { path: 'tarticle', name: '防疫公告（教师）', component: () => import('../views/Tarticle.vue')},
      { path: 'regional', name: '地区疫情', component: () => import('../views/Regional.vue')},
      { path: 'visitorEnter', name: '入校申请（访客）', component: () => import('../views/VisitorEnter.vue')},
      { path: 'visitorOut', name: '出校申请（访客）', component: () => import('../views/VisitorOut.vue')},
    ]
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import(/* webpackChunkName: "about" */ '../views/Register.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  next()  // 放行路由
})

export default router
