import { useState, useEffect } from 'react'
import {
  Card,
  CardContent,
  Typography,
  Box,
  Chip,
  LinearProgress,
  CircularProgress,
  Alert,
  Button,
  Stack,
  IconButton,
} from '@mui/material'
import RefreshIcon from '@mui/icons-material/Refresh'
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
      <Card>
        <CardContent>
          <Stack direction="column" alignItems="center" spacing={2}>
            <Typography variant="h5">🔥 Términos Más Populares</Typography>
            <CircularProgress />
          </Stack>
        </CardContent>
      </Card>
    )
  }

  if (error) {
    return (
      <Card>
        <CardContent>
          <Typography variant="h5" sx={{ mb: 2 }}>
            🔥 Términos Más Populares
          </Typography>
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
          <Button variant="contained" onClick={loadTopTerms}>
            Reintentar
          </Button>
        </CardContent>
      </Card>
    )
  }

  const maxFrequency = topTerms[0]?.frequency || 1

  return (
    <Card>
      <CardContent>
        <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 2 }}>
          <Typography variant="h5" sx={{ m: 0 }}>
            🔥 Términos Más Populares
          </Typography>
          <IconButton
            onClick={loadTopTerms}
            size="small"
            aria-label="Actualizar términos populares"
          >
            <RefreshIcon />
          </IconButton>
        </Stack>

        <Stack direction="column" spacing={1}>
          {topTerms.map((term, index) => (
            <Box
              key={term.term}
              sx={{
                backgroundColor: 'background.paper',
                border: '1px solid',
                borderColor: 'divider',
                borderRadius: 1,
                padding: 1.5,
              }}
            >
              <Stack direction="row" alignItems="center" spacing={2}>
                <Chip
                  label={`#${index + 1}`}
                  color="primary"
                  size="small"
                  sx={{ minWidth: '40px', justifyContent: 'center' }}
                />

                <Box sx={{ flex: 1 }}>
                  <Typography variant="body2">
                    <strong>{term.term}</strong>
                  </Typography>
                  <Typography variant="caption" color="textSecondary">
                    {term.frequency.toLocaleString()} búsquedas
                  </Typography>
                </Box>

                <Box sx={{ flex: 2, minWidth: '100px' }}>
                  <LinearProgress
                    variant="determinate"
                    value={(term.frequency / maxFrequency) * 100}
                  />
                </Box>
              </Stack>
            </Box>
          ))}
        </Stack>
      </CardContent>
    </Card>
  )
}

export default TopTerms