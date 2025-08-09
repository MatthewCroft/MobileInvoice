import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { UsersDashboard } from './pages/UsersDashboard'
import { CreateUser } from './pages/CreateUser'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<UsersDashboard />} />
        <Route path="/new-user" element={<CreateUser />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
