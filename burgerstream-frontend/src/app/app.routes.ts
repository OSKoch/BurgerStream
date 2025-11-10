import { Routes } from '@angular/router';
import { AdminView } from './features/admin/admin-view/admin-view';
import { PublicView } from './features/public/public-view/public-view';
import { CreateBurger } from './features/admin/burger/create-burger/create-burger';
import { AdminMenuItems } from './features/admin/admin-menu-items/admin-menu-items';
import { CreateDrink } from './features/admin/drink/create-drink/create-drink';
import { UpdateBurger } from './features/admin/burger/update-burger/update-burger';
import { UpdateDrink } from './features/admin/drink/update-drink/update-drink';
import { CreateSide } from './features/admin/side/create-side/create-side';
import { UpdateSide } from './features/admin/side/update-side/update-side';

export const routes: Routes = [
    {path: '', component: PublicView, pathMatch: 'full'},
    

    {path: 'admin', component: AdminView,
        children: [
            {path:'', component: AdminMenuItems},
            {path: 'create-burger', component:CreateBurger},
            {path: 'update-burger/:id', component:UpdateBurger},
            {path: 'create-drink', component:CreateDrink},
            {path: 'update-drink/:id', component:UpdateDrink},
            {path: 'create-side', component:CreateSide},
            {path: 'update-side/:id', component:UpdateSide}
            
        ]
    },
    {path: '**', redirectTo: ''}
];
