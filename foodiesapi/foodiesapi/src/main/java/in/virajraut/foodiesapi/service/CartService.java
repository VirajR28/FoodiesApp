package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.io.CartRequest;
import in.virajraut.foodiesapi.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getCart();//Not passing anything, will get the user's id from Spring context.

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);
}

