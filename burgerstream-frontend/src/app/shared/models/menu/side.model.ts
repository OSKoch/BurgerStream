import { MenuItem } from "./menu-item.model";
import { SizeOption } from "./sizeOption.model";

export class Side implements MenuItem {
    id: number;
    name: string;
    description?: string;
    basePrice: number;
    imageUrl?: string;
    sizeOptions: SizeOption[];

    isShareable?: boolean;

    constructor(id: number, name: string, description: string, basePrice: number, imageUrl: string, sizeOptions: SizeOption[], isShareable: boolean = false){
        this.id = id;
        this.name = name,
        this.description = description,
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.sizeOptions = sizeOptions;
        this.isShareable = isShareable;
    }
}