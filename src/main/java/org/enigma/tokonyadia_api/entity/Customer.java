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

// TODO change entity to person
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.CUSTOMER_TABLE)
@Builder
@EntityListeners(AuditingEntityListener.class) // Mengaktifkan listener auditing
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "phone_number", unique = true, nullable = false,length = 15)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "address")
    private String address;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @OneToOne(mappedBy = "customer", orphanRemoval = true)
    private Store stores;

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private List<ProductRating> productRatings;

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
