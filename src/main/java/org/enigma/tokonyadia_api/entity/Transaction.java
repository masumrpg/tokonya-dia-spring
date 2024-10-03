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
@Table(name = Constant.TRANSACTION_TABLE)
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customerId;

    @Column(name = "transaction_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime transactionDate;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}
