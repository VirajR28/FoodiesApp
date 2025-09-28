package in.virajraut.foodiesapi.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO
//No need of image url since after uploading the image to S3 bucket we'll be getting url which we'll store in FoodEntity.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
    private String name;
    private String description;
    private double price;
    private String category;
}
