package com.ic.boot.cat.catalogwebapp.service;

import com.ic.boot.cat.catalogwebapp.data.Catalog;
import com.ic.boot.cat.catalogwebapp.data.CatalogRepository;
import com.ic.boot.cat.catalogwebapp.data.Product;
import com.ic.boot.cat.catalogwebapp.data.ProductRepository;
import com.ic.boot.cat.catalogwebapp.domain.CatalogDto;
import com.ic.boot.cat.catalogwebapp.domain.LocalizedCatalogDto;
import com.ic.boot.cat.catalogwebapp.domain.ProductDto;
import com.ic.boot.cat.catalogwebapp.exception.CatalogAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CatalogServiceTests {
    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Iterable<Product>> productIterableCaptor;

    @Captor
    private ArgumentCaptor<Catalog> catalogCaptor;

    private CatalogService catalogService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        catalogService = new CatalogService(catalogRepository, productRepository);
    }

    @Test
    void createCatalogExists() {
        CatalogDto catalogDto = new CatalogDto("a-name", "US");

        try {
            when(catalogRepository.existsById(catalogDto.getName())).thenReturn(true);
            catalogService.createCatalog(catalogDto);
            fail("Should throw exception");
        } catch (Exception e) {
            assertTrue(e instanceof CatalogAlreadyExistsException);
        } finally {
            verify(catalogRepository, times(1)).existsById(catalogDto.getName());
        }
    }

    @Test
    void createCatalog() {
        CatalogDto catalogDto = new CatalogDto("a-name", "US");
        CatalogService spy = Mockito.spy(catalogService);

        when(catalogRepository.existsById(catalogDto.getName())).thenReturn(false);

        doReturn(catalogDto).when(spy).upsertCatalog(catalogDto);

        CatalogDto result = spy.createCatalog(catalogDto);

        assertEquals(catalogDto, result);

        verify(spy, times(1)).upsertCatalog(catalogDto);
        verify(catalogRepository, times(1)).existsById(catalogDto.getName());
    }

    @Test
    void updateCatalog() {
        CatalogDto catalogDto = new CatalogDto("a-name", "US");
        CatalogService spy = Mockito.spy(catalogService);

        doReturn(catalogDto).when(spy).upsertCatalog(catalogDto);

        spy.updateCatalog(catalogDto);

        verify(spy, times(1)).upsertCatalog(catalogDto);
    }

    @Test
    void upsertCatalog() {
        CatalogDto catalogDto = new CatalogDto("a-name", "US");

        LocalizedCatalogDto localizedCatalogDto = new LocalizedCatalogDto("en");
        ProductDto productDto1 = new ProductDto("name1", "desc1");
        ProductDto productDto2 = new ProductDto("name2", "desc2");

        List<ProductDto> productDtos = Arrays.asList(productDto1, productDto2);
        localizedCatalogDto.setProducts(productDtos);
        catalogDto.addLocalizedCatalog(localizedCatalogDto);

        // Returned values are irrelevant for the method and test
        when(catalogRepository.save(any(Catalog.class))).thenReturn(null);
        when(productRepository.saveAll(anyList())).thenReturn(null);

        catalogService.upsertCatalog(catalogDto);

        verify(catalogRepository, times(1)).save(catalogCaptor.capture());
        Catalog catalogCaptorValue = catalogCaptor.getValue();
        assertEquals(catalogDto.getName(), catalogCaptorValue.getName());
        assertEquals(catalogDto.getCountry(), catalogCaptorValue.getCountry());

        verify(productRepository, times(1)).saveAll(productIterableCaptor.capture());
        Iterable<Product> productIterableCaptorValue = productIterableCaptor.getValue();
        List<Product> result = new ArrayList<>();
        productIterableCaptorValue.forEach(result::add);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(productPredicate(productDto1)));
        assertTrue(result.stream().anyMatch(productPredicate(productDto2)));
    }

    private static Predicate<Product> productPredicate(ProductDto productDto) {
        return product -> product.getName().equals(productDto.getName()) &&
                product.getDescription().equals(productDto.getDescription());
    }

    @Test
    void deleteCatalog() {
        String catalogName = "a-name";

        doNothing().when(productRepository).deleteProductsByCatalog(catalogName);
        when(catalogRepository.existsById(catalogName)).thenReturn(true);
        doNothing().when(catalogRepository).deleteById(catalogName);

        catalogService.deleteCatalog(catalogName);

        verify(productRepository, times(1)).deleteProductsByCatalog(ArgumentMatchers.eq(catalogName));
        verify(catalogRepository, times(1)).existsById(ArgumentMatchers.eq(catalogName));
        verify(catalogRepository, times(1)).deleteById(ArgumentMatchers.eq(catalogName));
    }

    @Test
    void deleteCatalogNotExists() {
        String catalogName = "a-name";

        doNothing().when(productRepository).deleteProductsByCatalog(catalogName);
        when(catalogRepository.existsById(catalogName)).thenReturn(false);
        doNothing().when(catalogRepository).deleteById(catalogName);

        catalogService.deleteCatalog(catalogName);

        verify(productRepository, times(1)).deleteProductsByCatalog(ArgumentMatchers.eq(catalogName));
        verify(catalogRepository, times(1)).existsById(ArgumentMatchers.eq(catalogName));
        verify(catalogRepository, times(0)).deleteById(anyString());
    }
}
