package org.testinfected.petstore.jdbc;

public final class Tables {

    private Tables() {}

    public static Table products() {
        Table table = new Table("products");

        table.addColumn(Column.bigint("id"));
        table.addColumn(Column.varchar("number"));
        table.addColumn(Column.varchar("name"));
        table.addColumn(Column.varchar("description"));
        table.addColumn(Column.varchar("photo_file_name"));

        return table;
    }

}
