package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.component.MenuItemValidator;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SideService {

    private final SideRepository sideRepository;

    private final SizeOptionRepository sizeOptionRepository;

    private final MenuItemValidator validator;

    public SideService(SideRepository sideRepository, SizeOptionRepository sizeOptionRepository, MenuItemValidator validator){
        this.sideRepository = sideRepository;
        this.sizeOptionRepository = sizeOptionRepository;
        this.validator = validator;
    }

    public Side createSide(Side side){
        validator.validate(side);
        return sideRepository.save(side);
    }

    public Side getSide(Long id){
        return sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

    }

    public List<Side> getFilteredSides(Boolean shareable) {
        if (shareable != null && shareable) {
            return sideRepository.findByIsShareableTrue();

        } else {
            return sideRepository.findAll();
        }
    }

    public Side updateSide(Long id, Side newSideDetails){
        Side oldSideDetails = sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));
        validator.validate(newSideDetails);
        oldSideDetails.setName(newSideDetails.getName());
        oldSideDetails.setDescription(newSideDetails.getDescription());
        oldSideDetails.setBasePrice(newSideDetails.getBasePrice());
        oldSideDetails.setImageURL(newSideDetails.getImageURL());
        oldSideDetails.setShareable(newSideDetails.getShareable());

        return sideRepository.save(oldSideDetails);
    }

    public Map<String, Boolean> deleteSide(Long id){
        Side side = sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

        sideRepository.delete(side);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }


    // SIZE OPTIONS
    public Set<SizeOption> getSideSizes(Long id){
        Side side = sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

        return side.getSizeOptions();
    }

    public Side addSideSizeOption(Long id, Long sizeId){
        Side side = sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        side.getSizeOptions().add(size);

        return sideRepository.save(side);
    }

    public Side removeSideSizeOption(Long id, Long sizeId){
        Side side = sideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId).orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        side.getSizeOptions().remove(size);

        return sideRepository.save(side);
    }
}