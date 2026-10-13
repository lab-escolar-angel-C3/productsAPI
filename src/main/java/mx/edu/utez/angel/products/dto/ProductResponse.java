package mx.edu.utez.angel.products.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String nombre;
    private Integer stock;
    private Double precio;
}