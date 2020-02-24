package com.ic.boot.cat.catalogwebapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class CatalogDto {
    @Size(min=3, max = 50)
    private String name;

    @Size(min=2, max = 2)
    private String country;

    @JsonProperty("localizedCatalogs")
    private List<LocalizedCatalogDto> localizedCatalogs;

    public CatalogDto(String name, String country) {
        this.name = name;
        this.country = country;
        this.localizedCatalogs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<LocalizedCatalogDto> getLocalizedCatalogs() {
        return localizedCatalogs;
    }

    public void setLocalizedCatalogs(List<LocalizedCatalogDto> localizedCatalogs) {
        this.localizedCatalogs = localizedCatalogs;
    }

    public void addLocalizedCatalog(LocalizedCatalogDto localizedCatalog){
        this.localizedCatalogs.add(localizedCatalog);
    }
}
