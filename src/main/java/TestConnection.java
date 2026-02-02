import db.DatabaseConnectionManager;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Connecting to DB...");
        try (Connection c = DatabaseConnectionManager.getConnection()) {
            System.out.println("DB CONNECTED ✅");
            System.out.println("AutoCommit=" + c.getAutoCommit());
        } catch (Exception e) {
            System.out.println("DB FAILED ❌: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

