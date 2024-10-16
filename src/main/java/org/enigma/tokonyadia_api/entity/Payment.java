package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.constant.Constant;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PAYMENT_TABLE)
@Builder
public class Payment extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "midtrans_code")
    private String midtransCode;

    @Column(name = "is_active")
    private Boolean isActive;
}
