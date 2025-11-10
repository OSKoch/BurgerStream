import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Burger } from '../../models/menu/burger.model';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BurgerService {
  
    private baseURL = "http://localhost:8080/api/v1/BurgerStream/menu/burgers"

    constructor(private httpClient: HttpClient) {}

    getAllBurgers(): Observable<Burger[]>{
        return this.httpClient.get<Burger[]>(this.baseURL);
    }
    
    getBurgerById(id: number): Observable<Burger>{
        return this.httpClient.get<Burger>(`${this.baseURL}/${id}`)
    }

    createBurger(formData: FormData): Observable<Burger>{
        return this.httpClient.post<Burger>(this.baseURL, formData);
    }

    updateBurger(id: number, formData: FormData): Observable<Object>{
        return this.httpClient.put(`${this.baseURL}/${id}`, formData);
    }

    deleteBurger(id: number): Observable<Object>{
        return this.httpClient.delete(`${this.baseURL}/${id}`)
    }
}
