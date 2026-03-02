import { useState } from 'react'
import {
  Container,
  Box,
  Typography,
  Card,
  CardContent,
  Stack,
  Alert,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import Autocomplete from './components/Autocomplete'
import TopTerms from './components/TopTerms'

function App() {
  const [selectedTerm, setSelectedTerm] = useState<string>('')

  return (
    <Box
      sx={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        minHeight: '100vh',
        paddingTop: '40px',
        paddingBottom: '40px',
      }}
    >
      <Container maxWidth="lg">
        <Stack direction="column" alignItems="center" spacing={4}>
          {/* Header */}
          <Stack direction="column" alignItems="center" spacing={1}>
            <Stack direction="row" alignItems="center" spacing={2}>
              <SearchIcon sx={{ fontSize: '2rem', color: 'white' }} />
              <Typography
                variant="h1"
                component="h1"
                sx={{ color: 'white', margin: 0, fontSize: '2.5rem' }}
              >
                Sistema de Autocompletado
              </Typography>
            </Stack>
            <Typography
              variant="subtitle1"
              sx={{ color: 'rgba(255, 255, 255, 0.9)', margin: 0 }}
            >
              Implementado con conceptos de System Design Interview
            </Typography>
          </Stack>

          {/* Autocomplete Section */}
          <Box sx={{ width: '100%', maxWidth: '800px' }}>
            <Card>
              <CardContent>
                <Stack direction="column" spacing={2}>
                  <Typography variant="h5">Búsqueda con Autocompletado</Typography>
                  <Autocomplete onSelect={setSelectedTerm} />
                  {selectedTerm && (
                    <Alert severity="success">
                      <Typography variant="body2">
                        Término seleccionado: <strong>{selectedTerm}</strong>
                      </Typography>
                    </Alert>
                  )}
                </Stack>
              </CardContent>
            </Card>
          </Box>

          {/* Top Terms Section */}
          <Box sx={{ width: '100%', maxWidth: '800px' }}>
            <TopTerms />
          </Box>

          {/* Features Section */}
          <Box sx={{ width: '100%', maxWidth: '800px' }}>
            <Card>
              <CardContent>
                <Stack direction="column" spacing={2}>
                  <Typography variant="h5">Características del Sistema</Typography>
                  <Typography variant="body2" component="div">
                    <ul style={{ listStyle: 'none', paddingLeft: 0 }}>
                      <li>✨ Búsqueda en tiempo real mientras escribes</li>
                      <li>📊 Ranking por frecuencia de uso</li>
                      <li>⚡ Cache en backend para optimizar rendimiento</li>
                      <li>🎯 Actualización dinámica de frecuencias</li>
                      <li>💾 Persistencia en base de datos con JPA</li>
                      <li>🐳 Despliegue con Docker Compose</li>
                    </ul>
                  </Typography>
                </Stack>
              </CardContent>
            </Card>
          </Box>

          {/* Footer */}
          <Typography
            variant="caption"
            sx={{ color: 'rgba(255, 255, 255, 0.8)', textAlign: 'center' }}
          >
            Backend: Spring Boot + Gradle + JPA | Frontend: React 19 +
            TypeScript + Vite + Material-UI
          </Typography>
        </Stack>
      </Container>
    </Box>
  )
}

export default App