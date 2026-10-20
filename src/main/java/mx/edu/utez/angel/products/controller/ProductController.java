package mx.edu.utez.angel.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.angel.products.dto.AddStockRequest;
import mx.edu.utez.angel.products.dto.ProductRequest;
import mx.edu.utez.angel.products.dto.ProductResponse;
import mx.edu.utez.angel.products.dto.RemoveStockRequest;
import mx.edu.utez.angel.products.entity.Category;
import mx.edu.utez.angel.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse savedProduct = service.saveProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        return service.updateProduct(id, productRequest)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/products/{id}/stock/add")
    public ResponseEntity<?> addStock(
            @PathVariable Long id,
            @Valid @RequestBody AddStockRequest request) {
        return service.addStock(id, request.add())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/products/{id}/stock/remove")
    public ResponseEntity<?> removeStock(
            @PathVariable Long id,
            @Valid @RequestBody RemoveStockRequest request) {
        return service.removeStock(id, request.remove())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return service.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> saveCategory(@Valid @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveCategory(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        return service.updateCategory(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (service.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}