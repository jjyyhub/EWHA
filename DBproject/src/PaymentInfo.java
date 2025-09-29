public class PaymentInfo {
    private final String customerID;
    private final int totalPrice;
    private final String paymentMethod;

    public PaymentInfo(String customerID, int total_price, String paymentMethod) {
        this.customerID = customerID;
        this.totalPrice = total_price;
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerID() {
        return customerID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
