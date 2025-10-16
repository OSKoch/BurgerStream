package com.burgerstream.backend.repository.menu;

import com.burgerstream.backend.model.menu.Burger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BurgerRepository extends JpaRepository<Burger, Long> {
    List<Burger> findByIsVeganTrue();

    List<Burger> findByIsChickenTrue();

    List<Burger> findByIsLactoseFreeTrue();

    List<Burger> findByIsChickenTrueAndIsLactoseFreeTrue();
}