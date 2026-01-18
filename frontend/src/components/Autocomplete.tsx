import React, { useState, useEffect, useRef } from 'react'
import { getSuggestions, submitTerm, SuggestionDTO } from '../services/api'
import './Autocomplete.css'
interface AutocompleteProps {
  onSelect?: (term: string) => void
}
function Autocomplete({ onSelect }: AutocompleteProps) {
  const [query, setQuery] = useState<string>('')
  const [suggestions, setSuggestions] = useState<SuggestionDTO[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [showSuggestions, setShowSuggestions] = useState<boolean>(false)
  const [selectedIndex, setSelectedIndex] = useState<number>(-1)
  const [executionTime, setExecutionTime] = useState<number | null>(null)
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null)
  const wrapperRef = useRef<HTMLDivElement>(null)
  // Cerrar sugerencias al hacer click fuera
  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (wrapperRef.current && !wrapperRef.current.contains(event.target as Node)) {
        setShowSuggestions(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])
  // Búsqueda con debouncing
  useEffect(() => {
    if (query.length === 0) {
      setSuggestions([])
      setShowSuggestions(false)
      return
    }
    // Clear previous timeout
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current)
    }
    // Set new timeout
    debounceTimeout.current = setTimeout(async () => {
      setLoading(true)
      try {
        const response = await getSuggestions(query)
        setSuggestions(response.suggestions)
        setExecutionTime(response.executionTimeMs)
        setShowSuggestions(true)
        setSelectedIndex(-1)
      } catch (error) {
        console.error('Error al obtener sugerencias:', error)
        setSuggestions([])
      } finally {
        setLoading(false)
      }
    }, 300) // 300ms debounce
    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current)
      }
    }
  }, [query])
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value)
  }
  const handleSuggestionClick = async (term: string) => {
    setQuery(term)
    setShowSuggestions(false)
    // Enviar término seleccionado al backend para incrementar frecuencia
    try {
      await submitTerm(term)
      if (onSelect) {
        onSelect(term)
      }
    } catch (error) {
      console.error('Error al enviar término:', error)
    }
  }
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (!showSuggestions || suggestions.length === 0) return
    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault()
        setSelectedIndex(prev =>
          prev < suggestions.length - 1 ? prev + 1 : prev
        )
        break
      case 'ArrowUp':
        e.preventDefault()
        setSelectedIndex(prev => prev > 0 ? prev - 1 : -1)
        break
      case 'Enter':
        e.preventDefault()
        if (selectedIndex >= 0) {
          handleSuggestionClick(suggestions[selectedIndex].term)
        }
        break
      case 'Escape':
        setShowSuggestions(false)
        setSelectedIndex(-1)
        break
      default:
        break
    }
  }
  return (
    <div className="autocomplete-wrapper" ref={wrapperRef}>
      <div className="input-container">
        <input
          type="text"
          className="autocomplete-input"
          placeholder="Escribe para buscar... (ej: java, react, python)"
          value={query}
          onChange={handleInputChange}
          onKeyDown={handleKeyDown}
          onFocus={() => query && setShowSuggestions(true)}
        />
        {loading && <div className="loading-spinner">🔄</div>}
      </div>
      {showSuggestions && suggestions.length > 0 && (
        <div className="suggestions-dropdown">
          <div className="suggestions-header">
            {suggestions.length} sugerencia{suggestions.length !== 1 ? 's' : ''}
            {executionTime !== null && (
              <span className="execution-time"> • {executionTime}ms</span>
            )}
          </div>
          <ul className="suggestions-list">
            {suggestions.map((suggestion, index) => (
              <li
                key={suggestion.term}
                className={`suggestion-item ${index === selectedIndex ? 'selected' : ''}`}
                onClick={() => handleSuggestionClick(suggestion.term)}
                onMouseEnter={() => setSelectedIndex(index)}
              >
                <span className="suggestion-term">{suggestion.term}</span>
                <span className="suggestion-frequency">
                  {suggestion.frequency.toLocaleString()} búsquedas
                </span>
              </li>
            ))}
          </ul>
        </div>
      )}
      {showSuggestions && suggestions.length === 0 && query && !loading && (
        <div className="suggestions-dropdown">
          <div className="no-results">
            No se encontraron resultados para "{query}"
          </div>
        </div>
      )}
    </div>
  )
}
export default Autocomplete