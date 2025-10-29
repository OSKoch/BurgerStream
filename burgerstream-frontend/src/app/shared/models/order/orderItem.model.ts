import { MenuItem } from "../menu/menu-item.model";
import { SizeOption } from "../menu/sizeOption.model";

export class OrderItem {
    id?: number;
    menuItem: MenuItem;
    sizeOption?: SizeOption;
    amount: number;
    itemEndPrice: number;

    constructor(id: number, menuItem: MenuItem, sizeOption: SizeOption, amount: number, itemEndPrice: number){
        this.id = id;
        this.menuItem = menuItem;
        this.sizeOption = sizeOption;
        this.amount = amount;
        this.itemEndPrice = itemEndPrice;
    }
}