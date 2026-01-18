package com.autocomplete.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermSubmitRequest {
    
    @NotBlank(message = "El término no puede estar vacío")
    @Size(min = 1, max = 255, message = "El término debe tener entre 1 y 255 caracteres")
    private String term;
}
