package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.component.MenuItemHelper;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.repository.menu.BurgerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BurgerService {

    private final BurgerRepository burgerRepository;
    private final MenuItemHelper menuItemHelper;

    public BurgerService(BurgerRepository burgerRepository, MenuItemHelper itemHelper){
        this.burgerRepository = burgerRepository;
        this.menuItemHelper = itemHelper;
    }

    public Burger createBurger(Burger burger, MultipartFile image){
        menuItemHelper.validate(burger);

        if (image != null && !image.isEmpty()){
            menuItemHelper.saveImage(image);
            burger.setImageUrl(image.getOriginalFilename());
        }

        return burgerRepository.save(burger);
    }

    public Burger getBurger(Long id){
        return burgerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));
    }

    public List<Burger> getFilteredBurgers(Boolean vegan, Boolean chicken, Boolean lactoseFree){
        vegan = Boolean.TRUE.equals(vegan);
        chicken = Boolean.TRUE.equals(chicken);
        lactoseFree = Boolean.TRUE.equals(lactoseFree);

        if (vegan && chicken) return new ArrayList<>();

        if (vegan) {
            return burgerRepository.findByIsVeganTrue();
        }

        if (chicken && lactoseFree){
            return burgerRepository.findByIsChickenTrueAndIsLactoseFreeTrue();
        } else if (chicken) {
            return burgerRepository.findByIsChickenTrue();
        }

        if (lactoseFree) {
            return burgerRepository.findByIsLactoseFreeTrue();
        }

        return burgerRepository.findAll();
    }

    public Burger updateBurger(Long id, Burger newBurgerDetails, MultipartFile image){
        menuItemHelper.validate(newBurgerDetails);

        Burger oldBurgerDetails = burgerRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));

        if(image != null && !image.isEmpty()){
            menuItemHelper.replaceImage(oldBurgerDetails.getImageUrl(), image);
            oldBurgerDetails.setImageUrl(image.getOriginalFilename());
        }

        oldBurgerDetails.setName(newBurgerDetails.getName());
        oldBurgerDetails.setDescription(newBurgerDetails.getDescription());
        oldBurgerDetails.setBasePrice(newBurgerDetails.getBasePrice());
        oldBurgerDetails.setIsVegan(newBurgerDetails.getIsVegan());
        oldBurgerDetails.setIsChicken(newBurgerDetails.getIsChicken());
        oldBurgerDetails.setIsLactoseFree(newBurgerDetails.getIsLactoseFree());

        return burgerRepository.save(oldBurgerDetails);
    }

    public Map<String, Boolean> deleteBurger(Long id){
        Burger burger = burgerRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));

        if(burger.getImageUrl() != null && !burger.getImageUrl().isEmpty()){
            menuItemHelper.deleteImage(burger.getImageUrl());
        }

        burgerRepository.delete(burger);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }
}