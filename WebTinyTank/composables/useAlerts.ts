export interface Alert {
  id: number
  message: string
  title: string
  level: 'warning' | 'success' | 'danger'
}

let counter = 0

/** Flash-alert system, port of the legacy global `myAlert`. Auto-dismiss after 5s. */
export function useAlerts() {
  const alerts = useState<Alert[]>('alerts', () => [])

  function dismiss(id: number) {
    alerts.value = alerts.value.filter((a) => a.id !== id)
  }

  function notify(message: string, title = '', level: Alert['level'] = 'warning') {
    if (!message) return
    const id = ++counter
    alerts.value = [...alerts.value, { id, message, title, level }]
    if (import.meta.client) setTimeout(() => dismiss(id), 5000)
  }

  return { alerts, notify, dismiss }
}
