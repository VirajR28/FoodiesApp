package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.entity.FoodEntity;
import in.virajraut.foodiesapi.io.FoodRequest;
import in.virajraut.foodiesapi.io.FoodResponse;
import in.virajraut.foodiesapi.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//Created a method inside below class which is responsible for uploading the image into the S3 bucket and returning the public url.
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private  S3Client s3Client;

    @Autowired
    private  FoodRepository foodRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        //To get original filename.
       String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1); //To extract the file extension

        //key contains the random UUID plus the file extension
        String key = UUID.randomUUID().toString()+"."+filenameExtension;
        try {

            //PutObjectRequest is typically part of the AWS SDK. It's used when interacting with Amazon S3 to upload an object (like a file, text, etc.) into an S3 bucket.
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            //Created put object request which we are going to add it to S3 bucket.


            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            //Checking if the file is successfully uploaded then manually generating and returning the public url.
            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File upload failed");
            }
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occured while uploading the file.");
        }
    }

    //We need to convert the request object to entity object. For this we are using private method.
    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);
    }

    @Override
    public List<FoodResponse> readFoods() {
       List<FoodEntity> databaseEntries = foodRepository.findAll();
       return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
       FoodEntity existingFood = foodRepository.findById(id).orElseThrow(()-> new RuntimeException("Food not found for the id: "+id));
       return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String filename) {
//The DeleteObjectRequest is part of the AWS SDK, and it's used to delete a specific file (object) from an S3 bucket.
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        boolean isFileDelete = deleteFile(filename);
        if (isFileDelete) {
            foodRepository.deleteById(response.getId());
        }
    }


    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .build();
    }

}
