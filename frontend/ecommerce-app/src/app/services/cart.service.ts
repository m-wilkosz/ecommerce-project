import {Injectable} from "@angular/core";
import {CartItem} from "../common/cart-item";
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];

  totalPrice: Subject<number> = new Subject<number>();
  totalQuantity: Subject<number> = new Subject<number>();

  constructor() { }

  addToCart(theCartItem: CartItem) {

    // check if we already have the item in our cart
    let alreadyExistInCart: boolean = false;
    let existingCartItem: CartItem = undefined!;

    if (this.cartItems.length > 0) {

      // find the item in the cart based on item id
      existingCartItem = this.cartItems.find(tmpCartItem => tmpCartItem.id === theCartItem.id)!;

      alreadyExistInCart = (existingCartItem != undefined);

      if (alreadyExistInCart) {

        // increment the quantity
        // @ts-ignore
        existingCartItem.quantity++;
      } else {

        // just add the item to the array
        this.cartItems.push(theCartItem);
      }
    } else {

      // just add the item to the array
      this.cartItems.push(theCartItem);
    }

    // compute cart total price and total quantity
    this.computeCartTotals();
  }

  computeCartTotals() {

    let totalPriceValue: number = 0;
    let totalQuantityValue: number = 0;

    for (let currentCartItem of this.cartItems) {
      totalPriceValue += currentCartItem.quantity * currentCartItem.unitPrice;
      totalQuantityValue += currentCartItem.quantity;
    }

    // publish the new value
    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);

    // log cart data
    this.logCartData(totalPriceValue, totalQuantityValue);
  }

  private logCartData(totalPriceValue: number, totalQuantityValue: number) {

    console.log('Contents of the cart');
    for (let tmpCartItem of this.cartItems) {
      const subTotalPrice = tmpCartItem.quantity * tmpCartItem.unitPrice;
      console.log(`name: ${tmpCartItem.name}, quantity=${tmpCartItem.quantity}, unitPrice=${tmpCartItem.unitPrice}, subTotalPrice=${subTotalPrice}`);
    }

    console.log(`totalPrice: ${totalPriceValue.toFixed(2)}, totalQuantity: ${totalQuantityValue}`);
    console.log('-----');
  }
}
