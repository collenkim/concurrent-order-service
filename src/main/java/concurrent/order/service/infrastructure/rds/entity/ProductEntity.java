package concurrent.order.service.infrastructure.rds.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stocks", nullable = false)
    private Integer stockQuantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    private ProductEntity(String name, BigDecimal price, Integer stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static ProductEntity createProduct(String name, BigDecimal price, Integer stockQuantity){
        return new ProductEntity(name, price, stockQuantity);
    }

}
