//package database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CakeRepository {

	public static void addCake(Cake cake) {
		String sql = "INSERT INTO Cake (cakeID, cake_type, price, size, made_date, ingredientID, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, cake.getCakeID());
			stmt.setString(2, cake.getCakeType());
			stmt.setInt(3, cake.getPrice());
			stmt.setString(4, cake.getSize());
			stmt.setDate(5, Date.valueOf(cake.getMade_date()));
			stmt.setString(6, cake.getIngredientID());
			stmt.setInt(7, cake.getQuantity());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Cake findCake(String type, String size) {
		String sql = "SELECT * FROM Cake WHERE cake_type = ? AND size = ? LIMIT 1";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, type);
			stmt.setString(2, size);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Cake(
						rs.getString("cakeID"),
						rs.getString("cake_type"),
						rs.getInt("price"),
						rs.getString("size"),
						rs.getDate("made_date").toLocalDate(),
						rs.getString("ingredientID"),
						rs.getInt("quantity"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 여기 추가 //
	public static List<Cake> findCakesByType(String type) {
		String sql = "SELECT * FROM Cake WHERE cake_type = ?";
		List<Cake> list = new ArrayList<>();

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, type);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Cake c = new Cake(
							rs.getString("cakeID"),
							rs.getString("cake_type"),
							rs.getInt("price"),
							rs.getString("size"),
							rs.getDate("made_date").toLocalDate(),
							rs.getString("ingredientID"),
							rs.getInt("quantity"));
					list.add(c);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static boolean hasEnoughQuantity(Cake cake, int quantity) {
		return cake.getQuantity() >= quantity;
	}

	public static void reduceQuantity(Cake cake, int amount) {
		String sql = "UPDATE Cake SET quantity = quantity - ? WHERE cakeID = ?";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, amount);
			stmt.setString(2, cake.getCakeID());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addQuantity(Cake cake, int amount) {
		String sql = "UPDATE Cake SET quantity = quantity + ? WHERE cakeID = ?";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, amount);
			stmt.setString(2, cake.getCakeID());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Cake> getStock() {
		List<Cake> list = new ArrayList<>();
		String sql = "SELECT * FROM Cake";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Cake cake = new Cake(
						rs.getString("cakeID"),
						rs.getString("cake_type"),
						rs.getInt("price"),
						rs.getString("size"),
						rs.getDate("made_date").toLocalDate(),
						rs.getString("ingredientID"),
						rs.getInt("quantity"));
				list.add(cake);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

}
