import axios from "axios";

//Writing API calls in main function(AddFood.jsx) is not a good practise, hence created separate file. 
const API_URL = 'http://localhost:8080/api/foods';

//JSON.stringify(data) convert JSON to String as in Intellij backend we are accepting food as a String
export const addFood = async (foodData, image) => {
    const formData = new FormData;
        formData.append('food', JSON.stringify(foodData));
        formData.append('file', image);
    
        //Passing formData to backend API along with the headers as we are passing the file and formData. Content-Type is nothing but the key-value pair required in headers.
        try {
          await axios.post(API_URL, formData, {headers:{"Content-Type":"multipart/form-data"}});
        } catch (error) {
          console.log('Error', error);
          throw error;
        }
}

export const getFoodlist = async()=>{
   try {
    const response = await axios.get(API_URL);
      return response.data;
   } catch (error) {
    console.log('Error while fetching food list', error);
    throw error;
   }
}

export const deleteFood = async (foodId)=>{
  try {
    const response = await axios.delete(API_URL+"/"+foodId);
    return response.status === 204; //return status if its equal to 204
  } catch (error) {
    console.log('Error while deleting the food.', error);
    throw error;
  }

}