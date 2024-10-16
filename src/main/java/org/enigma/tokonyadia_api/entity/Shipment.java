package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.Courier;
import org.enigma.tokonyadia_api.constant.ShipmentStatus;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "courier_name", nullable = false)
    private Courier courierName;

    @Column(name = "receipt", nullable = false) // no resi
    private String receipt;

    @Column(name = "delivery_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveryDate;

    @Column(name = "delivery_from", nullable = false)
    private String deliveryFrom;

    @Column(name = "delivery_to", nullable = false)
    private String deliveryTo;

    @Column(name = "estimate_date", nullable = false)
    private LocalDateTime estimateDate;

    @Column(name = "status", nullable = false)
    private ShipmentStatus status;

    @PrePersist
    protected void onCreate() {
        this.deliveryDate = LocalDateTime.now();
    }
}
