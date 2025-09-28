import React, { useState } from 'react';
import Header from '../../components/Header/Header';
import ExploreMenu from '../../components/ExploreMenu/ExploreMenu';
import FoodDisplay from '../../components/FoodDisplay/FoodDisplay';
import { useSearchParams } from 'react-router-dom';

//Earlier FoodDisplay was accepting only category as a prop but now its accepting searchText as well from ExploreFood.jsx hench here also we need to send empty searchText as a prop otherwise it will throw an error.
const Home = () => {
  const[category, setCategory] = useState('All');
  return (
    <main className="container">
      <Header />
      <ExploreMenu category={category} setCategory={setCategory}/>
      <FoodDisplay category={category} searchText={''}/>
    </main>
  )
}

export default Home;