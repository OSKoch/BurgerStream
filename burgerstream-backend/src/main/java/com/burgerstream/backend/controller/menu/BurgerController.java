package com.burgerstream.backend.controller.menu;

import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.service.menu.BurgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/BurgerStream/menu/burgers")
public class BurgerController{

    private final BurgerService burgerService;

    public BurgerController(BurgerService burgerService){
        this.burgerService = burgerService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Burger createBurger(@RequestPart("burger") Burger burger, @RequestPart(value = "image", required = false)MultipartFile image) {
        return burgerService.createBurger(burger, image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Burger> getBurger(@PathVariable Long id){
        return ResponseEntity.ok(burgerService.getBurger(id));
    }

    @GetMapping
    public List<Burger> getFilteredBurgers(
            @RequestParam(required = false) Boolean vegan,
            @RequestParam(required = false) Boolean chicken,
            @RequestParam(required = false) Boolean lactoseFree
    ) {
        return burgerService.getFilteredBurgers(vegan, chicken, lactoseFree);
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Burger> updateBurger(@PathVariable Long id,
                                               @RequestPart("burger") Burger newBurgerDetails,
                                               @RequestPart(value = "image", required = false) MultipartFile image){
        return ResponseEntity.ok(burgerService.updateBurger(id, newBurgerDetails, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBurger(@PathVariable Long id){
        return ResponseEntity.ok(burgerService.deleteBurger(id));
    }
}