//package database;

import java.time.LocalDate;

public class Ingredient {
	private String igdID;
	private String igdName;
	private int igdQuantity;
	private LocalDate expDate;

	// 생성자
	public Ingredient(String igdID, String igdName, int igdQuantity, LocalDate expDate) {
		this.igdID = igdID;
		this.igdName = igdName;
		this.igdQuantity = igdQuantity;
		this.expDate = expDate;
	}

	// getter
	public String getIgdID() {
		return igdID;
	}

	public String getIgdName() {
		return igdName;
	}

	public int getIgdQuantity() {
		return igdQuantity;
	}

	public LocalDate getExpDate() {
		return expDate;
	}

	// setter
	public void setIgdQuantity(int quantity) {
		this.igdQuantity = quantity;
	}

}
