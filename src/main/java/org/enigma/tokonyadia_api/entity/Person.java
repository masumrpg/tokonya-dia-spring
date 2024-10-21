package org.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.enigma.tokonyadia_api.audit.Auditable;
import org.enigma.tokonyadia_api.constant.Constant;
import org.enigma.tokonyadia_api.constant.Gender;

import java.util.List;

// TODO add image file
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constant.PERSON_TABLE)
@Builder
public class Person extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "address")
    private String address;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @OneToOne(mappedBy = "person", orphanRemoval = true)
    private Store store;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<ProductRating> productRatings;
}
