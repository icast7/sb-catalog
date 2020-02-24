package com.ic.boot.cat.catalogwebapp.controller;

import com.ic.boot.cat.catalogwebapp.domain.CatalogDto;
import com.ic.boot.cat.catalogwebapp.exception.CatalogAlreadyExistsException;
import com.ic.boot.cat.catalogwebapp.exception.CatalogNotFoundException;
import com.ic.boot.cat.catalogwebapp.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogDto createCatalog(@RequestBody @Validated CatalogDto catalogDto) {
        try {
            return catalogService.createCatalog(catalogDto);
        } catch (CatalogAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Catalog already exists");
        }
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CatalogDto retrieveCatalog(@PathVariable("name") String name) {
        try {
            return catalogService.getCatalog(name);
        } catch (CatalogNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalog Not Found");
        }
    }

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCatalog(@RequestBody @Validated CatalogDto catalogDto) {
        catalogService.updateCatalog(catalogDto);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCatalog(@PathVariable("name") String name) {
        catalogService.deleteCatalog(name);
    }
}
