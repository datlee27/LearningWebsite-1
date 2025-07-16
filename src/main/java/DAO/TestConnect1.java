package dao;



import java.sql.Connection;

public class TestConnect1 {
    public static void main(String[] args) {
        try {
            DBConnection db = new DBConnection();
            Connection conn = db.getConnection();
            System.out.println("DB connection successful: " + (conn != null));
        } catch (Exception e) {
            System.out.println("DB connection failed:");
            e.printStackTrace();
        }
    }
}
