package com.ic.boot.cat.catalogwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ic.boot.cat.catalogwebapp.domain.CatalogDto;
import com.ic.boot.cat.catalogwebapp.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class CatalogControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @Test
    void testPutCatalog() throws Exception {
        CatalogDto catalogDto = new CatalogDto("a-name", "US");
        String catalodDtoJson = new ObjectMapper().writeValueAsString(catalogDto);
        doNothing().when(catalogService).updateCatalog(any(CatalogDto.class));

        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/v1/catalog/newshoes")
                        .content(catalodDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void testDeleteCatalog() throws Exception {
        doNothing().when(catalogService).deleteCatalog(anyString());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/catalog/newshoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
