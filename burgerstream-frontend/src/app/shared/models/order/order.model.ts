import { OrderItem } from "./orderItem.model";

export class Order{
    id: number;
    date?: string;
    orderEndPrice: number;
    customerName: string;
    customerEmail: string;
    orderItems: OrderItem[];

    constructor(id: number, date: string, orderEndPrice: number, customerName: string, customerEmail: string, orderItems: OrderItem[]){
        this.id = id;
        this.date = date;
        this.orderEndPrice = orderEndPrice;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderItems = orderItems;
    }
}