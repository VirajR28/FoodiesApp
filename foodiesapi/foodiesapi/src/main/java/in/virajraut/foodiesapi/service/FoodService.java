package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.io.FoodRequest;
import in.virajraut.foodiesapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    //Will take multipart file as a parameter. When we upload a file we'll get the public url of that file so we are returning a String.
    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();

    FoodResponse readFood(String id);

    boolean deleteFile(String filename);

    void deleteFood(String id);

}
