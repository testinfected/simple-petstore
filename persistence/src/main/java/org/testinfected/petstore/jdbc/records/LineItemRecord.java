package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.jdbc.support.Column;
import org.testinfected.petstore.jdbc.support.Table;
import org.testinfected.petstore.order.LineItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Access.idOf;
import static org.testinfected.petstore.jdbc.Access.orderOf;

public class LineItemRecord extends AbstractRecord<LineItem> {

    private final Table<LineItem> lineItems = new Table<LineItem>("line_items", this);

    private final Column<Long> id = lineItems.LONG("id");
    private final Column<String> number = lineItems.STRING("item_number");
    private final Column<String> description = lineItems.STRING("item_description");
    private final Column<BigDecimal> unitPrice = lineItems.BIG_DECIMAL("item_unit_price");
    private final Column<Integer> quantity = lineItems.INT("quantity");
    private final Column<BigDecimal> totalPrice = lineItems.BIG_DECIMAL("total_price");
    private final Column<Long> order = lineItems.LONG("order_id");
    private final Column<Integer> line = lineItems.INT("order_line");

    public static Table<LineItem> buildTable() {
        return new LineItemRecord().lineItems;
    }

    @Override
    public LineItem hydrate(ResultSet rs) throws SQLException {
        LineItem lineItem = new LineItem(number.get(rs), description.get(rs), unitPrice.get(rs), quantity.get(rs), totalPrice.get(rs));
        idOf(lineItem).set(id.get(rs));
        return lineItem;
    }

    @Override
    public void dehydrate(PreparedStatement st, LineItem lineItem) throws SQLException {
        id.set(st, idOf(lineItem).get());
        number.set(st, lineItem.getItemNumber());
        unitPrice.set(st, lineItem.getItemUnitPrice());
        description.set(st, lineItem.getItemDescription());
        quantity.set(st, lineItem.getQuantity());
        totalPrice.set(st, lineItem.getTotalPrice());
        order.set(st, idOf(orderOf(lineItem).get()).get());
        line.set(st, orderOf(lineItem).get().lineOf(lineItem));
    }
}
