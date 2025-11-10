import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Burger } from '../../models/menu/burger.model';
import { BurgerCard } from '../cards/burger-card/burger-card';
import { BurgerService } from '../../services/burger-service/burger-service';

import { Drink } from '../../models/menu/drink.model';
import { DrinkCard } from '../cards/drink-card/drink-card';
import { DrinkService } from '../../services/drink-service/drink-service';

import { Side } from '../../models/menu/side.model';
import { SideCard } from '../cards/side-card/side-card';
import { SideService } from '../../services/side-service/side-service';

@Component({
    selector: 'menu-item-list',
    standalone: true,
    imports: [CommonModule, BurgerCard, DrinkCard, SideCard],
    templateUrl: './menu-item-list.html',
    styleUrl: './menu-item-list.css'
})
export class MenuItemList implements OnInit {
    @Input() isAdmin: boolean = false;

    burgers: Burger[] = [];
    drinks: Drink[] = [];
    sides: Side[] = [];


    constructor(private burgerService: BurgerService, private drinkService: DrinkService, private sideService: SideService) { }

    ngOnInit(): void {
        this.getMenuItems();
    }

    private getMenuItems() {
        this.burgerService.getAllBurgers().subscribe(
            data => {
                this.burgers = data;
            },
            error => {
                console.error("Http error ", error)
            }
        )
        this.drinkService.getAllDrinks().subscribe(
            data => {
                this.drinks = data;
            }
        )
        this.sideService.getAllSides().subscribe(
            data => {
                this.sides = data;
            }
        )
    }

    onBurgerDeleted(deletedId: number) {
        this.burgers = this.burgers.filter(b => b.id !== deletedId);
    }

    onDrinkDeleted(deletedId: number) {
        this.drinks = this.drinks.filter(b => b.id !== deletedId);
    }

    onSideDeleted(deletedId: number) {
        this.sides = this.sides.filter(b => b.id !== deletedId);
    }
}
