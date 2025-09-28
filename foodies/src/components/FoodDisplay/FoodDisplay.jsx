import React from 'react';
import { useContext } from 'react';
import { StoreContext } from '../../context/StoreContext';
import FoodItem from '../FoodItem/FoodItem';

const FoodDisplay = ({ category, searchText }) => {

  //If category === 'All', include everything.
  //Otherwise, include only items where food.category === category.

  //category === 'All' || food.category === category
  //Keep this food item if:
  //the selected category is ‘All’
  //OR
  //the food item’s category exactly matches the selected category”
  //food.name.toLowerCase().includes(searchText.toLowerCase())
  //After filtering the categories we are checking searchText of input tag is present in the food name or not.
  const { foodList } = useContext(StoreContext);
  const filteredFoods = foodList.filter(food => (
    (category === 'All' || food.category === category) && 
    food.name.toLowerCase().includes(searchText.toLowerCase())
  ));


  //If foodList is not empty then disply the food item cards else display "No Food Found" 
  return (
    <div className="container">
      <div className="row">
        {filteredFoods.length > 0 ? (
          filteredFoods.map((food, index) => {
            return (
              <FoodItem key={index}
                name={food.name}
                description={food.description}
                price={food.price}
                id={food.id}
                imageUrl={food.imageUrl}
              />
            )
          })
        ) : (
          <div className="text-center mt-4">
            <h4>No food found.</h4>
          </div>
        )}
      </div>
    </div>
  )
}

export default FoodDisplay;