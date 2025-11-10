import { Component } from '@angular/core';
import { MenuItemList } from '../../../shared/components/menu-item-list/menu-item-list';

@Component({
  selector: 'app-public-view',
  imports: [MenuItemList],
  templateUrl: './public-view.html',
  styleUrl: './public-view.css'
})
export class PublicView {

}
