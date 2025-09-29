//package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {

    // 전체 재료 조회
    public static List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM Ingredient";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ingredient i = new Ingredient(
                        rs.getString("ingredientID"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiration_date").toLocalDate());
                list.add(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 재료 추가
    public static void addIngredient(Ingredient i) {
        String sql = "INSERT INTO Ingredient VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, i.getIgdID());
            stmt.setString(2, i.getIgdName());
            stmt.setInt(3, i.getIgdQuantity());
            stmt.setDate(4, Date.valueOf(i.getExpDate()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 유통기한 지난 재료 삭제
    public static void deleteExpiredIngredients() {
        String sql = "DELETE FROM Ingredient WHERE expiration_date < ?";

        try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
