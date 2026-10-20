package mx.edu.utez.angel.products.controller;

import mx.edu.utez.angel.products.exception.GlobalExceptionHandler;
import mx.edu.utez.angel.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryValidationTest {

    private ProductService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service = mock(ProductService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createCategoryWithBlankNameReturnsBadRequestWithoutSaving() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "descripcion": "Categoría de prueba"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("nombre: El nombre de la categoría no puede estar vacío"));

        verifyNoInteractions(service);
    }
}
