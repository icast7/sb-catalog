package com.ic.boot.cat.catalogwebapp.service;

import com.ic.boot.cat.catalogwebapp.data.CatalogRepository;
import com.ic.boot.cat.catalogwebapp.data.ProductRepository;
import com.ic.boot.cat.catalogwebapp.domain.CatalogDto;
import com.ic.boot.cat.catalogwebapp.exception.CatalogAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
