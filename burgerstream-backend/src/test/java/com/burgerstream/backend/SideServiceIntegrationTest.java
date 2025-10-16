package com.burgerstream.backend;

import com.burgerstream.backend.component.MenuItemValidator;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import com.burgerstream.backend.service.menu.SideService;
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
@Import({SideService.class, MenuItemValidator.class})
public class SideServiceIntegrationTest {
    
    @Autowired
    private SideService sideService;
    
    @Autowired
    private SideRepository sideRepository;

    @Autowired
    private SizeOptionRepository sizeOptionRepository;
    
    private Side side;

    @BeforeEach
    void setUp(){
        side = new Side();
        side.setName("French Fries");
        side.setBasePrice(BigDecimal.valueOf(25.00));
    }

    @Test
    void createSide_withCorrectAttributes_sideSaved(){
        Side savedSide = sideService.createSide(side);

        assertThat(savedSide.getId()).isNotNull();
        assertThat(savedSide.getName()).isEqualTo("French Fries");
    }

    @Test
    void createSide_InvalidAttributes_throwsIllegalArgumentException(){
        side = new Side();
        assertThatThrownBy( () -> sideService.createSide(side))
                .isInstanceOf(IllegalArgumentException.class);
        side.setName("French Fries");
        assertThatThrownBy( () -> sideService.createSide(side))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getFilteredSides_AllParametersFalse_getsAllSides(){
        setupMixedSides();
        List<Side> allSides = sideService.getFilteredSides(false);

        assertThat(allSides).hasSize(5);
    }

    @Test
    void getFilteredSides_shareableTrue_getAllShareableSides(){
        setupMixedSides();
        List<Side> allShareableSides = sideService.getFilteredSides(true);

        assertThat(allShareableSides).hasSize(3);
        assertThat(allShareableSides.stream().allMatch(Side::getShareable)).isTrue();
    }

    @Test
    void getSide_validId_getCorrectSide(){
        side = sideRepository.save(side);

        Side foundSide = sideService.getSide(side.getId());

        assertThat(foundSide).isEqualTo(side);
    }

    @Test
    void getSide_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> sideService.getSide(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateSide_validIdAndUpdateSide_sideUpdated(){
        side = sideRepository.save(side);
        Long sideId = side.getId();

        Side newSideDetails = new Side();
        newSideDetails.setName("Curly Fries");
        newSideDetails.setBasePrice(BigDecimal.valueOf(30.00));
        Side updateSide = sideService.updateSide(sideId,newSideDetails);

        assertThat(updateSide.getName()).isEqualTo("Curly Fries");
        assertThat(updateSide.getBasePrice()).isEqualTo(BigDecimal.valueOf(30.00));
    }

    @Test
    void updateSide_invalidIdAndUpdateSide_throwsResourceNotFoundException(){
        Side newSideDetails = new Side();
        newSideDetails.setName("Curly Fries");
        newSideDetails.setBasePrice(BigDecimal.valueOf(30.00));

        assertThatThrownBy(() -> sideService.updateSide(999L, newSideDetails))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateSide_validIdAndNoUpdateSide_throwsIllegalArgumentException(){
        side = sideRepository.save(side);
        Long sideId = side.getId();
        Side newSideDetails = new Side();

        assertThatThrownBy(() -> sideService.updateSide(sideId, newSideDetails))
                .isInstanceOf(IllegalArgumentException.class);

        newSideDetails.setName("Curly Fries");
        assertThatThrownBy(() -> sideService.updateSide(sideId, newSideDetails))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteSide_validId_sideDeleted(){
        side = sideRepository.save(side);
        Long sideId = side.getId();
        assertThat(sideRepository.findAll()).hasSize(1);

        sideService.deleteSide(sideId);

        assertThat(sideRepository.findAll()).isEmpty();
    }

    @Test
    void deleteSide_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy(() -> sideService.deleteSide(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getSideSizes_validId_getSizes(){
        side.setSizeOptions(setUpSizes());
        side = sideRepository.save(side);

        Set<SizeOption> sideSizes = sideService.getSideSizes(side.getId());

        assertThat(sideSizes).size().isEqualTo(3);
        assertThat(sideSizes).isEqualTo(side.getSizeOptions());
    }

    @Test
    void getSideSizes_invalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> sideService.getSideSizes(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addSideSizeOption_validIds_sizeIsAddedToSide(){
        side.setSizeOptions(setUpSizes());
        side = sideRepository.save(side);
        assertThat(side.getSizeOptions().size()).isEqualTo(3);

        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Fries");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);

        side = sideService.addSideSizeOption(side.getId(),extraLarge.getId());

        assertThat(side.getSizeOptions().size()).isEqualTo(4);
        assertThat(side.getSizeOptions()).contains(extraLarge);
    }

    @Test
    void addSideSizeOption_invalidId_throwsResourceNotFoundException(){
        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Fries");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);
        Long sizeId = extraLarge.getId();

        assertThatThrownBy( () -> sideService.addSideSizeOption(999L, sizeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addSideSizeOption_invalidSizeId_throwsResourceNotFoundException(){
        side = sideRepository.save(side);

        assertThatThrownBy( () -> sideService.addSideSizeOption(side.getId(), 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeSideSizeOption_validIds_sizeIsAddedToSide(){
        side.setSizeOptions(setUpSizes());
        side = sideRepository.save(side);
        assertThat(side.getSizeOptions().size()).isEqualTo(3);

        SizeOption medium = side.getSizeOptions().stream()
                .filter(size -> "Medium".equals(size.getSizeLabel()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Could not find the size"));
        side = sideService.removeSideSizeOption(side.getId(),medium.getId());

        assertThat(side.getSizeOptions().size()).isEqualTo(2);
        assertThat(side.getSizeOptions()).doesNotContain(medium);
    }

    @Test
    void removeSideSizeOption_invalidId_throwsResourceNotFoundException(){
        SizeOption extraLarge = new SizeOption();
        extraLarge.setLabel("Fries");
        extraLarge.setSizeLabel("X-Large");
        extraLarge.setExtraPrice(BigDecimal.valueOf(10.00));
        extraLarge = sizeOptionRepository.save(extraLarge);
        Long sizeId = extraLarge.getId();

        assertThatThrownBy( () -> sideService.removeSideSizeOption(999L, sizeId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeSideSizeOption_invalidSizeId_throwsResourceNotFoundException(){
        side = sideRepository.save(side);

        assertThatThrownBy( () -> sideService.removeSideSizeOption(side.getId(), 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private void setupMixedSides() {
        for (int i = 0; i < 3; i++) {
            Side side = new Side();
            side.setName("Shareable side: " + (i + 1)); // added parentheses to fix concatenation order
            side.setBasePrice(BigDecimal.valueOf(20.00));
            side.setShareable(true);
            sideRepository.save(side);
        }

        for (int i = 0; i < 2; i++) {
            Side side = new Side();
            side.setName("NoneShareable side: " + (i + 1));
            side.setBasePrice(BigDecimal.valueOf(15.00));
            sideRepository.save(side);
        }
    }

    private Set<SizeOption> setUpSizes(){
        SizeOption small = new SizeOption();
        small.setLabel("Fries");
        small.setSizeLabel("Small");
        small.setExtraPrice(BigDecimal.valueOf(0.00));
        small = sizeOptionRepository.save(small);

        SizeOption medium = new SizeOption();
        medium.setLabel("Fries");
        medium.setSizeLabel("Medium");
        medium.setExtraPrice(BigDecimal.valueOf(5.00));
        medium = sizeOptionRepository.save(medium);

        SizeOption large = new SizeOption();
        large.setLabel("Fries");
        large.setSizeLabel("Large");
        large.setExtraPrice(BigDecimal.valueOf(10.00));
        large = sizeOptionRepository.save(large);

        List<SizeOption> sizes = new ArrayList<>();
        return new HashSet<>(Arrays.asList(small, medium, large));
    }
}