import './App.css'
import { Routes, Route, Navigate } from 'react-router-dom'
import { UsersDashboard } from '@/UsersDashboard'
import { CreateUser } from '@/CreateUser'

function App() {
  return (
    <Routes>
      <Route path="/users" element={<UsersDashboard />} />
      <Route path="/users/new" element={<CreateUser />} />
      <Route path="*" element={<Navigate to="/users" replace />} />
    </Routes>
  )
}

export default App
