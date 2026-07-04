import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from '../views/HomeView.vue'
import AdminResourceView from '../views/admin/AdminResourceView.vue'
import StudentCourseSelectionView from '../views/student/StudentCourseSelectionView.vue'
import StudentSelectionsView from '../views/student/StudentSelectionsView.vue'
import StudentScheduleView from '../views/student/StudentScheduleView.vue'
import StudentGradesView from '../views/student/StudentGradesView.vue'
import TeacherClassesView from '../views/teacher/TeacherClassesView.vue'
import TeacherGradesView from '../views/teacher/TeacherGradesView.vue'
import { useSessionStore } from '../stores/session'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    { path: '/', component: HomeView },
    { path: '/admin/:resource', component: AdminResourceView },
    { path: '/student/select', component: StudentCourseSelectionView },
    { path: '/student/selections', component: StudentSelectionsView },
    { path: '/student/schedule', component: StudentScheduleView },
    { path: '/student/grades', component: StudentGradesView },
    { path: '/teacher/classes', component: TeacherClassesView },
    { path: '/teacher/grades', component: TeacherGradesView }
  ]
})

router.beforeEach((to) => {
  const session = useSessionStore()
  if (to.path !== '/login' && !session.user) return '/login'
  if (to.path === '/login' && session.user) return '/'
})

export default router
