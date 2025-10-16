package com.burgerstream.backend.controller.menu;

import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.service.menu.BurgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/BurgerStream/menu/burgers")
public class BurgerController{

    private final BurgerService burgerService;

    public BurgerController(BurgerService burgerService){
        this.burgerService = burgerService;
    }

    @PostMapping
    public Burger createBurger(@RequestBody Burger burger) {return burgerService.createBurger(burger);}

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

    @PutMapping("/burgers/{id}")
    public ResponseEntity<Burger> updateBurger(@PathVariable Long id, @RequestBody Burger newBurgerDetails){
        return ResponseEntity.ok(burgerService.updateBurger(id, newBurgerDetails));
    }

    @DeleteMapping("/burgers/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBurger(@PathVariable Long id){
        return ResponseEntity.ok(burgerService.deleteBurger(id));
    }
}