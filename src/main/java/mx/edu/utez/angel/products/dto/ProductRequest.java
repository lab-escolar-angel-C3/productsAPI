package mx.edu.utez.angel.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser un número negativo")
    private Integer stock;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser de al menos $1.00")
    private Double precio;
}