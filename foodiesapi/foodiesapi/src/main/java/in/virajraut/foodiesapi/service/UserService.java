package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.io.UserRequest;
import in.virajraut.foodiesapi.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();

}
