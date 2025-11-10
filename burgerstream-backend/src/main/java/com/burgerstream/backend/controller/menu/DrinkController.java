package com.burgerstream.backend.controller.menu;

import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.service.menu.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/BurgerStream/menu/drinks")
public class DrinkController {
    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService){
        this.drinkService = drinkService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Drink createDrink(@RequestPart("drink") Drink drink, @RequestPart(value = "image", required = false)MultipartFile image) {
        return drinkService.createDrink(drink, image);
    }

    @GetMapping
    public List<Drink> getFilteredDrinks(
            @RequestParam(required = false) Boolean carbonated,
            @RequestParam(required = false) Boolean lactoseFree
    ) {
        return drinkService.getFilteredDrinks(carbonated, lactoseFree);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drink> getDrink(@PathVariable Long id){
        return ResponseEntity.ok(drinkService.getDrink(id));
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Drink> updateDrink(@PathVariable Long id,
                                             @RequestPart("drink") Drink newDrinkDetails,
                                             @RequestPart(value = "image", required = false) MultipartFile image){
        return ResponseEntity.ok(drinkService.updateDrink(id, newDrinkDetails, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteDrink(@PathVariable Long id){
        return ResponseEntity.ok(drinkService.deleteDrink(id));
    }

    @GetMapping("/{id}/sizes")
    public Set<SizeOption> getDrinkSizes(@PathVariable Long id){
        return drinkService.getDrinkSizes(id);
    }

    @PutMapping("/{id}/sizes/{sizeId}")
    public ResponseEntity<Drink> addDrinkSizeOption(@PathVariable Long id, @PathVariable Long sizeId){
        return ResponseEntity.ok(drinkService.addDrinkSizeOption(id, sizeId));
    }

    @DeleteMapping("/{id}/sizes/{sizeId}")
    public ResponseEntity<Drink> removeDrinkSizeOption(@PathVariable Long id, @PathVariable Long sizeId){
        return ResponseEntity.ok(drinkService.removeDrinkSizeOption(id, sizeId));
    }
}