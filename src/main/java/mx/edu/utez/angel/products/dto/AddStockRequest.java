package mx.edu.utez.angel.products.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddStockRequest(
        @NotNull(message = "La cantidad para agregar es obligatoria")
        @Positive(message = "La cantidad para agregar debe ser mayor que cero")
        Integer add) {
}
