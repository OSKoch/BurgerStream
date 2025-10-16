package com.burgerstream.backend;

import com.burgerstream.backend.exception.InvalidSizeOptionException;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.DrinkRepository;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import com.burgerstream.backend.service.menu.SizeOptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import(SizeOptionService.class)
public class SizeOptionServiceIntegrationTest {

    @Autowired
    private SizeOptionService sizeOptionService;

    @Autowired
    private SizeOptionRepository sizeOptionRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private SideRepository sideRepository;

    private SizeOption size;

    @BeforeEach
    void setUp(){
        size = new SizeOption();
        size.setLabel("Hot Drink");
        size.setSizeLabel("Medium");
        size.setExtraPrice(BigDecimal.valueOf(10.00));
    }

    @Test
    void createSizeOption_validInput_SavesSize(){
        SizeOption savedSize = sizeOptionService.createSizeOption(size);

        assertThat(savedSize.getId()).isNotNull();
        assertThat(savedSize.getLabel()).isEqualTo("Hot Drink");
        assertThat(savedSize.getSizeLabel()).isEqualTo("Medium");
        assertThat(savedSize.getExtraPrice()).isEqualTo(BigDecimal.valueOf(10.00));
    }

    @Test
    void createSizeOption_missingAttributes_throwsInvalidSizeOptionException(){
        size = new SizeOption();
        assertThatThrownBy( () -> sizeOptionService.createSizeOption(size))
                .isInstanceOf(InvalidSizeOptionException.class);

        size.setLabel("Hot Drink");
        assertThatThrownBy( () -> sizeOptionService.createSizeOption(size))
                .isInstanceOf(InvalidSizeOptionException.class);

        size.setSizeLabel("Medium");
        assertThatThrownBy( () -> sizeOptionService.createSizeOption(size))
                .isInstanceOf(InvalidSizeOptionException.class);
    }

    @Test
    void getSizeOption_withValidIdea_findsCorrectSize(){
        size = sizeOptionRepository.save(size);

        SizeOption savedSize = sizeOptionService.getSizeOption(size.getId());

        assertThat(savedSize).isEqualTo(size);
    }

    @Test
    void getSizeOption_withInvalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> sizeOptionService.getSizeOption(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateSizeOption_attributesFilledOut_UpdatesCorrect(){

        size = sizeOptionRepository.save(size);
        Long sizeId = size.getId();
        SizeOption newSizeDetails = new SizeOption();
        newSizeDetails.setLabel("Cold Drink");
        newSizeDetails.setSizeLabel("Small");
        newSizeDetails.setExtraPrice(BigDecimal.valueOf(20.00));

        SizeOption updatedSize = sizeOptionService.updateSizeOption(size.getId(), newSizeDetails);

        assertThat(updatedSize.getLabel()).isEqualTo("Cold Drink");
        assertThat(updatedSize.getSizeLabel()).isEqualTo("Small");
        assertThat(updatedSize.getExtraPrice()).isEqualTo(BigDecimal.valueOf(20.00));
    }

    @Test
    void updateSizeOption_missingAttributes_throwsInvalidSizeOptionException(){

        size = sizeOptionRepository.save(size);
        Long sizeId = size.getId();

        SizeOption newSize = new SizeOption();
        assertThatThrownBy( () -> sizeOptionService.updateSizeOption(sizeId,newSize))
                .isInstanceOf(InvalidSizeOptionException.class);

        newSize.setLabel("Cold Drink");
        assertThatThrownBy( () -> sizeOptionService.updateSizeOption(sizeId, newSize))
                .isInstanceOf(InvalidSizeOptionException.class);

        newSize.setSizeLabel("Small");
        assertThatThrownBy( () -> sizeOptionService.updateSizeOption(sizeId, newSize))
                .isInstanceOf(InvalidSizeOptionException.class);
    }

    @Test
    void deleteSizeOption_validId_sizeOptionDeleted(){
        size = sizeOptionRepository.save(size);
        Long sizeId = size.getId();

        assertThat(sizeOptionRepository.findById(sizeId)).isPresent();

        sizeOptionService.deleteSizeOption(sizeId);

        assertThat(sizeOptionRepository.findAll()).isEmpty();
    }

    @Test
    void deleteSizeOption_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> sizeOptionService.deleteSizeOption(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void assignSizeToDrinks_DrinksHaveNewSize(){
        Drink drink = new Drink();
        drink.setName("Soda Pop");
        drink.setBasePrice(BigDecimal.valueOf(25.00));
        drinkRepository.save(drink);

        drink = new Drink();
        drink.setName("Fizzy Drink");
        drink.setBasePrice(BigDecimal.valueOf(25.00));
        drinkRepository.save(drink);

        assertThat(drinkRepository.findAll()).hasSize(2);

        size = sizeOptionRepository.save(size);
        Long sizeId = size.getId();

        List<Long> drinkIds = drinkRepository.findAll().stream().map(Drink::getId).toList();

        sizeOptionService.assignSizeToDrinks(sizeId, drinkIds);

        List<Drink> updatedDrinks = drinkRepository.findAll();

        for (Drink d : updatedDrinks) {
            assertThat(d.getSizeOptions())
                    .as("Drink with id " + d.getId() + " should have the new size")
                    .contains(size);
        }
    }

    @Test
    void assignSizeToDrinks_withInvalidSizeId_throwsResourceNotFoundException() {
        List<Long> drinkIds = List.of(1L, 2L);
        assertThatThrownBy(() -> sizeOptionService.assignSizeToDrinks(999L, drinkIds))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void assignSizeToDrinks_withNoDrinkIds_throwsIllegalArgumentException(){
        List<Long> drinkIds = new ArrayList<>();
        assertThatThrownBy(()-> sizeOptionService.assignSizeToDrinks(999L, drinkIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void assignSizeToSides_SidesHaveNewSize(){
        Side side = new Side();
        side.setName("French Fries");
        side.setBasePrice(BigDecimal.valueOf(25.00));
        sideRepository.save(side);

        side = new Side();
        side.setName("Curly Fries");
        side.setBasePrice(BigDecimal.valueOf(25.00));
        sideRepository.save(side);

        assertThat(sideRepository.findAll()).hasSize(2);

        size = sizeOptionRepository.save(size);
        Long sizeId = size.getId();

        List<Long> sideIds = sideRepository.findAll().stream().map(Side::getId).toList();

        sizeOptionService.assignSizeToSides(sizeId, sideIds);

        List<Side> updatedSides = sideRepository.findAll();

        for (Side s : updatedSides) {
            assertThat(s.getSizeOptions())
                    .as("Side with id " + s.getId() + " should have the new size")
                    .contains(size);
        }
    }

    @Test
    void assignSizeToSides_withInvalidSizeId_throwsResourceNotFoundException() {
        List<Long> sideIds = List.of(1L, 2L);
        assertThatThrownBy(() -> sizeOptionService.assignSizeToSides(999L, sideIds))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void assignSizeToSides_withNoSideIds_throwsIllegalArgumentException(){
        List<Long> sideIds = new ArrayList<>();
        assertThatThrownBy(() -> sizeOptionService.assignSizeToSides(999L, sideIds))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
