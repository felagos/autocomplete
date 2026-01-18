import axios, { AxiosInstance } from 'axios'
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/autocomplete'
const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})
export interface SuggestionDTO {
  term: string
  frequency: number
}
export interface AutocompleteResponse {
  prefix: string
  suggestions: SuggestionDTO[]
  executionTimeMs: number
}
export interface FrequencyTerm {
  id: number
  term: string
  frequency: number
  lastUsed: string
  createdAt: string
}
export const getSuggestions = async (
  prefix: string,
  limit: number = 10
): Promise<AutocompleteResponse> => {
  try {
    const response = await api.get<AutocompleteResponse>('/suggest', {
      params: { prefix, limit }
    })
    return response.data
  } catch (error) {
    console.error('Error al obtener sugerencias:', error)
    throw error
  }
}
export const submitTerm = async (term: string): Promise<FrequencyTerm> => {
  try {
    const response = await api.post<FrequencyTerm>('/submit', { term })
    return response.data
  } catch (error) {
    console.error('Error al enviar término:', error)
    throw error
  }
}
export const getTopTerms = async (limit: number = 10): Promise<SuggestionDTO[]> => {
  try {
    const response = await api.get<SuggestionDTO[]>('/top', {
      params: { limit }
    })
    return response.data
  } catch (error) {
    console.error('Error al obtener términos populares:', error)
    throw error
  }
}
export const initializeData = async (): Promise<string> => {
  try {
    const response = await api.post<string>('/init')
    return response.data
  } catch (error) {
    console.error('Error al inicializar datos:', error)
    throw error
  }
}
export default api