//Utils are used to remove duplicacy of code. Same below code was written in PlaceOrder.jsx and Cart.jsx


export const calculateCartTotals = (cartItems, quantities)=>{
    const subtotal = cartItems.reduce((acc, food) => acc + food.price * quantities[food.id], 0)

    const shipping = subtotal === 0 ? 0.0 : 10;
    const tax = 0.1 * subtotal; //10%
    const total = subtotal + shipping + tax;
    return {subtotal, shipping, tax, total}; 
}