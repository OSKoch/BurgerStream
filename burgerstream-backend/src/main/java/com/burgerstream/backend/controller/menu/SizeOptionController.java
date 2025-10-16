package com.burgerstream.backend.controller.menu;

import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.service.menu.SizeOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/BurgerStream/menu/sizes")
public class SizeOptionController {

    private final SizeOptionService sizeOptionService;

    public SizeOptionController(SizeOptionService sizeOptionService){
        this.sizeOptionService = sizeOptionService;
    }

    @PostMapping
    public SizeOption createSizeOption(@RequestBody SizeOption sizeOption){
        return sizeOptionService.createSizeOption(sizeOption);
    }

    @GetMapping
    public List<SizeOption> getAllSizeOptions(){
        return sizeOptionService.getAllSizeOptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SizeOption> getSizeOption(@PathVariable Long id){
        return ResponseEntity.ok(sizeOptionService.getSizeOption(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SizeOption> updateSizeOption(@PathVariable Long id, @RequestBody SizeOption newSizeDetails){
        return ResponseEntity.ok(sizeOptionService.updateSizeOption(id, newSizeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSizeOption(@PathVariable Long id){
        return ResponseEntity.ok(sizeOptionService.deleteSizeOption(id));
    }

    @PutMapping("/{id}/assign/drinks")
    public ResponseEntity<SizeOption> assignSizeToDrinks(@PathVariable Long id, @RequestBody List<Long> drinkIds){
        return ResponseEntity.ok(sizeOptionService.assignSizeToDrinks(id, drinkIds));
    }

    @PutMapping("/{id}/assign/sides")
    public ResponseEntity<SizeOption> assignSizeToSides(@PathVariable Long id, @RequestBody List<Long> sideIds){
        return ResponseEntity.ok(sizeOptionService.assignSizeToSides(id, sideIds));
    }
}