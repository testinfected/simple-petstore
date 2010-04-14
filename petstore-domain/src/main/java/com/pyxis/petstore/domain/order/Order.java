package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cascade;
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

    @OneToMany(cascade = CascadeType.ALL) @JoinColumn(name="order_id", nullable = false)
    @IndexColumn(name = "order_line", base=1)
    private List<LineItem> lines = new ArrayList<LineItem>();

    @ManyToOne(cascade = CascadeType.PERSIST) @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "payment_id")
    private CreditCardDetails creditCardDetails;

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

    public void markPaidWith(CreditCardDetails creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    public CreditCardDetails getCreditCardDetails() {
        return creditCardDetails;
    }

    public boolean isPaid() {
        return creditCardDetails != null;
    }

    public int getLineItemCount() {
        return lines.size();
    }
}
