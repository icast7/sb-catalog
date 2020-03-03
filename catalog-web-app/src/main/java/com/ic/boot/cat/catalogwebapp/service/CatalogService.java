package com.ic.boot.cat.catalogwebapp.service;

import com.ic.boot.cat.catalogwebapp.data.Catalog;
import com.ic.boot.cat.catalogwebapp.data.CatalogRepository;
import com.ic.boot.cat.catalogwebapp.data.Product;
import com.ic.boot.cat.catalogwebapp.data.ProductRepository;
import com.ic.boot.cat.catalogwebapp.domain.CatalogDto;
import com.ic.boot.cat.catalogwebapp.domain.LocalizedCatalogDto;
import com.ic.boot.cat.catalogwebapp.domain.ProductDto;
import com.ic.boot.cat.catalogwebapp.exception.CatalogAlreadyExistsException;
import com.ic.boot.cat.catalogwebapp.exception.CatalogNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatalogService {
    private CatalogRepository catalogRepository;
    private ProductRepository productRepository;

    @Autowired
    public CatalogService(CatalogRepository catalogRepository, ProductRepository productRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
    }

    public CatalogDto createCatalog(CatalogDto catalogDto) {
        // Handle existing catalog
        if (catalogRepository.existsById(catalogDto.getName())) {
            throw new CatalogAlreadyExistsException();
        } else {
            return upsertCatalog(catalogDto);
        }
    }

    public void updateCatalog(CatalogDto catalogDto) {
        // Update or create if it doesn't exist
        upsertCatalog(catalogDto);
    }

    CatalogDto upsertCatalog(CatalogDto catalogDto) {
        // Upsert catalog
        Catalog catalog = new Catalog(catalogDto.getName(), catalogDto.getCountry());
        catalogRepository.save(catalog);
        // Create new products
        List<Product> catalogProducts = new ArrayList<>();
        catalogDto.getLocalizedCatalogs()
                .forEach(c -> {
                    // Collect products by language
                    List<Product> localizedProducts = c.getProducts().stream()
                            .map(p -> new Product(p.getName(), p.getDescription(), c.getLanguage(), catalog.getCountry(), catalog.getName()))
                            .collect(Collectors.toList());
                    catalogProducts.addAll(localizedProducts);
                });
        productRepository.saveAll(catalogProducts);
        return catalogDto;
    }

    public CatalogDto getCatalog(String name) {
        // Get catalog
        Optional<Catalog> catalogOpt = catalogRepository.findById(name);
        if (catalogOpt.isEmpty()) {
            throw new CatalogNotFoundException();
        } else {
            Catalog catalog = catalogOpt.get();
            CatalogDto catalogDto = new CatalogDto(catalog.getName(), catalog.getCountry());

            // Get all products by catalog
            Collection<Product> productsByCatalog = productRepository.findProductsByCatalog(name);

            // Group products by language
            Map<String, List<Product>> productsByLanguage = productsByCatalog.stream()
                    .collect(Collectors.groupingBy(Product::getLanguage));
            // Create DTO list by language
            productsByLanguage.entrySet().forEach(e -> {
                LocalizedCatalogDto localizedCatalogDto = new LocalizedCatalogDto(e.getKey());
                List<ProductDto> products = e.getValue().stream().map(ProductDto::new).collect(Collectors.toList());
                localizedCatalogDto.setProducts(products);
                catalogDto.addLocalizedCatalog(localizedCatalogDto);
            });
            return catalogDto;
        }
    }

    public void deleteCatalog(String name) {
        // Delete products
        productRepository.deleteProductsByCatalog(name);
        // Delete catalog
        if (catalogRepository.existsById(name)) {
            catalogRepository.deleteById(name);
        }
    }
}
