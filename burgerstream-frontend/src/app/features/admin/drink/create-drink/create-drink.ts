import { Component, OnInit } from '@angular/core';
import { Drink } from '../../../../shared/models/menu/drink.model';
import { DrinkService } from '../../../../shared/services/drink-service/drink-service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SizeOptionService } from '../../../../shared/services/sizeOption-service/size-option-service';
import { SizeOption } from '../../../../shared/models/menu/sizeOption.model';

@Component({
    selector: 'create-drink',
    imports: [FormsModule, CommonModule],
    templateUrl: './create-drink.html',
    styleUrl: './create-drink.css'
})
export class CreateDrink implements OnInit {

    drink: Drink = new Drink('', '', 0, '', []);

    selectedfile?: File;

    selectedSize?: SizeOption;

    allSizes: SizeOption[] = [];

    constructor(private drinkService: DrinkService, private sizeOptionService: SizeOptionService, private router: Router) { }

    ngOnInit(): void {
        this.getSizes();

    }

    private getSizes() {
        this.sizeOptionService.getAllSizes().subscribe(
            data => {
                console.log(data);
                this.allSizes = data;
            },
            error => console.log("Http error: ", error)
        )
    }

    goToAdminView() {
        this.router.navigate(['/admin']);
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('drink', new Blob([JSON.stringify(this.drink)], { type: 'application/json' }));
        if (this.selectedfile) {
            formData.append('image', this.selectedfile);
        }

        this.saveDrink(formData);
    }

    saveDrink(formData: FormData) {
        this.drinkService.createDrink(formData).subscribe(data => {
            console.log(data)
            this.goToAdminView();
        },
            error => console.log(error));
    }

    addSizeToDrink(size: SizeOption) {
        const alreadyAdded = this.drink.sizeOptions.some(s => s.id === size.id);
        if (alreadyAdded) return;

        this.drink.sizeOptions.push(size);

        this.allSizes = this.allSizes.filter(s => s.id !== size.id);
    }

    removeSizeFromDrink(size: SizeOption) {
        this.drink.sizeOptions = this.drink.sizeOptions.filter(s => s.id !== size.id);

        const existsInAllSizes = this.allSizes.some(s => s.id === size.id);
        if (!existsInAllSizes) {
            this.allSizes.push(size);
        }
    }

    onFileSelected(event: any) {
        this.selectedfile = event.target.files[0];
    }
}
