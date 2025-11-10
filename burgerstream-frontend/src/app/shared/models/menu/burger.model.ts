import { MenuItem } from "./menu-item.model";

export class Burger implements MenuItem{
    id?: number;
    name: string;
    description?: string | undefined;
    basePrice: number;
    imageUrl?: string | undefined;

    isVegan: boolean;
    isChicken: boolean;
    isLactoseFree: boolean;
    
    constructor(name: string, description: string, basePrice: number, imageUrl: string, isVegan: boolean = false, isChicken: boolean = false, isLactoseFree: boolean = false){
        this.name = name,
        this.description = description,
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.isVegan = isVegan;
        this.isChicken = isChicken;
        this.isLactoseFree = isLactoseFree;
    }
}