package Methods;

import java.sql.*;

public class Metadata {
    private String url = "jdbc:postgresql://localhost:5432/BookStore";
    private String user = "postgres";
    private String password = "fidan123";

    public void displayTablesInfoWithKeys() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("Tables in the database:");
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayColumnsInfo(String tableName) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            System.out.println("\nColumns in table '" + tableName + "':");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                System.out.println(columnName + " - " + columnType);
            }

            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            System.out.println("\nPrimary keys in table '" + tableName + "':");
            while (primaryKeys.next()) {
                System.out.println(primaryKeys.getString("COLUMN_NAME"));
            }

            ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);
            System.out.println("\nForeign keys in table '" + tableName + "':");
            while (foreignKeys.next()) {
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                System.out.println(fkColumnName + " references " + pkTableName + "(" + pkColumnName + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
