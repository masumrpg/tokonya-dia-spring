package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.ORDER_TABLE)
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @Column(name = "order_success_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderSuccessDate;

    @OneToOne(mappedBy = "order", orphanRemoval = true)
    private Shipment shipment;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    public void setToSuccessDate() {
        this.orderSuccessDate = LocalDateTime.now();
    }
}
