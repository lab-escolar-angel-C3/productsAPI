package mx.edu.utez.angel.products.service;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.angel.products.dto.ProductRequest;
import mx.edu.utez.angel.products.dto.ProductResponse;
import mx.edu.utez.angel.products.entity.Product;
import mx.edu.utez.angel.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

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

    // 5. Eliminar
    public boolean deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}