package org.testinfected.petstore.db.records;

import com.vtence.tape.Column;
import org.testinfected.petstore.order.LineItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.db.Access.idOf;
import static org.testinfected.petstore.db.Access.orderOf;

public class LineItemRecord extends AbstractRecord<LineItem> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<String> description;
    private final Column<BigDecimal> unitPrice;
    private final Column<Integer> quantity;
    private final Column<BigDecimal> totalPrice;
    private final Column<Long> orderId;
    private final Column<Integer> line;

    public LineItemRecord(Column<Long> id,
                          Column<String> itemNumber,
                          Column<String> itemDescription,
                          Column<BigDecimal> itemUnitPrice,
                          Column<Integer> quantity,
                          Column<BigDecimal> totalPrice,
                          Column<Long> orderId,
                          Column<Integer> orderLine) {
        this.id = id;
        this.number = itemNumber;
        this.description = itemDescription;
        this.unitPrice = itemUnitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.line = orderLine;
    }

    public LineItem hydrate(ResultSet rs) throws SQLException {
        LineItem lineItem = new LineItem(number.get(rs), description.get(rs), unitPrice.get(rs), quantity.get(rs), totalPrice.get(rs));
        idOf(lineItem).set(id.get(rs));
        return lineItem;
    }

    public void dehydrate(PreparedStatement st, LineItem lineItem) throws SQLException {
        id.set(st, idOf(lineItem).get());
        number.set(st, lineItem.getItemNumber());
        unitPrice.set(st, lineItem.getItemUnitPrice());
        description.set(st, lineItem.getItemDescription());
        quantity.set(st, lineItem.getQuantity());
        totalPrice.set(st, lineItem.getTotalPrice());
        orderId.set(st, idOf(orderOf(lineItem).get()).get());
        line.set(st, orderOf(lineItem).get().lineOf(lineItem));
    }
}
