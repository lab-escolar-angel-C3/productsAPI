package mx.edu.utez.angel.products.service;

import jakarta.persistence.EntityManager;
import mx.edu.utez.angel.products.entity.Product;
import mx.edu.utez.angel.products.exception.StockOperationException;
import mx.edu.utez.angel.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class StockOperationsTest {

    private ProductRepository repository;
    private ProductService service;
    private Product product;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        service = new ProductService(repository, mock(EntityManager.class));
        product = new Product(1L, "Producto", 3, 10.0);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
    }

    @Test
    void addStockIncreasesExistingStock() {
        assertEquals(8, service.addStock(1L, 5).orElseThrow().getStock());
    }

    @Test
    void removeStockRejectsQuantityGreaterThanAvailableStock() {
        assertThrows(StockOperationException.class, () -> service.removeStock(1L, 5));
        assertEquals(3, product.getStock());
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }
}
