//package database;

public class Payment {

	private static int counter = 1; // c001, c002, ...
	// 왜냐하면 paymentID 는 입력받는 게 아니라 생성되는 게 자연스럽잖아.

	private String paymentID;
	private String paymentMethod;
	private String orderID; // 외래키

	public Payment(String paymentMethod, String orderID) {
		this.paymentID = "P" + String.format("%03d", counter++);
		this.paymentMethod = paymentMethod;
		this.orderID = orderID;
	}

	// Getter
	public String getPaymentID() {
		return paymentID;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getOrderID() {
		return orderID;
	}

	// Setter (필요 시)
	public void setPaymentMethod(String method) {
		this.paymentMethod = method;
	}

}
