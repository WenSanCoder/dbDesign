<template>
  <div class="page">
    <h1 class="page-title">成绩录入</h1>
    <section class="plain-panel">
      <div class="filter-row">
        <span>教学班</span>
        <el-select v-model="selectedClassId" placeholder="请选择教学班" style="width: 280px" @change="loadGrades">
          <el-option v-for="item in classes" :key="item.teaching_class_id" :label="`${item.course_name} - ${item.class_name}`" :value="item.teaching_class_id" />
        </el-select>
        <el-button type="primary" :disabled="!selectedClassId" @click="save(false)">保存草稿</el-button>
        <el-button type="success" :disabled="!selectedClassId" @click="save(true)">提交成绩</el-button>
        <el-button :disabled="!selectedClassId" @click="loadStats">成绩统计</el-button>
      </div>
    </section>
    <section class="plain-panel">
      <el-table :data="grades" border>
        <el-table-column prop="student_no" label="学号" width="120" />
        <el-table-column prop="student_name" label="姓名" width="100" />
        <el-table-column prop="admin_class_name" label="行政班" width="120" />
        <el-table-column label="平时分" width="150">
          <template #default="{ row }"><el-input v-model="row.usual_score" inputmode="decimal" placeholder="请输入" /></template>
        </el-table-column>
        <el-table-column label="考试分" width="150">
          <template #default="{ row }"><el-input v-model="row.exam_score" inputmode="decimal" placeholder="请输入" /></template>
        </el-table-column>
        <el-table-column label="总评" width="100">
          <template #default="{ row }">{{ calcFinal(row) }}</template>
        </el-table-column>
        <el-table-column prop="grade_point" label="绩点" width="90" />
        <el-table-column label="备注" min-width="150">
          <template #default="{ row }"><el-input v-model="row.remark" placeholder="请输入" /></template>
        </el-table-column>
      </el-table>
    </section>

    <section v-if="stats" class="plain-panel">
      <h2 class="section-title">成绩统计</h2>
      <div class="metric-grid">
        <div class="metric-item"><div class="metric-label">参考人数</div><div class="metric-value">{{ stats.overview.grade_count || 0 }}</div></div>
        <div class="metric-item"><div class="metric-label">平均分</div><div class="metric-value">{{ stats.overview.avg_score || '-' }}</div></div>
        <div class="metric-item"><div class="metric-label">最高分</div><div class="metric-value">{{ stats.overview.max_score || '-' }}</div></div>
        <div class="metric-item"><div class="metric-label">及格率</div><div class="metric-value">{{ stats.overview.pass_rate || 0 }}%</div></div>
      </div>
      <el-table :data="stats.ranking" border style="margin-top: 14px">
        <el-table-column prop="rank_no" label="排名" width="80" />
        <el-table-column prop="student_no" label="学号" width="120" />
        <el-table-column prop="student_name" label="姓名" width="100" />
        <el-table-column prop="class_name" label="行政班" width="120" />
        <el-table-column prop="final_score" label="总评" width="100" />
        <el-table-column prop="grade_point" label="绩点" width="100" />
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { apiGet, apiPost } from '../../api/http'
import { useSessionStore } from '../../stores/session'

const session = useSessionStore()
const route = useRoute()
const teacherId = session.user?.related_id || 1
const classes = ref<any[]>([])
const grades = ref<any[]>([])
const selectedClassId = ref<number | null>(route.query.classId ? Number(route.query.classId) : null)
const stats = ref<any>(null)

onMounted(async () => {
  await loadClasses()
  if (selectedClassId.value) await loadGrades()
})

async function loadClasses() {
  try {
    classes.value = await apiGet(`/teacher/${teacherId}/classes`)
    if (!selectedClassId.value && classes.value.length) selectedClassId.value = classes.value[0].teaching_class_id
    if (selectedClassId.value) await loadGrades()
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

async function loadGrades() {
  if (!selectedClassId.value) return
  try {
    grades.value = await apiGet(`/teacher/${teacherId}/classes/${selectedClassId.value}/grades`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}

function calcFinal(row: any) {
  const usual = Number(row.usual_score ?? 0)
  const exam = Number(row.exam_score ?? 0)
  return (usual * 0.3 + exam * 0.7).toFixed(2)
}

async function save(submit: boolean) {
  if (!selectedClassId.value) return
  try {
    if (submit) await ElMessageBox.confirm('提交后学生端可查询成绩，确定提交吗？', '提交确认', { type: 'warning' })
    const body = {
      grades: grades.value.map((row) => ({
        selectionId: row.selection_id,
        usualScore: row.usual_score === '' ? null : Number(row.usual_score),
        examScore: row.exam_score === '' ? null : Number(row.exam_score),
        remark: row.remark
      }))
    }
    const url = submit
      ? `/teacher/${teacherId}/classes/${selectedClassId.value}/grades/submit`
      : `/teacher/${teacherId}/classes/${selectedClassId.value}/grades`
    await apiPost(url, body)
    ElMessage.success(submit ? '成绩已提交' : '草稿已保存')
    await loadGrades()
  } catch (error) {
    if (error instanceof Error) ElMessage.error(error.message)
  }
}

async function loadStats() {
  if (!selectedClassId.value) return
  try {
    stats.value = await apiGet(`/teacher/${teacherId}/classes/${selectedClassId.value}/grade-statistics`)
  } catch (error) {
    ElMessage.error((error as Error).message)
  }
}
</script>

<style scoped>
.section-title {
  margin: 0 0 12px;
  font-size: 16px;
}
</style>
