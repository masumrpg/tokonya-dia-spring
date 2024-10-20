package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.constant.Constant;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.SHIPMENT_TABLE)
@Builder
public class Shipment extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "delivery_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveryDate;

    @Column(name = "delivery_from", nullable = false)
    private String deliveryFrom;

    @Column(name = "delivery_to", nullable = false)
    private String deliveryTo;

    @PrePersist
    protected void onCreate() {
        this.deliveryDate = LocalDateTime.now();
    }
}
