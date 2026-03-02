import React from 'react'
import ReactDOM from 'react-dom/client'
import { EuiProvider } from '@elastic/eui'
import '@elastic/eui/dist/eui_theme_light.css'
import App from './App.tsx'
import './index.css'

import { appendIconComponentCache } from '@elastic/eui/es/components/icon/icon'
import { icon as EuiIconSearch } from '@elastic/eui/es/components/icon/assets/search'
import { icon as EuiIconCross } from '@elastic/eui/es/components/icon/assets/cross'
import { icon as EuiIconCrossInCircle } from '@elastic/eui/es/components/icon/assets/crossInCircle'
import { icon as EuiIconRefresh } from '@elastic/eui/es/components/icon/assets/refresh'
import { icon as EuiIconCheck } from '@elastic/eui/es/components/icon/assets/check'
import { icon as EuiIconError } from '@elastic/eui/es/components/icon/assets/error'
import { icon as EuiIconWarning } from '@elastic/eui/es/components/icon/assets/warning'
import { icon as EuiIconArrowDown } from '@elastic/eui/es/components/icon/assets/arrow_down'
import { icon as EuiIconArrowUp } from '@elastic/eui/es/components/icon/assets/arrow_up'
import { icon as EuiIconArrowLeft } from '@elastic/eui/es/components/icon/assets/arrow_left'
import { icon as EuiIconArrowRight } from '@elastic/eui/es/components/icon/assets/arrow_right'
import { icon as EuiIconEmpty } from '@elastic/eui/es/components/icon/assets/empty'

appendIconComponentCache({
  search: EuiIconSearch,
  cross: EuiIconCross,
  crossInCircle: EuiIconCrossInCircle,
  refresh: EuiIconRefresh,
  check: EuiIconCheck,
  error: EuiIconError,
  warning: EuiIconWarning,
  arrowDown: EuiIconArrowDown,
  arrowUp: EuiIconArrowUp,
  arrowLeft: EuiIconArrowLeft,
  arrowRight: EuiIconArrowRight,
  empty: EuiIconEmpty,
})

;(window as unknown as Record<string, unknown>).React = React

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <EuiProvider colorMode="light">
      <App />
    </EuiProvider>
  </React.StrictMode>,
)