package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.component.MenuItemHelper;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.DrinkRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final SizeOptionRepository sizeOptionRepository;
    private final MenuItemHelper menuItemHelper;

    public DrinkService(DrinkRepository drinkRepository,
                        SizeOptionRepository sizeOptionRepository,
                        MenuItemHelper itemHelper){
        this.drinkRepository = drinkRepository;
        this.sizeOptionRepository = sizeOptionRepository;
        this.menuItemHelper = itemHelper;
    }

    public Drink createDrink(Drink drink, MultipartFile image){
        menuItemHelper.validate(drink);

        if (image != null && !image.isEmpty()){
            menuItemHelper.saveImage(image);
            drink.setImageUrl(image.getOriginalFilename());
        }

        return drinkRepository.save(drink);
    }

    public List<Drink> getFilteredDrinks(Boolean carbonated, Boolean lactoseFree){
        if (Boolean.TRUE.equals(carbonated) && Boolean.TRUE.equals(lactoseFree)){
            return drinkRepository.findByIsCarbonatedTrueAndIsLactoseFreeTrue();

        } else if (Boolean.TRUE.equals(carbonated)) {
            return drinkRepository.findByIsCarbonatedTrue();

        } else if (Boolean.TRUE.equals(lactoseFree)) {
            return drinkRepository.findByIsLactoseFreeTrue();

        } else {
            return drinkRepository.findAll();
        }
    }

    public Drink getDrink(Long id){
        return drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));
    }

    public Drink updateDrink(Long id, Drink newDrinkDetails, MultipartFile image){
        menuItemHelper.validate(newDrinkDetails);

        Drink oldDrinkDetails = drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));

        if(image != null && !image.isEmpty()){
            menuItemHelper.replaceImage(oldDrinkDetails.getImageUrl(), image);
            oldDrinkDetails.setImageUrl(image.getOriginalFilename());
        }

        oldDrinkDetails.setName(newDrinkDetails.getName());
        oldDrinkDetails.setDescription(newDrinkDetails.getDescription());
        oldDrinkDetails.setBasePrice(newDrinkDetails.getBasePrice());
        oldDrinkDetails.setSizeOptions(newDrinkDetails.getSizeOptions());
        oldDrinkDetails.setIsCarbonated(newDrinkDetails.getIsCarbonated());
        oldDrinkDetails.setIsLactoseFree(newDrinkDetails.getIsLactoseFree());

        return drinkRepository.save(oldDrinkDetails);
    }

    public Map<String, Boolean> deleteDrink(Long id){
        Drink drink = drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));

        if(drink.getImageUrl() != null && !drink.getImageUrl().isEmpty()){
            menuItemHelper.deleteImage(drink.getImageUrl());
        }

        drinkRepository.delete(drink);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }

    public Set<SizeOption> getDrinkSizes(Long id){
        Drink drink = drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));
        return drink.getSizeOptions();
    }

    public Drink addDrinkSizeOption(Long id, Long sizeId){
        Drink drink = drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId)
                .orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        drink.getSizeOptions().add(size);

        return drinkRepository.save(drink);
    }

    public Drink removeDrinkSizeOption(Long id, Long sizeId){
        Drink drink = drinkRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Drink with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId)
                .orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        drink.getSizeOptions().remove(size);

        return drinkRepository.save(drink);
    }

}