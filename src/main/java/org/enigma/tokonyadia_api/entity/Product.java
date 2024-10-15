package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.constant.Constant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PRODUCT_TABLE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false, columnDefinition = "bigint check(price > 0)")
    private Long price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne()
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<ProductRating> productRating;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<ProductPromo> productPromo;

    // Auditing fields
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
