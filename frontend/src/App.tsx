import { useState } from 'react'
import {
  EuiPage,
  EuiPageBody,
  EuiPageSection,
  EuiTitle,
  EuiText,
  EuiSpacer,
  EuiPanel,
  EuiFlexGroup,
  EuiFlexItem,
  EuiCallOut,
  EuiIcon,
} from '@elastic/eui'
import Autocomplete from './components/Autocomplete'
import TopTerms from './components/TopTerms'

function App() {
  const [selectedTerm, setSelectedTerm] = useState<string>('')

  return (
    <EuiPage paddingSize="none" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', minHeight: '100vh' }}>
      <EuiPageBody paddingSize="none">
        <EuiPageSection color="transparent" alignment="center" paddingSize="l">
          <EuiFlexGroup direction="column" alignItems="center" gutterSize="xl">
            {/* Header */}
            <EuiFlexItem>
              <EuiFlexGroup direction="column" alignItems="center" gutterSize="s">
                <EuiFlexItem>
                  <EuiTitle size="l">
                    <h1 style={{ color: 'white', display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <EuiIcon type="search" size="xl" />
                      Sistema de Autocompletado
                    </h1>
                  </EuiTitle>
                </EuiFlexItem>
                <EuiFlexItem>
                  <EuiText style={{ color: 'white', opacity: 0.9 }}>
                    <p>Implementado con conceptos de System Design Interview</p>
                  </EuiText>
                </EuiFlexItem>
              </EuiFlexGroup>
            </EuiFlexItem>

            <EuiSpacer size="l" />

            {/* Autocomplete Section */}
            <EuiFlexItem style={{ width: '100%', maxWidth: '800px' }}>
              <EuiPanel>
                <EuiTitle size="m">
                  <h2>Búsqueda con Autocompletado</h2>
                </EuiTitle>
                <EuiSpacer size="m" />
                <Autocomplete onSelect={setSelectedTerm} />
                {selectedTerm && (
                  <>
                    <EuiSpacer size="m" />
                    <EuiCallOut
                      title="Término seleccionado"
                      color="success"
                      iconType="check"
                    >
                      <EuiText size="s">
                        <strong>{selectedTerm}</strong>
                      </EuiText>
                    </EuiCallOut>
                  </>
                )}
              </EuiPanel>
            </EuiFlexItem>

            {/* Top Terms Section */}
            <EuiFlexItem style={{ width: '100%', maxWidth: '800px' }}>
              <TopTerms />
            </EuiFlexItem>

            {/* Features Section */}
            <EuiFlexItem style={{ width: '100%', maxWidth: '800px' }}>
              <EuiPanel>
                <EuiTitle size="m">
                  <h3>Características del Sistema</h3>
                </EuiTitle>
                <EuiSpacer size="m" />
                <EuiText size="s">
                  <ul>
                    <li>✨ Búsqueda en tiempo real mientras escribes</li>
                    <li>📊 Ranking por frecuencia de uso</li>
                    <li>⚡ Cache en backend para optimizar rendimiento</li>
                    <li>🎯 Actualización dinámica de frecuencias</li>
                    <li>💾 Persistencia en base de datos con JPA</li>
                    <li>🐳 Despliegue con Docker Compose</li>
                  </ul>
                </EuiText>
              </EuiPanel>
            </EuiFlexItem>

            {/* Footer */}
            <EuiFlexItem>
              <EuiText size="s" style={{ color: 'white', opacity: 0.8, textAlign: 'center' }}>
                <p>Backend: Spring Boot + Gradle + JPA | Frontend: React 19 + TypeScript + Vite + Elastic UI</p>
              </EuiText>
            </EuiFlexItem>
          </EuiFlexGroup>
        </EuiPageSection>
      </EuiPageBody>
    </EuiPage>
  )
}

export default App