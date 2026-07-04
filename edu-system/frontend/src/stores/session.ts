import { defineStore } from 'pinia'

export type RoleCode = 'ADMIN' | 'TEACHER' | 'STUDENT'

export interface SessionUser {
  user_id: number
  username: string
  role_code: RoleCode
  display_name: string
  related_id: number | null
  token: string
}

const storageKey = 'edu-system-session'

export const useSessionStore = defineStore('session', {
  state: () => ({
    user: JSON.parse(localStorage.getItem(storageKey) || 'null') as SessionUser | null
  }),
  actions: {
    setUser(user: SessionUser) {
      this.user = user
      localStorage.setItem(storageKey, JSON.stringify(user))
    },
    logout() {
      this.user = null
      localStorage.removeItem(storageKey)
    }
  }
})
