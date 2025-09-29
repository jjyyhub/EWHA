
//package database;
import java.time.LocalDate;

public class Order {
	private static int orderCounter = 1;
	// orderID도 database에서 받아오는 게 아니라 생성되는 게 자연스러우니까

	private String orderID;
	private LocalDate orderDate;
	private LocalDate pickupDate;
	private int totalPrice;
	private int quantity;
	private String customerID;
	private Payment payment; // 지불 수단 연결하기.

	private Cake cake; // 주문한 케이크 정보 포함

	public Order(Cake cake, int quantity, String customerID, LocalDate pickupDate) {
		this.orderID = "R" + String.format("%03d", orderCounter++);
		this.orderDate = LocalDate.now(); // 주문 날짜
		this.cake = cake;
		this.quantity = quantity;
		this.customerID = customerID;
		this.pickupDate = pickupDate; // 픽업할 날짜
		this.totalPrice = cake.getPrice() * quantity;

	}

	// select 1번을 위한 생성//
	public Order(String string, LocalDate localDate, LocalDate localDate2, int int1, String string2, int int2,
			String string3) {
		// TODO Auto-generated constructor stub
	}

	// getter
	public String getOrderID() {
		return orderID;
	}

	public LocalDate getPickupDate() {
		return pickupDate;
	}

	public int getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}

	public String getCustomerID() {
		return customerID;
	}

	public Payment getPayment() {
		return payment;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	// 필요한 메서드들(getter 포함)

	public Cake getCake() {
		return cake;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
		this.totalPrice = cake.getPrice() * quantity;
	}

	public void updateTotalPrice() {
		this.totalPrice = cake.getPrice() * quantity;
	}

	public static Order createOrder(Cake cake, int quantity, Customer customer, LocalDate pickupDate) {
		Order order = new Order(cake, quantity, customer.getCustomerID(), pickupDate);

		// 주문일 기준으로 고객의 마지막 주문일 갱신
		customer.setLastOrderDate(order.orderDate);

		return order;
	}

	public void printOrderInfo() {
		System.out.println();
		System.out.println("----------주문 내역 확인----------");
		System.out.println("주문번호: " + orderID);
		System.out.println("주문일자: " + orderDate);
		System.out.println("픽업 날짜: " + pickupDate);
		System.out.println("케이크 종류: " + cake.getCakeType());
		System.out.println("사이즈: " + cake.getSize());
		System.out.println("수량: " + quantity);
		System.out.println("총 금액: " + totalPrice + "원");
		System.out.println("고객 ID: " + customerID);
		if (payment != null) {
			System.out.println("결제 수단: " + payment.getPaymentMethod());
			System.out.println("결제 ID: " + payment.getPaymentID());
		}
		System.out.println("-------------------------------");
		System.out.println();
	}

	public void setPickupDate(LocalDate pickupDate) {
		this.pickupDate = pickupDate;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
