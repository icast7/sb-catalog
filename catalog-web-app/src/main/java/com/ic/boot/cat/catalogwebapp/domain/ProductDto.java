package com.ic.boot.cat.catalogwebapp.domain;

import com.ic.boot.cat.catalogwebapp.data.Product;

import javax.validation.constraints.Size;

public class ProductDto {
    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String description;

    public ProductDto(Product product){
        this(product.getName(), product.getDescription());
    }

    private ProductDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
