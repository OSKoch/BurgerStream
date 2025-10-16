package com.burgerstream.backend.component;

import com.burgerstream.backend.model.menu.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemValidator {
    public void validate(MenuItem menuItem){
        if (menuItem.getName() == null || menuItem.getName().isBlank()){
            throw new IllegalArgumentException("Menu item must have a name");
        }
        if (menuItem.getBasePrice() == null){
            throw new IllegalArgumentException("Menu item must have a base price");
        }
    }
}