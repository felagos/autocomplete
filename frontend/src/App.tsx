import React, { useState } from 'react'
import Autocomplete from './components/Autocomplete'
import TopTerms from './components/TopTerms'
import './App.css'
function App() {
  const [selectedTerm, setSelectedTerm] = useState<string>('')
  return (
    <div className="App">
      <header className="App-header">
        <h1>🔍 Sistema de Autocompletado</h1>
        <p className="subtitle">Implementado con conceptos de System Design Interview</p>
      </header>
      <main className="App-main">
        <div className="autocomplete-section">
          <h2>Búsqueda con Autocompletado</h2>
          <Autocomplete onSelect={setSelectedTerm} />
          {selectedTerm && (
            <div className="selected-term">
              <p>✅ Término seleccionado: <strong>{selectedTerm}</strong></p>
            </div>
          )}
        </div>
        <div className="top-terms-section">
          <TopTerms />
        </div>
        <div className="info-section">
          <h3>Características del Sistema</h3>
          <ul>
            <li>✨ Búsqueda en tiempo real mientras escribes</li>
            <li>📊 Ranking por frecuencia de uso</li>
            <li>⚡ Cache en backend para optimizar rendimiento</li>
            <li>🎯 Actualización dinámica de frecuencias</li>
            <li>💾 Persistencia en base de datos con JPA</li>
            <li>🐳 Despliegue con Docker Compose</li>
          </ul>
        </div>
      </main>
      <footer className="App-footer">
        <p>Backend: Spring Boot + Gradle + JPA | Frontend: React 18 + TypeScript + Vite</p>
      </footer>
    </div>
  )
}
export default App