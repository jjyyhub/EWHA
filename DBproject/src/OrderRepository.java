//package database;

import java.sql.*;
import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

public class OrderRepository {

    // 주문 추가
    public static void addOrder(Order order) {
        String sql = "INSERT INTO Reservation VALUES (?, CURRENT_DATE(), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getOrderID());
            stmt.setDate(2, Date.valueOf(order.getPickupDate()));
            stmt.setString(3, order.getCake().getCakeType());
            stmt.setString(4, order.getCake().getSize());
            stmt.setInt(5, order.getQuantity());
            stmt.setInt(6, order.getTotalPrice());
            stmt.setString(7, order.getCustomerID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 여기 추가//
    public static OrderInfo getOrderInformation(String resID) {
        String sql = "SELECT c.name AS customerName, r.res_date, r.pickup_date, r.quantity, r.cake_type, r.total_price, r.payment_method FROM ReservationInfo r JOIN Customer c ON r.customerID = c.customerID WHERE r.resID = ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resID);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new OrderInfo(
                            rs.getString("customerName"),
                            rs.getDate("res_date").toLocalDate(),
                            rs.getDate("pickup_date").toLocalDate(),
                            rs.getInt("quantity"),
                            rs.getString("cake_type"),
                            rs.getInt("total_price"),
                            rs.getString("payment_method"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 여기 추가//
    // 예약 취소
    public static boolean cancelOrder(String orderID) throws SQLException {
        String findSql = "SELECT * FROM Reservation WHERE resID = ?";
        String deletePay = "DELETE FROM Payment     WHERE resID = ?";
        String deleteRes = "DELETE FROM Reservation  WHERE resID = ?";
        String updStock = "UPDATE Cake SET quantity = quantity + ? WHERE cake_type = ? AND size = ?";

        try (Connection conn = DBManager.getConnection()) {
            conn.setAutoCommit(false);

            // 1) 예약에서 필요한 정보만 조회
            String cakeType;
            String size;
            int qty;
            try (PreparedStatement ps = conn.prepareStatement(findSql)) {
                ps.setString(1, orderID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false; // 예약 자체가 없음
                    }
                    cakeType = rs.getString("cake_type");
                    size = rs.getString("size");
                    qty = rs.getInt("quantity");
                }
            }

            // 2) 결제 삭제
            try (PreparedStatement ps = conn.prepareStatement(deletePay)) {
                ps.setString(1, orderID);
                ps.executeUpdate();
            }

            // 3) 예약 삭제
            try (PreparedStatement ps = conn.prepareStatement(deleteRes)) {
                ps.setString(1, orderID);
                ps.executeUpdate();
            }

            // 4) 케이크 재고 복구
            try (PreparedStatement ps = conn.prepareStatement(updStock)) {
                ps.setInt(1, qty);
                ps.setString(2, cakeType);
                ps.setString(3, size);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        }
    }

    // 여기 추가//
    // 예약 수정 - 종류 + 사이즈
    public static boolean updateCakeAndSize(String resID, String cakeType, String size, int totalPrice) {
        String sql = """
                    UPDATE Reservation
                       SET cake_type    = ?,
                           size         = ?,
                           total_price  = ?
                     WHERE resID = ?
                """;
        try (Connection conn = DBManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            ps.setString(1, cakeType);
            ps.setString(2, size);
            ps.setInt(3, totalPrice);
            ps.setString(4, resID);
            int updated = ps.executeUpdate();

            conn.commit();
            return (updated == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DBManager.getConnection().rollback();
            } catch (Exception ex) {
            }
            return false;
        }
    }

    // 여기 추가//
    // 예약 수정 - 수량 + 총 금액
    public static boolean updateQuantity(String resID, int quantity, int totalPrice) {
        String sql = """
                    UPDATE Reservation
                       SET quantity    = ?,
                           total_price = ?
                     WHERE resID = ?
                """;
        try (Connection conn = DBManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            ps.setInt(1, quantity);
            ps.setInt(2, totalPrice);
            ps.setString(3, resID);
            int updated = ps.executeUpdate();

            conn.commit();
            return (updated == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DBManager.getConnection().rollback();
            } catch (Exception ex) {
            }
            return false;
        }
    }

    // 여기 추가//
    // 예약 수정 - 픽업 날짜
    public static boolean updatePickupDate(String resID, LocalDate pickupDate) {
        String sql = """
                    UPDATE Reservation
                       SET pickup_date = ?
                     WHERE resID = ?
                """;
        try (Connection conn = DBManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            ps.setDate(1, Date.valueOf(pickupDate));
            ps.setString(2, resID);
            int updated = ps.executeUpdate();

            conn.commit();
            return (updated == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DBManager.getConnection().rollback();
            } catch (Exception ex) {
            }
            return false;
        }
    }

    // 결제 저장
    public static void addPayment(Payment payment) {
        String sql = "INSERT INTO Payment (paymentID, payment_method, resID) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getPaymentID());
            stmt.setString(2, payment.getPaymentMethod());
            stmt.setString(3, payment.getOrderID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 여기 추가//
    // 월 별 총 주문액 합계
    public static void MonthlyPayment(int year) {
        String sql = """
                    SELECT MONTH(res_date) AS month,
                           COUNT(*)         AS cnt,
                           SUM(total_price) AS total
                      FROM Reservation
                     WHERE YEAR(res_date) = ?
                     GROUP BY MONTH(res_date)
                     ORDER BY month
                """;

        try (Connection conn = DBManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\n" + year + "년 월별 정산 결과:");
                while (rs.next()) {
                    int m = rs.getInt("month");
                    int cnt = rs.getInt("cnt");
                    int tot = rs.getInt("total");
                    System.out.printf("%2d월: 예약 %d건, 매출 %,d원%n", m, cnt, tot);
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.err.println("월별 정산 중 오류 발생:");
            e.printStackTrace();
        }
    }

}
