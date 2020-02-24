package com.ic.boot.cat.catalogwebapp.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CATALOG")
public class Catalog {
    @Id
    @Column(name="CATALOG_NAME")
    private String name;

    @Column(name="COUNTRY")
    private String country;

    public Catalog(){
    }

    public Catalog(String name, String country){
        this.name = name;
        this.country = country;
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
}
