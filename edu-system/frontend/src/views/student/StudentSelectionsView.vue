<template>
  <div class="page">
    <h1 class="page-title">我的选课</h1>
    <section class="plain-panel">
      <el-table :data="rows" border v-loading="loading">
        <el-table-column prop="course_code" label="课程代码" width="110" />
        <el-table-column prop="course_name" label="课程名称" min-width="150" />
        <el-table-column prop="class_name" label="教学班" min-width="160" />
        <el-table-column prop="teacher_name" label="任课教师" width="110" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="selected_at" label="选课时间" width="180" />
        <el-table-column prop="dropped_at" label="退课时间" width="180" />
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
const loading = ref(false)
const studentId = session.user?.related_id || 1

onMounted(async () => {
  loading.value = true
  try {
    rows.value = await apiGet(`/student/${studentId}/selections`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
})
</script>
