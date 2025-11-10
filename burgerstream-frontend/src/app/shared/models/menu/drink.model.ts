import { MenuItem } from "./menu-item.model";
import { SizeOption } from "./sizeOption.model";

export class Drink implements MenuItem {
    id?: number;
    name: string;
    description?: string;
    basePrice: number;
    imageUrl?: string;
    sizeOptions: SizeOption[];

    isCarbonated?: boolean;
    isLactoseFree?: boolean;

    constructor(name: string, description: string, basePrice: number, imageUrl: string, sizeOptions: SizeOption[], isCarbonated: boolean = false, isLactoseFree: boolean = false){
        this.name = name,
        this.description = description,
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.sizeOptions = sizeOptions;
        this.isCarbonated = isCarbonated;
        this.isLactoseFree = isLactoseFree;
    }
}