//package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    // 고객 조회
    public static Customer findById(String id) {
        String sql = "SELECT * FROM Customer WHERE customerID = ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("customerID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getDate("last_order_date") != null ? rs.getDate("last_order_date").toLocalDate() : null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 여기 추가//
    // 특정 고객 조회
    public static List<PaymentInfo> findCustomerByPay(String payment_method) {

        String sql = "SELECT customerID, total_price, payment_method FROM ReservationInfo WHERE payment_method = ?";

        List<PaymentInfo> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment_method);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(new PaymentInfo(
                            rs.getString("customerID"),
                            rs.getInt("total_price"),
                            rs.getString("payment_method")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 고객 추가
    public static void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (customerID, name, phone, last_order_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerID());
            stmt.setString(2, customer.getCustomerName());
            stmt.setString(3, customer.getCustomerPhone());

            LocalDate lastOrder = customer.getLastOrderDate();
            if (lastOrder != null) {
                stmt.setDate(4, Date.valueOf(lastOrder));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
