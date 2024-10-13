package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.PaymentStatus;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PAYMENT_TABLE)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "method")
    private String method;

    @Column(name = "status")
    private PaymentStatus status;

}
