import axios from 'axios'

export const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

function extractErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as { message?: string } | undefined
    if (data?.message) return data.message
    if (error.response?.status) return `Request failed, status ${error.response.status}`
    return error.message || 'Request failed'
  }
  if (error instanceof Error) return error.message
  return 'Request failed'
}

function unwrap<T>(data: { success: boolean; message?: string; data: T }): T {
  if (!data.success) throw new Error(data.message || 'Request failed')
  return data.data
}

export async function apiGet<T>(url: string, params?: Record<string, unknown>): Promise<T> {
  try {
    const { data } = await http.get(url, { params })
    return unwrap<T>(data)
  } catch (error) {
    throw new Error(extractErrorMessage(error))
  }
}

export async function apiPost<T>(url: string, body?: unknown): Promise<T> {
  try {
    const { data } = await http.post(url, body)
    return unwrap<T>(data)
  } catch (error) {
    throw new Error(extractErrorMessage(error))
  }
}

export async function apiPut<T>(url: string, body?: unknown): Promise<T> {
  try {
    const { data } = await http.put(url, body)
    return unwrap<T>(data)
  } catch (error) {
    throw new Error(extractErrorMessage(error))
  }
}

export async function apiDelete<T>(url: string): Promise<T> {
  try {
    const { data } = await http.delete(url)
    return unwrap<T>(data)
  } catch (error) {
    throw new Error(extractErrorMessage(error))
  }
}
