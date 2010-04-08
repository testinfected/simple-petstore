package com.pyxis.petstore.domain;

import org.hibernate.annotations.AccessType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity  @AccessType("field") @Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    private OrderNumber orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id", nullable = false)
    private List<LineItem> items = new ArrayList<LineItem>();

    @Embedded @AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
        @AttributeOverride(name = "emailAddress", column = @Column(name = "billing_email"))
    })
    private Account billingAccount;

    @Embedded @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "credit_card_type")),
        @AttributeOverride(name = "number", column = @Column(name = "credit_card_number")),
        @AttributeOverride(name = "expiryDate", column = @Column(name = "credit_card_expiry_date"))
    })
    private CreditCard creditCard;

    Order() {}

    public Order(OrderNumber orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getNumber() {
        return orderNumber.getNumber();
    }

    public void addItemsFromCart(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            items.add(LineItem.from(cartItem));
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : items) {
            total = total.add(lineItem.getTotalPrice());
        }
        return total;
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(items);
    }

    public void billedTo(Account billingAccount) {
        this.billingAccount = billingAccount;
    }

    public void paidWith(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isPaid() {
        return creditCard != null;
    }
}
