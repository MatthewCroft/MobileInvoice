/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

beforeEach(() => {
  ;(global as any).fetch = vi
    .fn()
    // initial GET on dashboard
    .mockResolvedValueOnce({ ok: true, json: async () => [] })
    // POST create user
    .mockResolvedValueOnce({ ok: true, json: async () => ({ id: '1', email: 'new@example.com' }) })
    // GET after creation
    .mockResolvedValueOnce({ ok: true, json: async () => [{ id: '1', email: 'new@example.com' }] })
})

afterEach(() => {
  ;(global as any).fetch = undefined
})

test('user can create a new user', async () => {
  const user = userEvent.setup()
  render(<App />)

  await user.click(screen.getByRole('button', { name: /new user/i }))
  await user.type(screen.getByLabelText(/email/i), 'new@example.com')
  await user.type(screen.getByLabelText(/password/i), 'pass')
  await user.click(screen.getByRole('button', { name: /create/i }))

  expect(await screen.findByText('new@example.com')).toBeInTheDocument()
})
