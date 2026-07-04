<template>
  <div class="page">
    <h1 class="page-title">我的教学班</h1>
    <section class="plain-panel">
      <el-table :data="rows" border v-loading="loading">
        <el-table-column prop="class_code" label="教学班编号" width="130" />
        <el-table-column prop="class_name" label="教学班名称" min-width="160" />
        <el-table-column prop="course_name" label="课程" min-width="150" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="schedule_text" label="上课安排" min-width="220" />
        <el-table-column label="容量" width="110">
          <template #default="{ row }">{{ row.selected_count }} / {{ row.capacity }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="showStudents(row)">学生名单</el-button>
            <el-button size="small" type="primary" @click="goGrades(row)">成绩录入</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="studentDialog" title="学生名单" width="860px">
      <el-table :data="students" border>
        <el-table-column prop="student_no" label="学号" width="120" />
        <el-table-column prop="student_name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="college_name" label="学院" min-width="160" />
        <el-table-column prop="major_name" label="专业" min-width="150" />
        <el-table-column prop="admin_class_name" label="行政班" width="120" />
        <el-table-column prop="selected_at" label="选课时间" width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { apiGet } from '../../api/http'
import { useSessionStore } from '../../stores/session'

const session = useSessionStore()
const router = useRouter()
const rows = ref<any[]>([])
const students = ref<any[]>([])
const loading = ref(false)
const studentDialog = ref(false)
const teacherId = session.user?.related_id || 1

onMounted(load)

async function load() {
  loading.value = true
  try {
    rows.value = await apiGet(`/teacher/${teacherId}/classes`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  } finally {
    loading.value = false
  }
}

async function showStudents(row: any) {
  try {
    students.value = await apiGet(`/teacher/${teacherId}/classes/${row.teaching_class_id}/students`)
    studentDialog.value = true
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

function goGrades(row: any) {
  router.push({ path: '/teacher/grades', query: { classId: row.teaching_class_id } })
}
</script>
