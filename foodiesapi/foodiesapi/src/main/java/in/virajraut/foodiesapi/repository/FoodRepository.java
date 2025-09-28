package in.virajraut.foodiesapi.repository;

import in.virajraut.foodiesapi.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

//Just like JPARepository which takes entity type and its primary key type(id)
public interface FoodRepository extends MongoRepository<FoodEntity, String> {
}
