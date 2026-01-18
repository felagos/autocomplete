import React, { useState, useEffect } from 'react'
import { getTopTerms, SuggestionDTO } from '../services/api'
import './TopTerms.css'
function TopTerms() {
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
      <div className="top-terms-container">
        <h2>🔥 Términos Más Populares</h2>
        <div className="loading">Cargando...</div>
      </div>
    )
  }
  if (error) {
    return (
      <div className="top-terms-container">
        <h2>🔥 Términos Más Populares</h2>
        <div className="error">{error}</div>
        <button onClick={loadTopTerms} className="reload-button">
          Reintentar
        </button>
      </div>
    )
  }
  return (
    <div className="top-terms-container">
      <div className="top-terms-header">
        <h2>🔥 Términos Más Populares</h2>
        <button onClick={loadTopTerms} className="refresh-button" title="Actualizar">
          🔄
        </button>
      </div>
      <div className="top-terms-list">
        {topTerms.map((term, index) => (
          <div key={term.term} className="top-term-item">
            <div className="term-rank">#{index + 1}</div>
            <div className="term-info">
              <div className="term-name">{term.term}</div>
              <div className="term-stats">
                {term.frequency.toLocaleString()} búsquedas
              </div>
            </div>
            <div className="term-bar">
              <div
                className="term-bar-fill"
                style={{
                  width: `${(term.frequency / topTerms[0].frequency) * 100}%`
                }}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
export default TopTerms