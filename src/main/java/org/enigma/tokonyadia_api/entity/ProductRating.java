package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.constant.Constant;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PRODUCT_RATING_TABLE)
@Builder
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "review")
    private String review;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @PrePersist()
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
