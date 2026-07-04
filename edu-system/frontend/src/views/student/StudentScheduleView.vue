<template>
  <div class="page">
    <h1 class="page-title">我的课表</h1>
    <section class="plain-panel">
      <div class="schedule-grid">
        <div class="schedule-cell schedule-head">节次</div>
        <div v-for="day in days" :key="day" class="schedule-cell schedule-head">{{ day }}</div>
        <template v-for="period in periods" :key="period">
          <div class="schedule-cell schedule-head">第{{ period }}节</div>
          <div v-for="dayNo in 7" :key="`${period}-${dayNo}`" class="schedule-cell">
            <div v-for="item in cellCourses(dayNo, period)" :key="item.teaching_class_id" class="course-block">
              <strong>{{ item.course_name }}</strong><br />
              {{ item.teaching_class_name }}<br />
              {{ item.teacher_name }} {{ item.classroom }}
            </div>
          </div>
        </template>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { apiGet } from '../../api/http'
import { useSessionStore } from '../../stores/session'

const session = useSessionStore()
const rows = ref<any[]>([])
const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
const periods = Array.from({ length: 12 }, (_, index) => index + 1)

onMounted(async () => {
  try {
    rows.value = await apiGet(`/student/${session.user?.related_id || 1}/schedule`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
})

function cellCourses(dayNo: number, period: number) {
  return rows.value.filter((item) => Number(item.weekday) === dayNo && Number(item.start_period) <= period && Number(item.end_period) >= period)
}
</script>
