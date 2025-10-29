import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuItem } from '../../../shared/models/menu/menu-item.model';
import { MenuItemService } from '../../../shared/services/menu-item.service';


@Component({
  selector: 'menu-item-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu-item-list.html',
  styleUrl: './menu-item-list.css'
})
export class MenuItemList implements OnInit{
    menuItems: MenuItem[] = [];


    constructor(private menuItemService: MenuItemService) { } 

    ngOnInit(): void {
        this.getMenuItems();
    }

    private getMenuItems(){
        this.menuItemService.getAllMenuItems().subscribe(
            data => {
                console.log("Menu items from backend: ", data)
                this.menuItems = data;
            },
            error => {
                console.error("Http error", error)
            }
        )
    }
}
