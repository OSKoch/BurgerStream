import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import { Burger } from '../../../models/menu/burger.model';
import { BurgerService } from '../../../services/burger-service/burger-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'burger-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './burger-card.html',
  styleUrl: './burger-card.css'
})
export class BurgerCard implements OnChanges{
    @Input() burger!: Burger;
    @Input() isAdmin: boolean = false;

    @Output() burgerDeleted = new EventEmitter<number>();

    imageUrl = "";

    constructor(private burgerService: BurgerService, private router: Router, private route: ActivatedRoute) {}

    ngOnChanges(changes: SimpleChanges) {
    if (changes['burger'] && this.burger?.imageUrl) {
      this.imageUrl = "http://localhost:8080/api/v1/BurgerStream/images/" + this.burger.imageUrl;
        }
    }


    updateBurger(id?:number){
        if(!id) return;
        this.router.navigate(['update-burger', id], { relativeTo: this.route })
    }

    deleteBurger(){
        if (this.burger.id){
            if(confirm("Are you sure you wish to delete burger: " + this.burger.name +"?")){
                this.burgerService.deleteBurger(this.burger.id).subscribe(
                    data => {
                        console.log("Burger with id: " , this.burger.id , " has been deleted");
                        this.burgerDeleted.emit(this.burger.id); 
                    }
                )
            }
        } else {
            console.log("I have no idea what this id is");
        }
    }
}
