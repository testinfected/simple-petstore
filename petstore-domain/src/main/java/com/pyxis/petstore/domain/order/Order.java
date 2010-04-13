package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.Account;
import com.pyxis.petstore.domain.billing.CreditCard;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity  @AccessType("field") @Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

    private OrderNumber number;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id", nullable = false)
    @IndexColumn(name = "order_line", base=1)
    private List<LineItem> lines = new ArrayList<LineItem>();

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

    public Order(OrderNumber number) {
        this.number = number;
    }

    public String getNumber() {
        return number.getNumber();
    }

    public void addItemsFromCart(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            lines.add(LineItem.from(cartItem));
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : lines) {
            total = total.add(lineItem.getTotalPrice());
        }
        return total;
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(lines);
    }

    public void billedTo(Account billingAccount) {
        this.billingAccount = billingAccount;
    }

    public void paidWith(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    // todo billingAccount should be a component (or association) of credit card 
    public Account getBillingAccount() {
        return billingAccount;
    }

    // todo return a payment method (and for a first version, assume in view it is a credit card)
    public CreditCard getCreditCardDetails() {
        return creditCard;
    }

    public boolean isPaid() {
        return creditCard != null;
    }

    public int getLineItemCount() {
        return lines.size();
    }
}
