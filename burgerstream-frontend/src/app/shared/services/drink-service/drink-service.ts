import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Drink } from '../../models/menu/drink.model';

@Injectable({
  providedIn: 'root'
})
export class DrinkService {
    private baseURL = "http://localhost:8080/api/v1/BurgerStream/menu/drinks"

    constructor(private httpClient: HttpClient) {}

    getAllDrinks(): Observable<Drink[]>{
        return this.httpClient.get<Drink[]>(this.baseURL);
    }
    
    getDrinkById(id: number): Observable<Drink>{
        return this.httpClient.get<Drink>(`${this.baseURL}/${id}`)
    }

    createDrink(formData: FormData): Observable<Drink>{
        return this.httpClient.post<Drink>(this.baseURL, formData);
    }

    updateDrink(id: number, formData: FormData): Observable<Object>{
        return this.httpClient.put(`${this.baseURL}/${id}`, formData);
    }

    deleteDrink(id: number): Observable<Object>{
        return this.httpClient.delete(`${this.baseURL}/${id}`)
    }
    
}
