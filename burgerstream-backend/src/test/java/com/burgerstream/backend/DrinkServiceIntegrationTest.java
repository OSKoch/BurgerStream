package com.burgerstream.backend;

import com.burgerstream.backend.component.MenuItemValidator;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.DrinkRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import com.burgerstream.backend.service.menu.DrinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({DrinkService.class, MenuItemValidator.class})
public class DrinkServiceIntegrationTest {

    @Autowired
    private DrinkService drinkService;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private SizeOptionRepository sizeOptionRepository;

    private Drink drink;

    @BeforeEach
    void setUp(){
        drink = new Drink();
        drink.setName("Soda Pop");
        drink.setBasePrice(BigDecimal.valueOf(15.00));
    }

    @Test
    void createDrink_withCorrectAttributes_drinkSaved(){
        Drink savedDrink = drinkService.createDrink(drink);

        assertThat(savedDrink.getId()).isNotNull();
        assertThat(savedDrink.getName()).isEqualTo("Soda Pop");
    }

    @Test
    void createDrink_InvalidAttributes_throwsIllegalArgumentException(){
        drink = new Drink();
        assertThatThrownBy( () -> drinkService.createDrink(drink))
                .isInstanceOf(IllegalArgumentException.class);
        drink.setName("Soda Pop");
        assertThatThrownBy( () -> drinkService.createDrink(drink))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getFilteredDrinks_AllParametersFalse_getsAllDrinks(){
        setupMixedDrinks();
        List<Drink> allDrinks = drinkService.getFilteredDrinks(false,false);

        assertThat(allDrinks).hasSize(6);
    }

    @Test
    void getFilteredDrinks_CarbonatedTrue_getAllCarbonatedDrinks(){
        setupMixedDrinks();
        List<Drink> allCarbonatedDrinks = drinkService.getFilteredDrinks(true,false);

        assertThat(allCarbonatedDrinks).hasSize(4);
        assertThat(allCarbonatedDrinks.stream().allMatch(Drink::getCarbonated)).isTrue();
    }

    @Test
    void getFilteredDrinks_lactoseFreeTrue_getAllLactoseFreeDrinks(){
        setupMixedDrinks();
        List<Drink> allLactoseFreeDrinks = drinkService.getFilteredDrinks(false,true);

        assertThat(allLactoseFreeDrinks).hasSize(3);
        assertThat(allLactoseFreeDrinks.stream().allMatch(Drink::getLactoseFree)).isTrue();
    }

    @Test
    void getFilteredDrinks_AllTrue_getAllLactoseFreeAndCarbonatedDrinks(){
        setupMixedDrinks();
        List<Drink> allCarbonatedAndLactoseFreeDrinks = drinkService.getFilteredDrinks(true, true);

        assertThat(allCarbonatedAndLactoseFreeDrinks).hasSize(1);
        assertThat(allCarbonatedAndLactoseFreeDrinks)
                .allMatch(Drink::getCarbonated)
                .allMatch(Drink::getLactoseFree);
    }

    @Test
    void getDrink_validId_getCorrectDrink(){
        drink = drinkRepository.save(drink);

        Drink foundDrink = drinkService.getDrink(drink.getId());

        assertThat(foundDrink).isEqualTo(drink);
    }

    @Test
    void getDrink_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> drinkService.getDrink(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateDrink_validIdAndUpdateDrink_drinkUpdated(){
        drink = drinkRepository.save(drink);
        Long drinkId = drink.getId();

        Drink newDrinkDetails = new Drink();
        newDrinkDetails.setName("Fizzy Drink");
        newDrinkDetails.setBasePrice(BigDecimal.valueOf(20.00));
        Drink updateDrink = drinkService.updateDrink(drinkId,newDrinkDetails);

        assertThat(updateDrink.getName()).isEqualTo("Fizzy Drink");
        assertThat(updateDrink.getBasePrice()).isEqualTo(BigDecimal.valueOf(20.00));
    }

    @Test
    void updateDrink_invalidIdAndUpdateDrink_throwsResourceNotFoundException(){
        Drink newDrinkDetails = new Drink();
        newDrinkDetails.setName("Fizzy Drink");
        newDrinkDetails.setBasePrice(BigDecimal.valueOf(20.00));

        assertThatThrownBy(() -> drinkService.updateDrink(999L, newDrinkDetails))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateDrink_validIdAndNoUpdateDrink_throwsIllegalArgumentException(){
        drink = drinkRepository.save(drink);
        Long drinkId = drink.getId();
        Drink newDrinkDetails = new Drink();

        assertThatThrownBy(() -> drinkService.updateDrink(drinkId, newDrinkDetails))
                .isInstanceOf(IllegalArgumentException.class);

        newDrinkDetails.setName("Fizzy Drink");
        assertThatThrownBy(() -> drinkService.updateDrink(drinkId, newDrinkDetails))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteDrink_validId_drinkDeleted(){
        drink = drinkRepository.save(drink);
        Long drinkId = drink.getId();
        assertThat(drinkRepository.findAll()).hasSize(1);

        drinkService.deleteDrink(drinkId);

        assertThat(drinkRepository.findAll()).isEmpty();
    }

    @Test
    void deleteDrink_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy(() -> drinkService.deleteDrink(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getDrinkSizes_validId_getSizes(){
        drink.setSizeOptions(setUpSizes());
        drink = drinkRepository.save(drink);

        Set<SizeOption> drinkSizes = drinkService.getDrinkSizes(drink.getId());

        assertThat(drinkSizes).size().isEqualTo(3);
        assertThat(drinkSizes).isEqualTo(drink.getSizeOptions());
    }

    @Test
    void getDrinkSizes_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> drinkService.getDrinkSizes(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addDrinkSizeOption_validIds_sizeIsAddedToDrink(){
        drink.setSizeOptions(setUpSizes());
        drink = drinkRepository.save(drink);
        assertThat(drink.getSizeOptions().size()).isEqualTo(3);

        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Soda");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);

        drink = drinkService.addDrinkSizeOption(drink.getId(),extraLarge.getId());

        assertThat(drink.getSizeOptions().size()).isEqualTo(4);
        assertThat(drink.getSizeOptions()).contains(extraLarge);
    }

    @Test
    void addDrinkSizeOption_invalidId_throwsResourceNotFoundException(){
        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Soda");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);
        Long sizeId = extraLarge.getId();

        assertThatThrownBy( () -> drinkService.addDrinkSizeOption(999L, sizeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addDrinkSizeOption_invalidSizeId_throwsResourceNotFoundException(){
        drink = drinkRepository.save(drink);

        assertThatThrownBy( () -> drinkService.addDrinkSizeOption(drink.getId(), 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void removeDrinkSizeOption_validIds_sizeIsAddedToDrink(){
        drink.setSizeOptions(setUpSizes());
        drink = drinkRepository.save(drink);
        assertThat(drink.getSizeOptions().size()).isEqualTo(3);

        SizeOption medium = drink.getSizeOptions().stream()
                .filter(size -> "Medium".equals(size.getSizeLabel()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Could not find the size"));
        drink = drinkService.removeDrinkSizeOption(drink.getId(),medium.getId());

        assertThat(drink.getSizeOptions().size()).isEqualTo(2);
        assertThat(drink.getSizeOptions()).doesNotContain(medium);
    }

    @Test
    void removeDrinkSizeOption_invalidId_throwsResourceNotFoundException(){
        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Soda");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);
        Long sizeId = extraLarge.getId();

        assertThatThrownBy( () -> drinkService.removeDrinkSizeOption(999L, sizeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeDrinkSizeOption_invalidSizeId_throwsResourceNotFoundException(){
        drink = drinkRepository.save(drink);

        assertThatThrownBy( () -> drinkService.removeDrinkSizeOption(drink.getId(), 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }



    private void setupMixedDrinks() {
        for (int i = 0; i < 3; i++) {
            Drink drink = new Drink();
            drink.setName("Carbonated Drink: " + (i + 1)); // added parentheses to fix concatenation order
            drink.setBasePrice(BigDecimal.valueOf(20.00));
            drink.setCarbonated(true);
            drinkRepository.save(drink);
        }

        for (int i = 0; i < 2; i++) {
            Drink drink = new Drink();
            drink.setName("Lactose Free Drink: " + (i + 1));
            drink.setBasePrice(BigDecimal.valueOf(25.00));
            drink.setLactoseFree(true);
            drinkRepository.save(drink);
        }

        Drink hybrid = new Drink();
        hybrid.setName("Hybrid Drink");
        hybrid.setBasePrice(BigDecimal.valueOf(35.00));
        hybrid.setCarbonated(true);
        hybrid.setLactoseFree(true);
        drinkRepository.save(hybrid);
    }

    private Set<SizeOption> setUpSizes(){
        SizeOption small = new SizeOption();
        small.setLabel("Soda");
        small.setSizeLabel("Small");
        small.setExtraPrice(BigDecimal.valueOf(0.00));
        small = sizeOptionRepository.save(small);

        SizeOption medium = new SizeOption();
        medium.setLabel("Soda");
        medium.setSizeLabel("Medium");
        medium.setExtraPrice(BigDecimal.valueOf(5.00));
        medium = sizeOptionRepository.save(medium);

        SizeOption large = new SizeOption();
        large.setLabel("Soda");
        large.setSizeLabel("Large");
        large.setExtraPrice(BigDecimal.valueOf(10.00));
        large = sizeOptionRepository.save(large);

        List<SizeOption> sizes = new ArrayList<>();
        return new HashSet<>(Arrays.asList(small, medium, large));
    }
}