import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuItemList } from './features/admin/menu-item-list/menu-item-list';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MenuItemList],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('burgerstream-frontend');
}
