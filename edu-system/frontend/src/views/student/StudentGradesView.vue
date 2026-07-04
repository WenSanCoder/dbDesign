<template>
  <div class="page">
    <h1 class="page-title">我的成绩</h1>
    <section class="plain-panel">
      <el-table :data="rows" border>
        <el-table-column prop="academic_year" label="学年" width="120" />
        <el-table-column prop="semester" label="学期" width="80" />
        <el-table-column prop="course_code" label="课程代码" width="110" />
        <el-table-column prop="course_name" label="课程名称" min-width="150" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="usual_score" label="平时分" width="90" />
        <el-table-column prop="exam_score" label="考试分" width="90" />
        <el-table-column prop="final_score" label="总评" width="90" />
        <el-table-column prop="grade_point" label="绩点" width="90" />
      </el-table>
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

onMounted(async () => {
  try {
    rows.value = await apiGet(`/student/${session.user?.related_id || 1}/grades`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
})
</script>
