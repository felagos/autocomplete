package com.autocomplete.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutocompleteRequest {
    
    @NotBlank(message = "El prefijo no puede estar vacío")
    private String prefix;
    
    private Integer limit = 10;
}
