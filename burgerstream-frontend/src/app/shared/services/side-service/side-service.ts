import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Side } from '../../models/menu/side.model';

@Injectable({
    providedIn: 'root'
})
export class SideService {
    private baseURL = "http://localhost:8080/api/v1/BurgerStream/menu/sides"

    constructor(private httpClient: HttpClient) { }

    getAllSides(): Observable<Side[]> {
        return this.httpClient.get<Side[]>(this.baseURL);
    }

    getSideById(id: number): Observable<Side> {
        return this.httpClient.get<Side>(`${this.baseURL}/${id}`)
    }

    createSide(formData: FormData): Observable<Side> {
        return this.httpClient.post<Side>(this.baseURL, formData);
    }

    updateSide(id: number, formData: FormData): Observable<Object> {
        return this.httpClient.put(`${this.baseURL}/${id}`, formData);
    }

    deleteSide(id: number): Observable<Object> {
        return this.httpClient.delete(`${this.baseURL}/${id}`)
    }
}
