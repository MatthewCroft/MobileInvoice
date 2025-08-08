export interface User {
  id: string
  email: string
}

export interface CreateUser {
  email: string
  password: string
}

export async function getUsers(): Promise<User[]> {
  const res = await fetch('/users')
  if (!res.ok) throw new Error('Failed to fetch users')
  return res.json()
}

export async function createUser(data: CreateUser): Promise<User> {
  const res = await fetch('/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error('Failed to create user')
  return res.json()
}
