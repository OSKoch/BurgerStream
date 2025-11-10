import { Component } from '@angular/core';
import { Burger } from '../../../../shared/models/menu/burger.model';
import { BurgerService } from '../../../../shared/services/burger-service/burger-service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'create-burger',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-burger.html',
  styleUrl: './create-burger.css'
})
export class CreateBurger {

    burger: Burger = new Burger('','',0,'');

    selectedfile?: File;

    veganDisabled: boolean = false;
    chickenDisabled: boolean = false;
    lactoseFreeDisabled: boolean = false;

    constructor(private burgerService: BurgerService, private router: Router) {}

    saveBurger(formData: FormData){
        this.burgerService.createBurger(formData).subscribe( data => {
            console.log(data)
            this.goToAdminView();
        },
        error => console.log(error));
    }

    goToAdminView(){
        this.router.navigate(['/admin']);
    }

    onSubmit(){
        const formData = new FormData();
        formData.append('burger', new Blob([JSON.stringify(this.burger)], {type: 'application/json'}));
        if (this.selectedfile){
            formData.append('image', this.selectedfile);
        }

        this.saveBurger(formData);
    }

    updateCheckboxes(){
        if (this.burger.isVegan){
            this.burger.isLactoseFree = true;
            this.lactoseFreeDisabled = true;

            this.burger.isChicken = false;
            this.chickenDisabled = true;
        }

        if (!this.burger.isVegan){
            this.chickenDisabled = false;

            this.burger.isLactoseFree = false;
            this.lactoseFreeDisabled = false;
        }

        if (this.burger.isChicken) {
            this.burger.isVegan = false;
            this.veganDisabled = true;
        }

        if (!this.burger.isChicken && this.veganDisabled){
            this.veganDisabled = false;
        }
    }

    onFileSelected(event: any) {
        this.selectedfile = event.target.files[0];
    }

}
