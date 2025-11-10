import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Drink } from '../../../models/menu/drink.model';
import { SizeOption } from '../../../models/menu/sizeOption.model';
import { DrinkService } from '../../../services/drink-service/drink-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'drink-card',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './drink-card.html',
    styleUrl: './drink-card.css'
})
export class DrinkCard implements OnInit, OnChanges {
    @Input() drink!: Drink;
    @Input() isAdmin: boolean = true;

    @Output() drinkDeleted = new EventEmitter<number>();

    selectedSize?: SizeOption;
    imageUrl = "";

    constructor(private drinkService: DrinkService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.selectedSize = this.drink.sizeOptions[0];
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['drink'] && this.drink?.imageUrl) {
            this.imageUrl = "http://localhost:8080/api/v1/BurgerStream/images/" + this.drink.imageUrl;
        }
    }

    updateDrink(id?:number){
        if(!id) return;
        this.router.navigate(['update-drink', id], { relativeTo: this.route })
    }

    deleteDrink() {
        if (this.drink.id) {
            if (confirm("Are you sure you wish to delete drink: " + this.drink.name + "?")) {
                this.drinkService.deleteDrink(this.drink.id).subscribe(
                    data => {
                        console.log("drink with id: ", this.drink.id, " has been deleted");
                        this.drinkDeleted.emit(this.drink.id);
                    }
                )
            }
        } else {
            console.log("I have no idea what this id is");
        }
    }
}
