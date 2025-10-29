import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MenuItem } from '../models/menu/menu-item.model';

@Injectable({
  providedIn: 'root'
})
export class MenuItemService {
  
    private baseURL = "http://localhost:8080/api/v1/BurgerStream/menu/items"

    constructor(private httpClient: HttpClient) {}

    getAllMenuItems(): Observable<MenuItem[]>{
        return this.httpClient.get<MenuItem[]>(this.baseURL);
    }
}
