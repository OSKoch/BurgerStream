package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.exception.InvalidSizeOptionException;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Drink;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.DrinkRepository;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SizeOptionService {

    private final SizeOptionRepository sizeOptionRepository;
    private final DrinkRepository drinkRepository;
    private final SideRepository sideRepository;

    public SizeOptionService(SizeOptionRepository sizeOptionRepository, DrinkRepository drinkRepository, SideRepository sideRepository){
        this.sizeOptionRepository = sizeOptionRepository;
        this.drinkRepository = drinkRepository;
        this.sideRepository = sideRepository;
    }

    public List<SizeOption> getAllSizeOptions(){
        return sizeOptionRepository.findAll();
    }

    public SizeOption createSizeOption(SizeOption sizeOption){
        checkForMissingAttributes(sizeOption);
        return sizeOptionRepository.save(sizeOption);
    }

    public SizeOption getSizeOption(Long id){
        return sizeOptionRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + id + " does not exist"));
    }

    public SizeOption updateSizeOption(Long id, SizeOption newSizeDetails){
        checkForMissingAttributes(newSizeDetails);
        SizeOption oldSizeDetails = sizeOptionRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + id + " does not exist"));

        oldSizeDetails.setLabel(newSizeDetails.getLabel());
        oldSizeDetails.setSizeLabel(newSizeDetails.getSizeLabel());
        oldSizeDetails.setExtraPrice(newSizeDetails.getExtraPrice());

        return sizeOptionRepository.save(oldSizeDetails);
    }

    public Map<String, Boolean> deleteSizeOption(Long id){
        SizeOption sizeOption = sizeOptionRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + id + " does not exist"));

        sizeOptionRepository.delete(sizeOption);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }

    public SizeOption assignSizeToDrinks(Long id, List<Long> drinkIds){
        if (drinkIds.isEmpty()) throw new IllegalArgumentException("At least one drink must be provided");
        SizeOption sizeOption = sizeOptionRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + id + " does not exist"));

        List<Drink> drinks = drinkRepository.findAllById(drinkIds);

        for (Drink drink : drinks){
            drink.getSizeOptions().add(sizeOption);
        }
        drinkRepository.saveAll(drinks);
        return sizeOption;
    }

    public SizeOption assignSizeToSides(Long id, List<Long> sideIds){
        if (sideIds.isEmpty()) throw new IllegalArgumentException("At least one side must be provided");
        SizeOption sizeOption = sizeOptionRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + id + " does not exist"));

        List<Side> sides = sideRepository.findAllById(sideIds);

        for (Side side : sides){
            side.getSizeOptions().add(sizeOption);
        }

        sideRepository.saveAll(sides);
        return sizeOption;
    }

    private void checkForMissingAttributes(SizeOption sizeOption){
        if (sizeOption.getLabel() == null){
            throw new InvalidSizeOptionException("Size option is missing a label");
        } else if (sizeOption.getSizeLabel() == null){
            throw new InvalidSizeOptionException("Size option is missing a size label");
        } else if (sizeOption.getExtraPrice() == null){
            throw new InvalidSizeOptionException("Size option is missing a base price");
        }
    }
}
