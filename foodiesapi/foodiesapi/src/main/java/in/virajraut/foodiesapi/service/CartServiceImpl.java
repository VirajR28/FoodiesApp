package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.entity.CartEntity;
import in.virajraut.foodiesapi.io.CartRequest;
import in.virajraut.foodiesapi.io.CartResponse;
import in.virajraut.foodiesapi.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{


    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
        System.out.println("Current Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        String loggedInUserId = userService.findByUserId();
        //Checking whether existing cart is available or not for the user
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        //If cart is not present, create new cart entity.
        CartEntity cart = cartOptional.orElseGet(()-> new CartEntity(loggedInUserId, new HashMap<>()));
        //If cart is present, we're getting the items.
        Map<String, Integer> cartItems = cart.getItems();
        cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0)+1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElse(new CartEntity(null, loggedInUserId, new HashMap<>())); //cartRepository.findByUserId() is returning Optional object. If cart entity is not present "orElse()" will create a new cart entity.
        return convertToResponse(entity);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest cartRequest) {
        String loggedInUserId = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(()-> new RuntimeException("Cart is not found"));
        Map<String, Integer> cartItems = entity.getItems();
        if(cartItems.containsKey(cartRequest.getFoodId())){
            int curretnQty = cartItems.get(cartRequest.getFoodId());
            if(curretnQty>0){
                cartItems.put(cartRequest.getFoodId(), curretnQty-1);
            } else {
                cartItems.remove(cartRequest.getFoodId()); //If qty is zero then removing the key which is "foodId"
            }
            entity = cartRepository.save(entity);
        }
        return convertToResponse(entity);
    }

    private CartResponse convertToResponse(CartEntity cartEntity){
        return CartResponse.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId())
                .items(cartEntity.getItems())
                .build();
    }
}
