/* eslint-disable @typescript-eslint/no-explicit-any */
import { render, screen } from '@testing-library/react'
import { UsersDashboard } from './UsersDashboard'
import { BrowserRouter } from 'react-router-dom'

beforeEach(() => {
  global.fetch = vi.fn().mockResolvedValue({
    ok: true,
    json: async () => [{ id: '1', email: 'a@b.com' }],
  }) as unknown as typeof fetch
})

afterEach(() => {
  ;(global as any).fetch = undefined
})

test('renders users from api', async () => {
  render(
    <BrowserRouter>
      <UsersDashboard />
    </BrowserRouter>
  )

  expect(await screen.findByText('a@b.com')).toBeInTheDocument()
})
