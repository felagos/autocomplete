import { useState, useRef, ChangeEvent, useCallback } from 'react'
import {
  TextField,
  Typography,
  Box,
  Paper,
  CircularProgress,
  Alert,
  Chip,
  Stack,
} from '@mui/material'
import { getSuggestions, submitTerm, SuggestionDTO } from '../services/api'

interface AutocompleteProps {
  onSelect?: (term: string) => void
}

export function Autocomplete({ onSelect }: AutocompleteProps) {
  const [queryState, setQueryState] = useState<string>('')
  const [suggestions, setSuggestions] = useState<SuggestionDTO[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [executionTime, setExecutionTime] = useState<number | null>(null)
  const debounceTimeout = useRef<ReturnType<typeof setTimeout> | null>(null)
  const queryRef = useRef<string>('')

  const handleSuggestionSelect = useCallback(async (term: string) => {
    setQueryState(term)
    setSuggestions([])
    queryRef.current = term

    try {
      await submitTerm(term)
      if (onSelect) {
        onSelect(term)
      }
    } catch (error) {
      console.error('Error al enviar término:', error)
    }
  }, [onSelect])

  const handleInputChange = useCallback(
    async (e: ChangeEvent<HTMLInputElement>) => {
      const newQuery = e.target.value
      setQueryState(newQuery)
      queryRef.current = newQuery

      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current)
      }

      if (newQuery.length === 0) {
        setSuggestions([])
        return
      }

      debounceTimeout.current = setTimeout(async () => {
        setLoading(true)
        try {
          const response = await getSuggestions(newQuery)
          setSuggestions(response.suggestions)
          setExecutionTime(response.executionTimeMs)
        } catch (error) {
          console.error('Error al obtener sugerencias:', error)
          setSuggestions([])
        } finally {
          setLoading(false)
        }
      }, 300)
    },
    []
  )

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent<HTMLInputElement>) => {
      if (e.key === 'Enter' && queryRef.current.trim()) {
        e.preventDefault()
        handleSuggestionSelect(queryRef.current.trim())
      }
    },
    [handleSuggestionSelect]
  )

  const handleSearch = useCallback(() => {
    if (queryRef.current.trim()) {
      handleSuggestionSelect(queryRef.current.trim())
    }
  }, [handleSuggestionSelect])

  return (
    <Box sx={{ width: '100%', position: 'relative' }}>
      <TextField
        fullWidth
        placeholder="Escribe para buscar... (ej: java, react, python)"
        value={queryState}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        disabled={loading}
        inputProps={{
          'aria-label': 'Campo de búsqueda de autocompletado',
        }}
        slotProps={{
          input: {
            endAdornment: loading ? (
              <CircularProgress color="inherit" size={20} />
            ) : null,
          },
        }}
      />

      {executionTime !== null && queryState && (
        <Typography variant="caption" sx={{ display: 'block', mt: 1, color: 'text.secondary' }}>
          {suggestions.length} sugerencia
          {suggestions.length !== 1 ? 's' : ''} • {executionTime}ms
        </Typography>
      )}

      {queryState && suggestions.length > 0 && !loading && (
        <Paper
          sx={{
            position: 'absolute',
            top: '100%',
            left: 0,
            right: 0,
            maxHeight: '300px',
            overflowY: 'auto',
            marginTop: '8px',
            zIndex: 1000,
          }}
        >
          {suggestions.map((suggestion) => (
            <Box
              key={suggestion.term}
              onClick={() => handleSuggestionSelect(suggestion.term)}
              sx={{
                padding: '12px 16px',
                borderBottom: '1px solid #f0f0f0',
                cursor: 'pointer',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                transition: 'background-color 0.2s',
                '&:hover': {
                  backgroundColor: '#f9f9f9',
                },
                '&:last-child': {
                  borderBottom: 'none',
                },
              }}
            >
              <Typography variant="body2">
                <strong>{suggestion.term}</strong>
              </Typography>
              <Chip
                label={`${suggestion.frequency?.toLocaleString()} búsquedas`}
                size="small"
                color="primary"
                variant="outlined"
              />
            </Box>
          ))}
        </Paper>
      )}

      {queryState && suggestions.length === 0 && !loading && (
        <Alert severity="warning" sx={{ mt: 1 }}>
          No se encontraron resultados para "{queryState}"
        </Alert>
      )}
    </Box>
  )
}

export default Autocomplete