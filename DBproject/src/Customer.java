//package database;

import java.time.LocalDate;

public class Customer {
	private String customerID;
	private String customerName;
	private String customerPhone;
	private LocalDate last_order_date;

	// 생성자
	public Customer(String customerID, String customerName, String customerPhone) {
		this.customerID = customerID;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.last_order_date = null;
	}

	// local date 담은 생성자
	public Customer(String customerID, String customerName, String customerPhone, LocalDate lastOrderDate) {
		this.customerID = customerID;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.last_order_date = lastOrderDate;
	}

	// getter setter
	public String getCustomerID() {
		return customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public LocalDate getLastOrderDate() {
		return last_order_date;
	}

	public void setLastOrderDate(LocalDate date) {
		this.last_order_date = date;
	}
}
