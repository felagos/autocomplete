import React from 'react'
import ReactDOM from 'react-dom/client'
import { EuiProvider } from '@elastic/eui'
import '@elastic/eui/dist/eui_theme_light.css'
import App from './App.tsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <EuiProvider colorMode="light">
      <App />
    </EuiProvider>
  </React.StrictMode>,
)