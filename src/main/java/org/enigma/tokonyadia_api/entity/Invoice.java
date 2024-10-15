package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.constant.Constant;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = Constant.INVOICE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "shipping_cost", nullable = false)
    private Double shippingCost;

    @Column(name = "purchase_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime purchaseDate;

    @Column(name = "shop_count", nullable = false)
    private Integer shopCount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceItem> invoiceItems;

    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now();
    }
}
