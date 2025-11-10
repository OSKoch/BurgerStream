import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MenuItemList } from '../../../shared/components/menu-item-list/menu-item-list';

@Component({
  selector: 'admin-menu-items',
  imports: [MenuItemList],
  templateUrl: './admin-menu-items.html',
  styleUrl: './admin-menu-items.css'
})
export class AdminMenuItems {
      constructor(private router: Router, private route: ActivatedRoute) {}


    createBurger(){
        this.router.navigate(['create-burger'], { relativeTo: this.route});
    }

    createDrink(){
        this.router.navigate(['create-drink'], {relativeTo: this.route});
    }

    createSide(){
        this.router.navigate(['create-side'], {relativeTo: this.route})
    }

}
