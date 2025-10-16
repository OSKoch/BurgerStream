package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.component.MenuItemValidator;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.repository.menu.BurgerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BurgerService {

    private final BurgerRepository burgerRepository;
    private final MenuItemValidator validator;

    public BurgerService(BurgerRepository burgerRepository, MenuItemValidator validator){
        this.burgerRepository = burgerRepository;
        this.validator = validator;
    }

    public Burger createBurger(Burger burger){
        validator.validate(burger);
        return burgerRepository.save(burger);
    }

    public Burger getBurger(Long id){
        return burgerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));
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

    public Burger updateBurger(Long id, Burger newBurgerDetails){
        Burger oldBurgerDetails = burgerRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));
        validator.validate(newBurgerDetails);

        oldBurgerDetails.setName(newBurgerDetails.getName());
        oldBurgerDetails.setDescription(newBurgerDetails.getDescription());
        oldBurgerDetails.setBasePrice(newBurgerDetails.getBasePrice());
        oldBurgerDetails.setImageURL(newBurgerDetails.getImageURL());
        oldBurgerDetails.setVegan(newBurgerDetails.getVegan());
        oldBurgerDetails.setChicken(newBurgerDetails.getChicken());
        oldBurgerDetails.setLactoseFree(newBurgerDetails.getLactoseFree());


        return burgerRepository.save(oldBurgerDetails);
    }

    public Map<String, Boolean> deleteBurger(Long id){
        Burger burger = burgerRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Burger with id: " + id + " does not exist"));

        burgerRepository.delete(burger);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }
}