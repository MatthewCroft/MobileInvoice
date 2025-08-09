import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { useNavigate } from "react-router-dom"

interface User {
  id: string
  email: string
}

export function UsersDashboard() {
  const [users, setUsers] = useState<User[]>([])
  const navigate = useNavigate()

  useEffect(() => {
    fetch("/users")
      .then((res) => res.json())
      .then(setUsers)
      .catch(() => setUsers([]))
  }, [])

  return (
    <div className="p-4 flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h1 className="text-xl font-semibold">Users</h1>
        <Button onClick={() => navigate('/users/new')}>Create User</Button>
      </div>
      <ul className="flex flex-col gap-2">
        {users.map((user) => (
          <li key={user.id} className="rounded border p-2">
            {user.email}
          </li>
        ))}
      </ul>
    </div>
  )
}
