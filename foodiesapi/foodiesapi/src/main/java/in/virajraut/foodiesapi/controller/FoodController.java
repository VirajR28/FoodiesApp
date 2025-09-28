package in.virajraut.foodiesapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.virajraut.foodiesapi.io.FoodRequest;
import in.virajraut.foodiesapi.io.FoodResponse;
import in.virajraut.foodiesapi.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin("*")//Very common error, Spring boot application will block any request from other technologies like React on our case. To avoid those cross origin connection error we used this.
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file") MultipartFile file){
        //@RequestPart is used when the client sends data using multipart/form-data, often used for file uploads + additional fields.

        //@RequestBody is best for pure JSON requests
        //Cannot handle multipart data (like files)

        //ObjectMapper class is used to convert between Java objects and JSON.

        //JSON to Java
        //User user = mapper.readValue(jsonString, User.class);

        // Java to JSON
        //String json = mapper.writeValueAsString(user);

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            request = objectMapper.readValue(foodString, FoodRequest.class);
        }catch (JsonProcessingException ex){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }
        FoodResponse response = foodService.addFood(request, file);
        return response;
    }

    @GetMapping
    public List<FoodResponse> readFoods(){
        return foodService.readFoods();
    }

    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){
        return foodService.readFood(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id){
        foodService.deleteFood(id);
    }

}
