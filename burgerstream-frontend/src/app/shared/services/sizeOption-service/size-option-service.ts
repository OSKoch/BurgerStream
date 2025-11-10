import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SizeOption } from '../../models/menu/sizeOption.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SizeOptionService {

    private baseURL = "http://localhost:8080/api/v1/BurgerStream/menu/sizes";

    constructor(private httpClient: HttpClient) {};

    getAllSizes(): Observable<SizeOption[]>{
        return this.httpClient.get<SizeOption[]>(this.baseURL);

    }

}
