import React from 'react'
import ReactDOM from 'react-dom/client'
import { EuiProvider } from '@elastic/eui'
import '@elastic/eui/dist/eui_theme_light.css'
import App from './App.tsx'
import './index.css'

// @elastic/eui bundles some components compiled with the classic JSX transform
// which expects React to be available as a global variable.
;(window as unknown as Record<string, unknown>).React = React

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <EuiProvider colorMode="light">
      <App />
    </EuiProvider>
  </React.StrictMode>,
)