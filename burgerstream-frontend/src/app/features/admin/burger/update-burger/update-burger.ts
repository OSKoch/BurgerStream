import { Component } from '@angular/core';
import { BurgerService } from '../../../../shared/services/burger-service/burger-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Burger } from '../../../../shared/models/menu/burger.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'update-burger',
    imports: [FormsModule, CommonModule],
    templateUrl: './update-burger.html',
    styleUrl: './update-burger.css'
})
export class UpdateBurger {
    id: number = 0;
    burger: Burger = new Burger('', '', 0, '');

    selectedfile?: File;

    veganDisabled: boolean = false;
    chickenDisabled: boolean = false;
    lactoseFreeDisabled: boolean = false;

    constructor(private burgerService: BurgerService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.id = this.route.snapshot.params['id'];
        this.burgerService.getBurgerById(this.id).subscribe(data => {
            this.burger = data;
        }, error => console.log(error));
    }

    saveBurger(formData: FormData) {
        if (this.burger.id) {
            this.burgerService.updateBurger(this.burger.id, formData).subscribe(data => {

                this.goToAdminView();
            },
                error => console.log(error));
        }
    }

    goToAdminView() {
        this.router.navigate(['/admin']);
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('burger', new Blob([JSON.stringify(this.burger)], { type: 'application/json' }));
        if (this.selectedfile) {
            formData.append('image', this.selectedfile);
        }

        this.saveBurger(formData);
    }

    updateCheckboxes() {
        if (this.burger.isVegan) {
            this.burger.isLactoseFree = true;
            this.lactoseFreeDisabled = true;

            this.burger.isChicken = false;
            this.chickenDisabled = true;
        }

        if (!this.burger.isVegan) {
            this.chickenDisabled = false;

            this.burger.isLactoseFree = false;
            this.lactoseFreeDisabled = false;
        }

        if (this.burger.isChicken) {
            this.burger.isVegan = false;
            this.veganDisabled = true;
        }

        if (!this.burger.isChicken && this.veganDisabled) {
            this.veganDisabled = false;
        }
    }

    onFileSelected(event: any) {
        this.selectedfile = event.target.files[0];
    }
}
