package in.virajraut.foodiesapi.service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.virajraut.foodiesapi.io.OrderRequest;
import in.virajraut.foodiesapi.io.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;

    //to verify the payment done by user in razorpay dialogue box
    //once payment is completed we will get a paymentdata which contains razorpay payment id, signature
    void verifyPayment(Map<String, String> paymentData, String status);

    //To see loggedIn user's orders on UI
    List<OrderResponse> getUserOrders();

    //whenever order gets failed we need to remove that order
    void removeOrder(String orderId);

    //To retrieve all the orders from the database for admin panel
    List<OrderResponse> getOrdersOfAllUsers();

    //To change the order status once admin makes the changes.
    void updateOrderStatus(String orderId, String status);
}
