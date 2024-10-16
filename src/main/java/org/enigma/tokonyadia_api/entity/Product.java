package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.constant.Constant;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PRODUCT_TABLE)
@Builder
public class Product extends Auditable<String> {
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
}
