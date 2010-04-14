package com.pyxis.petstore.domain.billing;

import javax.persistence.*;

@Entity  @Access(AccessType.FIELD) @Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PaymentMethod {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;
}
