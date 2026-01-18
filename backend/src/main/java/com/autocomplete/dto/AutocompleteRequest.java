package com.autocomplete.dto;
import jakarta.validation.constraints.NotBlank;
public class AutocompleteRequest {
    @NotBlank(message = "El prefijo no puede estar vacío")
    private String prefix;
    private Integer limit = 10;
    public AutocompleteRequest() {
    }
    public AutocompleteRequest(String prefix, Integer limit) {
        this.prefix = prefix;
        this.limit = limit;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutocompleteRequest that = (AutocompleteRequest) o;
        return java.util.Objects.equals(prefix, that.prefix) && java.util.Objects.equals(limit, that.limit);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(prefix, limit);
    }
    @Override
    public String toString() {
        return "AutocompleteRequest{" +
                "prefix='" + prefix + "'" +
                ", limit=" + limit +
                "}";
    }
}