package com.burgerstream.backend;

import com.burgerstream.backend.component.MenuItemValidator;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Burger;
import com.burgerstream.backend.repository.menu.BurgerRepository;
import com.burgerstream.backend.service.menu.BurgerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({BurgerService.class, MenuItemValidator.class})
public class BurgerServiceIntegrationTest {

    @Autowired
    private BurgerService burgerService;

    @Autowired
    private BurgerRepository burgerRepository;

    private Burger burger;

    @BeforeEach
    void setUp(){
        burger = new Burger();
        burger.setName("Hamburger");
        burger.setBasePrice(BigDecimal.valueOf(55.00));
    }

    @Test
    void createBurger_withCorrectAttributes_burgerSaved(){
        Burger savedBurger = burgerService.createBurger(burger);

        assertThat(savedBurger.getId()).isNotNull();
        assertThat(savedBurger.getName()).isEqualTo("Hamburger");
    }

    @Test
    void createBurger_InvalidAttributes_throwsIllegalArgumentException(){
        burger = new Burger();
        assertThatThrownBy( () -> burgerService.createBurger(burger))
                .isInstanceOf(IllegalArgumentException.class);
        burger.setName("Hamburger");
        assertThatThrownBy( () -> burgerService.createBurger(burger))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getFilteredBurgers_AllParametersFalse_getsAllBurgers(){
        setupMixedBurgers();
        List<Burger> allBurgers = burgerService.getFilteredBurgers(false,false, false);

        assertThat(allBurgers).hasSize(10);
    }

    @Test
    void getFilteredBurgers_VeganTrue_getAllVeganBurgers(){
        setupMixedBurgers();
        List<Burger> allVeganBurgers = burgerService.getFilteredBurgers(true,false, false);

        assertThat(allVeganBurgers).hasSize(3);
        assertThat(allVeganBurgers.stream().allMatch(Burger::getVegan)).isTrue();
    }

    @Test
    void getFilteredBurgers_chickenTrue_getAllChickenBurgers(){
        setupMixedBurgers();
        List<Burger> allChickenBurgers = burgerService.getFilteredBurgers(false, true, false);

        assertThat(allChickenBurgers).hasSize(5);
        assertThat(allChickenBurgers.stream().allMatch(Burger::getChicken)).isTrue();
    }

    @Test
    void getFilteredBurgers_lactoseFreeTrue_getAllLactoseFreeBurgers(){
        setupMixedBurgers();
        List<Burger> allLactoseFreeBurgers = burgerService.getFilteredBurgers(false, false, true);

        assertThat(allLactoseFreeBurgers).hasSize(6);
        assertThat(allLactoseFreeBurgers.stream().allMatch(Burger::getLactoseFree)).isTrue();
    }

    @Test
    void getFilteredBurgers_LactoseFreeAndChickenTrue_getAllLactoseFreeAndChickenBurgers(){
        setupMixedBurgers();
        List<Burger> allChickenAndLactoseFreeBurgers = burgerService.getFilteredBurgers(false, true, true);

        assertThat(allChickenAndLactoseFreeBurgers).hasSize(1);
        assertThat(allChickenAndLactoseFreeBurgers)
                .allMatch(Burger::getChicken)
                .allMatch(Burger::getLactoseFree);
    }

    @Test
    void getFilteredBurgers_VeganAndChickenTrue_getEmptyList(){
        List<Burger> emptyList = burgerService.getFilteredBurgers(false, true, true);

        assertThat(emptyList).isEmpty();
    }

    @Test
    void getBurgers_validId_getCorrectBurger(){
        burger = burgerRepository.save(burger);

        Burger foundBurger = burgerService.getBurger(burger.getId());

        assertThat(foundBurger).isEqualTo(burger);
    }

    @Test
    void getBurger_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> burgerService.getBurger(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateBurger_validIdAndUpdateBurger_burgerUpdated(){
        burger = burgerRepository.save(burger);
        Long burgerId = burger.getId();

        Burger newBurgerDetails = new Burger();
        newBurgerDetails.setName("Base Burger");
        newBurgerDetails.setBasePrice(BigDecimal.valueOf(55.00));
        Burger updateBurger = burgerService.updateBurger(burgerId,newBurgerDetails);

        assertThat(updateBurger.getName()).isEqualTo("Base Burger");
        assertThat(updateBurger.getBasePrice()).isEqualTo(BigDecimal.valueOf(55.00));
    }

    @Test
    void updateBurger_invalidIdAndUpdateBurger_throwsResourceNotFoundException(){
        Burger newBurgerDetails = new Burger();
        newBurgerDetails.setName("Base Burger");
        newBurgerDetails.setBasePrice(BigDecimal.valueOf(20.00));

        assertThatThrownBy(() -> burgerService.updateBurger(999L, newBurgerDetails))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateBurger_validIdAndNoUpdateBurger_throwsIllegalArgumentException(){
        burger = burgerRepository.save(burger);
        Long burgerId = burger.getId();
        Burger newBurgerDetails = new Burger();

        assertThatThrownBy(() -> burgerService.updateBurger(burgerId, newBurgerDetails))
                .isInstanceOf(IllegalArgumentException.class);

        newBurgerDetails.setName("Base Burger");
        assertThatThrownBy(() -> burgerService.updateBurger(burgerId, newBurgerDetails))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteBurger_validId_burgerDeleted(){
        burger = burgerRepository.save(burger);
        Long burgerId = burger.getId();
        assertThat(burgerRepository.findAll()).hasSize(1);

        burgerService.deleteBurger(burgerId);

        assertThat(burgerRepository.findAll()).isEmpty();
    }

    @Test
    void deleteBurger_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy(() -> burgerService.deleteBurger(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private void setupMixedBurgers() {
        for (int i = 0; i < 3; i++) {
            Burger burger = new Burger();
            burger.setName("Vegan Burger: " + (i + 1));
            burger.setBasePrice(BigDecimal.valueOf(70.00));
            burger.setVegan(true);
            burgerRepository.save(burger);
        }

        for (int i = 0; i < 4; i++) {
            Burger burger = new Burger();
            burger.setName("Chicken Burger: " + (i + 1));
            burger.setBasePrice(BigDecimal.valueOf(65.00));
            burger.setChicken(true);
            burgerRepository.save(burger);
        }

        for (int i = 0; i < 2; i++) {
            Burger burger = new Burger();
            burger.setName("Lactose Free Burger: " + (i + 1));
            burger.setBasePrice(BigDecimal.valueOf(55.00));
            burger.setLactoseFree(true);
            burgerRepository.save(burger);
        }

        Burger hybrid = new Burger();
        hybrid.setName("Hybrid Burger");
        hybrid.setBasePrice(BigDecimal.valueOf(60.00));
        hybrid.setChicken(true);
        hybrid.setLactoseFree(true);
        burgerRepository.save(hybrid);
    }
}