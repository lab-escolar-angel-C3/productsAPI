package mx.edu.utez.angel.products.controller;

import mx.edu.utez.angel.products.dto.ProductResponse;
import mx.edu.utez.angel.products.exception.GlobalExceptionHandler;
import mx.edu.utez.angel.products.exception.StockOperationException;
import mx.edu.utez.angel.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductStockControllerTest {

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
    void addStockEndpointAcceptsAddJsonAndReturnsUpdatedProduct() throws Exception {
        when(service.addStock(1L, 5))
                .thenReturn(Optional.of(new ProductResponse(1L, "Producto", 8, 10.0)));

        mockMvc.perform(post("/api/products/1/stock/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"add": 5}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stock").value(8));
    }

    @Test
    void removeStockEndpointAcceptsRemoveJsonAndReturnsUpdatedProduct() throws Exception {
        when(service.removeStock(1L, 2))
                .thenReturn(Optional.of(new ProductResponse(1L, "Producto", 1, 10.0)));

        mockMvc.perform(post("/api/products/1/stock/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"remove": 2}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stock").value(1));
    }

    @Test
    void addStockEndpointRejectsNonPositiveQuantity() throws Exception {
        mockMvc.perform(post("/api/products/1/stock/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"add": 0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("add: La cantidad para agregar debe ser mayor que cero"));

        verifyNoInteractions(service);
    }

    @Test
    void removeStockEndpointRejectsQuantityGreaterThanAvailableStock() throws Exception {
        when(service.removeStock(1L, 5))
                .thenThrow(new StockOperationException("No hay suficiente stock para quitar esa cantidad"));

        mockMvc.perform(post("/api/products/1/stock/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"remove": 5}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No hay suficiente stock para quitar esa cantidad"));
    }
}
