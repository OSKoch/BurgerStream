package com.burgerstream.backend.controller.menu;

import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.service.menu.SideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/v1/BurgerStream/menu/sides")
public class SideController {

    private final SideService sideService;

    public SideController(SideService sideService) {
        this.sideService = sideService;
    }

    @PostMapping
    public Side createSide(@RequestBody Side side) {
        return sideService.createSide(side);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Side> getSide(@PathVariable Long id) {
        return ResponseEntity.ok(sideService.getSide(id));
    }

    @GetMapping
    public List<Side> getFilteredSides(
            @RequestParam(required = false) Boolean shareable
    ) {
        return sideService.getFilteredSides(shareable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Side> updateSide(@PathVariable Long id, @RequestBody Side newSideDetails){
        return ResponseEntity.ok(sideService.updateSide(id, newSideDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSide(@PathVariable Long id){
        return ResponseEntity.ok(sideService.deleteSide(id));
    }

    @GetMapping("/{id}/sizes")
    public Set<SizeOption> getSideSizes(@PathVariable Long id){
        return sideService.getSideSizes(id);
    }

    @PutMapping("/{id}/sizes/{sizeId}")
    public ResponseEntity<Side> addSideSizeOption(@PathVariable Long id, @PathVariable Long sizeId){
        return ResponseEntity.ok(sideService.addSideSizeOption(id, sizeId));
    }

    @DeleteMapping("/{id}/sizes/{sizeId}")
    public ResponseEntity<Side> removeSideSizeOption(@PathVariable Long id, @PathVariable Long sizeId){
        return ResponseEntity.ok(sideService.removeSideSizeOption(id, sizeId));
    }
}