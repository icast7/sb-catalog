package com.ic.boot.cat.catalogwebapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class LocalizedCatalogDto {
    @Size(min=2)
    String language;

    @JsonProperty("products")
    private List<ProductDto> products;

    public LocalizedCatalogDto() {
    }

    public LocalizedCatalogDto(String language){
        this.language = language;
        this.products = new ArrayList<>();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
