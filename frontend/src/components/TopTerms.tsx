import { useState, useEffect } from 'react'
import {
  EuiPanel,
  EuiTitle,
  EuiFlexGroup,
  EuiFlexItem,
  EuiButtonIcon,
  EuiSpacer,
  EuiProgress,
  EuiText,
  EuiBadge,
  EuiLoadingSpinner,
  EuiCallOut,
  EuiButton,
} from '@elastic/eui'
import { getTopTerms, SuggestionDTO } from '../services/api'

export function TopTerms() {
  const [topTerms, setTopTerms] = useState<SuggestionDTO[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    loadTopTerms()
  }, [])

  const loadTopTerms = async () => {
    setLoading(true)
    setError(null)
    try {
      const terms = await getTopTerms(10)
      setTopTerms(terms)
    } catch (err) {
      console.error('Error al cargar términos populares:', err)
      setError('Error al cargar los términos populares')
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <EuiPanel>
        <EuiFlexGroup direction="column" alignItems="center" gutterSize="m">
          <EuiFlexItem>
            <EuiTitle size="m">
              <h2>🔥 Términos Más Populares</h2>
            </EuiTitle>
          </EuiFlexItem>
          <EuiFlexItem>
            <EuiLoadingSpinner size="xl" />
          </EuiFlexItem>
        </EuiFlexGroup>
      </EuiPanel>
    )
  }

  if (error) {
    return (
      <EuiPanel>
        <EuiTitle size="m">
          <h2>🔥 Términos Más Populares</h2>
        </EuiTitle>
        <EuiSpacer size="m" />
        <EuiCallOut
          title="Error"
          color="danger"
          iconType="error"
        >
          {error}
        </EuiCallOut>
        <EuiSpacer size="m" />
        <EuiButton onClick={loadTopTerms} fill>
          Reintentar
        </EuiButton>
      </EuiPanel>
    )
  }

  const maxFrequency = topTerms[0]?.frequency || 1

  return (
    <EuiPanel>
      <EuiFlexGroup justifyContent="spaceBetween" alignItems="center">
        <EuiFlexItem>
          <EuiTitle size="m">
            <h2>🔥 Términos Más Populares</h2>
          </EuiTitle>
        </EuiFlexItem>
        <EuiFlexItem grow={false}>
          <EuiButtonIcon
            iconType="refresh"
            onClick={loadTopTerms}
            aria-label="Actualizar términos populares"
            display="base"
          />
        </EuiFlexItem>
      </EuiFlexGroup>

      <EuiSpacer size="m" />

      <EuiFlexGroup direction="column" gutterSize="s">
        {topTerms.map((term, index) => (
          <EuiFlexItem key={term.term}>
            <EuiPanel color="subdued" paddingSize="m">
              <EuiFlexGroup alignItems="center" gutterSize="m">
                <EuiFlexItem grow={false}>
                  <EuiBadge color="primary" style={{ minWidth: '36px', textAlign: 'center' }}>
                    #{index + 1}
                  </EuiBadge>
                </EuiFlexItem>
                
                <EuiFlexItem>
                  <EuiFlexGroup direction="column" gutterSize="xs">
                    <EuiFlexItem>
                      <EuiText size="s">
                        <strong>{term.term}</strong>
                      </EuiText>
                    </EuiFlexItem>
                    <EuiFlexItem>
                      <EuiText size="xs" color="subdued">
                        {term.frequency.toLocaleString()} búsquedas
                      </EuiText>
                    </EuiFlexItem>
                  </EuiFlexGroup>
                </EuiFlexItem>

                <EuiFlexItem grow={2}>
                  <EuiProgress
                    value={term.frequency}
                    max={maxFrequency}
                    color="primary"
                    size="s"
                  />
                </EuiFlexItem>
              </EuiFlexGroup>
            </EuiPanel>
          </EuiFlexItem>
        ))}
      </EuiFlexGroup>
    </EuiPanel>
  )
}

export default TopTerms