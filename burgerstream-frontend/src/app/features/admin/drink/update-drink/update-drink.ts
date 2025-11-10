import { Component } from '@angular/core';
import { Drink } from '../../../../shared/models/menu/drink.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DrinkService } from '../../../../shared/services/drink-service/drink-service';
import { SizeOptionService } from '../../../../shared/services/sizeOption-service/size-option-service';
import { ActivatedRoute, Router } from '@angular/router';
import { SizeOption } from '../../../../shared/models/menu/sizeOption.model';

@Component({
    selector: 'update-drink',
    imports: [FormsModule, CommonModule],
    templateUrl: './update-drink.html',
    styleUrl: './update-drink.css'
})
export class UpdateDrink {
    id: number = 0;
    drink: Drink = new Drink('', '', 0, '', []);

    allSizes: SizeOption[] = [];

    selectedSize?: SizeOption;
    selectedfile?: File;

    constructor(private drinkService: DrinkService, private sizeOptionService: SizeOptionService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.id = this.route.snapshot.params['id'];
        this.drinkService.getDrinkById(this.id).subscribe(data => {
            this.drink = data;

            this.sizeOptionService.getAllSizes().subscribe(
                data => {
                    console.log(data);
                    const selectedIds = this.drink.sizeOptions.map(s => s.id);
                    this.allSizes = data.filter(size => !selectedIds.includes(size.id));
                },
                error => console.log("Http error: ", error)
            )
        }, error => console.log(error));
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('drink', new Blob([JSON.stringify(this.drink)], { type: 'application/json' }));
        if (this.selectedfile) {
            formData.append('image', this.selectedfile);
        }

        this.updateDrink(formData);
    }

    updateDrink(formData: FormData) {
        if (this.drink.id) {
            this.drinkService.updateDrink(this.drink.id, formData).subscribe(data => {
                console.log(formData);
                this.goToAdminView();
            },
                error => console.log(error));
        }
    }

    goToAdminView() {
        this.router.navigate(['/admin']);
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
