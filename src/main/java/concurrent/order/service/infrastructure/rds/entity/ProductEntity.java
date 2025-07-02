package concurrent.order.service.infrastructure.rds.entity;

import concurrent.order.service.cd.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    private ProductEntity(String productId, String categoryId, String name, String description, ProductStatus status, Integer stockQuantity, BigDecimal price) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    public static ProductEntity createProductEntity(String productId, String categoryId, String name, String description, ProductStatus status, Integer stockQuantity, BigDecimal price){
        return new ProductEntity(productId, categoryId, name, description, status, stockQuantity, price);
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

}
