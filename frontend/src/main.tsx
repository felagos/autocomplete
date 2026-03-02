import React from 'react'
import ReactDOM from 'react-dom/client'
import { EuiProvider } from '@elastic/eui'
import { appendIconComponentCache } from '@elastic/eui/es/components/icon/icon'
import { icon as EuiIconSearch } from '@elastic/eui/es/components/icon/assets/search'
import { icon as EuiIconCheck } from '@elastic/eui/es/components/icon/assets/check'
import { icon as EuiIconCross } from '@elastic/eui/es/components/icon/assets/cross'
import { icon as EuiIconError } from '@elastic/eui/es/components/icon/assets/error'
import { icon as EuiIconRefresh } from '@elastic/eui/es/components/icon/assets/refresh'
import { icon as EuiIconWarning } from '@elastic/eui/es/components/icon/assets/warning'
import { icon as EuiIconInfo } from '@elastic/eui/es/components/icon/assets/info'
import { icon as EuiIconArrowDown } from '@elastic/eui/es/components/icon/assets/arrow_down'
import { icon as EuiIconArrowUp } from '@elastic/eui/es/components/icon/assets/arrow_up'
import App from './App.tsx'
import './index.css'

appendIconComponentCache({
  search: EuiIconSearch,
  check: EuiIconCheck,
  cross: EuiIconCross,
  error: EuiIconError,
  refresh: EuiIconRefresh,
  warning: EuiIconWarning,
  info: EuiIconInfo,
  arrowDown: EuiIconArrowDown,
  arrowUp: EuiIconArrowUp,
})

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <EuiProvider colorMode="light">
      <App />
    </EuiProvider>
  </React.StrictMode>,
)