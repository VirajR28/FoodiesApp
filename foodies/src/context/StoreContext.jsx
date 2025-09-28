import { createContext, useEffect, useState } from "react";
import { fetchFoodList } from "../service/foodService";
import axios from "axios";
import { addToCart, getCartData, removeQtyFromCart } from "../service/cartService";


export const StoreContext = createContext(null);

export const StoreContextProvider = (props) => {

    const [foodList, setFoodList] = useState([]);
    const [quantities, setQuantities] = useState({}); //Will store the data inside object in ["foodId":qty] key-value pair.
    const [token, setToken] = useState("");

    
    //If the item already has a quantity, it adds +1.
    //If not, prev[foodId] is undefined, so it uses 0 + 1 → starts from 1.
    const increaseQty = async (foodId) => {
        setQuantities((prev) => ({
            ...prev,
            [foodId]: (prev[foodId] || 0) + 1
        }));
        await addToCart(foodId, token);
    };

    const decreaseQty = async (foodId) => {
        setQuantities((prev) => ({
            ...prev,
            [foodId]: prev[foodId] > 0 ? prev[foodId] - 1 : 0
        }));
        await removeQtyFromCart(foodId, token);
    }

    const removeFromCart = (foodId) => {
        setQuantities((prevQuantites)=>{
            const updatedQuantities = {...prevQuantites};
            delete updatedQuantities[foodId];
            return updatedQuantities;
        })
    }

    
    const loadCartData =async (token) => {
        setQuantities(await getCartData(token));
    }

    //Can’t make the useEffect callback function itself async directly.
    //Making useEffect(() => { ... }, []) an async function would cause it to return a Promise, which React doesn't know how to handle.
    //As soon as the application runs,StoreContext is going to execute the useEffect() hook.
    //On refresh user is logging out even though the token is present. To prevent this, if token is present in localstorage then set token again.   
    //In setQuantities we cannot directly take the token from above useState since first the useEffect hook is called and at that time we're still to get the token
    useEffect(() => {
        async function loadData() {
            const data = await fetchFoodList();
            setFoodList(data);
            if(localStorage.getItem('token')){
                setToken(localStorage.getItem('token'));
                await loadCartData(localStorage.getItem('token'));
            }
        }
        loadData();
    }, [])

    const contextValue = {
        foodList,
        increaseQty,
        decreaseQty,
        quantities,
        removeFromCart,
        token,
        setToken,
        setQuantities,
        loadCartData
    }


    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    )
}