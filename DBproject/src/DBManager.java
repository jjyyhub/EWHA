import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/cake_ordering_system";
    private static final String USER = "root"; // 본인 MySQL 계정
    private static final String PASSWORD = "wjswldbs0817"; // 본인 비밀번호

    // 드라이버 로딩 (JDBC 4.0 이후 자동 로딩되지만, 명시해도 무방)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver 로드 실패");
            e.printStackTrace();
        }
    }

    /** DB 연결을 얻어오는 메서드 */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** 테스트용 main */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ DB 연결 성공!");
            }
        } catch (SQLException e) {
            System.err.println("❌ DB 연결 실패: " + e.getMessage());
        }
    }

}
