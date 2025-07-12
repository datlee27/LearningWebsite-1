package DAO;



import DAO.DBcontext;
import java.sql.Connection;

public class TestConnect1 {
    public static void main(String[] args) {
        try {
            DBcontext db = new DBcontext();
            Connection conn = db.getConnection();
            System.out.println("✅ Kết nối DB thành công!");
        } catch (Exception e) {
            System.out.println("❌ Kết nối DB thất bại:");
            e.printStackTrace();
        }
    }
}
