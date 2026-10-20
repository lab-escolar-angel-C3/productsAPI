package mx.edu.utez.angel.products.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.angel.products.dto.ProductRequest;
import mx.edu.utez.angel.products.dto.ProductResponse;
import mx.edu.utez.angel.products.entity.Category;
import mx.edu.utez.angel.products.entity.Product;
import mx.edu.utez.angel.products.exception.StockOperationException;
import mx.edu.utez.angel.products.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final EntityManager entityManager;

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getNombre(),
                product.getStock(),
                product.getPrecio()
        );
    }

    private Product mapRequestToEntity(ProductRequest request) {
        Product product = new Product();
        product.setNombre(request.getNombre());
        product.setStock(request.getStock());
        product.setPrecio(request.getPrecio());
        return product;
    }

    // 1. Obtener todos
    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. Obtener por ID
    public Optional<ProductResponse> getProductById(Long id) {
        return repository.findById(id).map(this::mapToResponse);
    }

    // 3. Guardar
    public ProductResponse saveProduct(ProductRequest request) {
        Product product = mapRequestToEntity(request);
        Product saved = repository.save(product);
        return mapToResponse(saved);
    }

    // 4. Actualizar
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(request.getNombre());
            existing.setStock(request.getStock());
            existing.setPrecio(request.getPrecio());
            return mapToResponse(repository.save(existing));
        });
    }

    public Optional<ProductResponse> addStock(Long id, Integer quantity) {
        return repository.findById(id).map(product -> {
            if (quantity > Integer.MAX_VALUE - product.getStock()) {
                throw new StockOperationException("La cantidad excede el máximo de stock permitido");
            }
            product.setStock(product.getStock() + quantity);
            return mapToResponse(repository.save(product));
        });
    }

    public Optional<ProductResponse> removeStock(Long id, Integer quantity) {
        return repository.findById(id).map(product -> {
            if (quantity > product.getStock()) {
                throw new StockOperationException("No hay suficiente stock para quitar esa cantidad");
            }
            product.setStock(product.getStock() - quantity);
            return mapToResponse(repository.save(product));
        });
    }

    // 5. Eliminar
    public boolean deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return entityManager.createQuery("SELECT c FROM Category c ORDER BY c.id", Category.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return Optional.ofNullable(entityManager.find(Category.class, id));
    }

    @Transactional
    public Category saveCategory(Category category) {
        category.setId(null);
        entityManager.persist(category);
        return category;
    }

    @Transactional
    public Optional<Category> updateCategory(Long id, Category category) {
        Category existing = entityManager.find(Category.class, id);
        if (existing == null) {
            return Optional.empty();
        }

        existing.setNombre(category.getNombre());
        existing.setDescripcion(category.getDescripcion());
        return Optional.of(existing);
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        Category category = entityManager.find(Category.class, id);
        if (category == null) {
            return false;
        }

        entityManager.remove(category);
        return true;
    }
}