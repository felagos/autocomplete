import { useState, useEffect, useRef, useDeferredValue } from 'react'
import {
  EuiFieldSearch,
  EuiSelectable,
  EuiText,
  EuiFlexGroup,
  EuiFlexItem,
  EuiBadge,
  EuiLoadingSpinner,
  EuiCallOut,
  EuiSpacer,
} from '@elastic/eui'
import { getSuggestions, submitTerm, SuggestionDTO } from '../services/api'

interface AutocompleteProps {
  onSelect?: (term: string) => void
}

interface SelectableOption {
  label: string
  data: SuggestionDTO
}

export function Autocomplete({ onSelect }: AutocompleteProps) {
  const [query, setQuery] = useState<string>('')
  const deferredQuery = useDeferredValue(query)
  const [suggestions, setSuggestions] = useState<SuggestionDTO[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [executionTime, setExecutionTime] = useState<number | null>(null)
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null)

  // Búsqueda con debouncing
  useEffect(() => {
    if (deferredQuery.length === 0) {
      setSuggestions([])
      return
    }

    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current)
    }

    debounceTimeout.current = setTimeout(async () => {
      setLoading(true)
      try {
        const response = await getSuggestions(deferredQuery)
        setSuggestions(response.suggestions)
        setExecutionTime(response.executionTimeMs)
      } catch (error) {
        console.error('Error al obtener sugerencias:', error)
        setSuggestions([])
      } finally {
        setLoading(false)
      }
    }, 300)

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current)
      }
    }
  }, [deferredQuery])

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value)
  }

  const handleSuggestionSelect = async (term: string) => {
    setQuery(term)
    setSuggestions([])
    
    try {
      await submitTerm(term)
      if (onSelect) {
        onSelect(term)
      }
    } catch (error) {
      console.error('Error al enviar término:', error)
    }
  }

  // Convertir sugerencias a formato de EuiSelectable
  const options: SelectableOption[] = suggestions.map((suggestion) => ({
    label: suggestion.term,
    data: suggestion,
  }))

  const renderOption = (option: SelectableOption) => {
    return (
      <EuiFlexGroup justifyContent="spaceBetween" alignItems="center" gutterSize="s">
        <EuiFlexItem grow={true}>
          <EuiText size="s">
            <strong>{option.data.term}</strong>
          </EuiText>
        </EuiFlexItem>
        <EuiFlexItem grow={false}>
          <EuiBadge color="hollow">
            {option.data.frequency.toLocaleString()} búsquedas
          </EuiBadge>
        </EuiFlexItem>
      </EuiFlexGroup>
    )
  }

  return (
    <div>
      <EuiFieldSearch
        placeholder="Escribe para buscar... (ej: java, react, python)"
        value={query}
        onChange={handleInputChange}
        isLoading={loading}
        fullWidth
        aria-label="Campo de búsqueda de autocompletado"
      />
      
      {executionTime !== null && query && (
        <>
          <EuiSpacer size="s" />
          <EuiText size="xs" color="subdued">
            {suggestions.length} sugerencia{suggestions.length !== 1 ? 's' : ''} • {executionTime}ms
          </EuiText>
        </>
      )}

      {query && suggestions.length > 0 && !loading && (
        <>
          <EuiSpacer size="s" />
          <EuiSelectable
            options={options}
            onChange={(newOptions) => {
              const selectedOption = newOptions.find((option) => option.checked === 'on')
              if (selectedOption) {
                handleSuggestionSelect(selectedOption.label)
              }
            }}
            singleSelection={true}
            listProps={{
              bordered: true,
            }}
            height={300}
            renderOption={renderOption}
          >
            {(list) => list}
          </EuiSelectable>
        </>
      )}

      {query && suggestions.length === 0 && !loading && (
        <>
          <EuiSpacer size="s" />
          <EuiCallOut
            title="Sin resultados"
            color="warning"
            iconType="search"
            size="s"
          >
            No se encontraron resultados para "{query}"
          </EuiCallOut>
        </>
      )}
    </div>
  )
}

export default Autocomplete