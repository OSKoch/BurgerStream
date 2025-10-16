package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.MenuItem;
import com.burgerstream.backend.repository.menu.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository){
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems(){
        return menuItemRepository.findAll();
    }

    public MenuItem getMenuItem(Long id){
        return menuItemRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Menu Item with id: " + id + " does not exist"));
    }
}