import java.time.LocalDate;

public class OrderInfo {
    private String customerName;
    private String customerID;
    private LocalDate orderDate;
    private LocalDate pickupDate;
    private int quantity;
    private String cakeType;
    private int totalPrice;
    private String paymentMethod;

    public OrderInfo(String customerName,
            LocalDate orderDate,
            LocalDate pickupDate,
            int quantity,
            String cakeType,
            int totalPrice,
            String paymentMethod) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
        this.quantity = quantity;
        this.cakeType = cakeType;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    // getters...

    public OrderInfo(String customerID, int total_price, String payment_method) {
        this.customerID = customerID;
        this.totalPrice = total_price;
        this.paymentMethod = payment_method;
    }

    @Override
    public String toString() {
        return String.format(
                "고객명: %s\n" +
                        "주문일: %s\n" +
                        "픽업일: %s\n" +
                        "케이크: %s (%d개)\n" +
                        "총 금액: %d원\n" +
                        "결제방법: %s\n",
                customerName, orderDate, pickupDate,
                cakeType, quantity, totalPrice, paymentMethod);
    }
}
