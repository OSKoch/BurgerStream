package com.burgerstream.backend.repository.menu;

import com.burgerstream.backend.model.menu.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
    List<Drink> findByIsCarbonatedTrue();

    List<Drink> findByIsLactoseFreeTrue();

    List<Drink> findByIsCarbonatedTrueAndIsLactoseFreeTrue();
}