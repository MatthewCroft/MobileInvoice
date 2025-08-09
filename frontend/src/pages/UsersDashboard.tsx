import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getUsers, User } from '@/lib/api'
import { Button } from '@/components/ui/button'

export function UsersDashboard() {
  const [users, setUsers] = useState<User[]>([])
  const navigate = useNavigate()

  useEffect(() => {
    getUsers().then(setUsers).catch(() => setUsers([]))
  }, [])

  return (
    <div className="p-4 max-w-md mx-auto w-full">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-xl font-bold">Users</h1>
        <Button onClick={() => navigate('/new-user')}>New User</Button>
      </div>
      <ul className="space-y-2">
        {users.map((u) => (
          <li key={u.id} className="border p-2 rounded">
            {u.email}
          </li>
        ))}
      </ul>
    </div>
  )
}
