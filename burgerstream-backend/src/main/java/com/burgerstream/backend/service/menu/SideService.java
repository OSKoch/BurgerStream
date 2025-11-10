package com.burgerstream.backend.service.menu;

import com.burgerstream.backend.component.MenuItemHelper;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SideService {

    private final SideRepository sideRepository;

    private final SizeOptionRepository sizeOptionRepository;

    private final MenuItemHelper menuItemHelper;

    public SideService(SideRepository sideRepository,
                       SizeOptionRepository sizeOptionRepository,
                       MenuItemHelper menuItemHelper){
        this.sideRepository = sideRepository;
        this.sizeOptionRepository = sizeOptionRepository;
        this.menuItemHelper = menuItemHelper;
    }

    public Side createSide(Side side, MultipartFile image){
        menuItemHelper.validate(side);

        if (image != null && !image.isEmpty()) {
            menuItemHelper.saveImage(image);
            side.setImageUrl(image.getOriginalFilename());
        }
        return sideRepository.save(side);
    }

    public Side getSide(Long id){
        return sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

    }

    public List<Side> getFilteredSides(Boolean shareable) {
        if (shareable != null && shareable) {
            return sideRepository.findByIsShareableTrue();

        } else {
            return sideRepository.findAll();
        }
    }

    public Side updateSide(Long id, Side newSideDetails, MultipartFile image){
        menuItemHelper.validate(newSideDetails);

        Side oldSideDetails = sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

        if(image != null && !image.isEmpty()){
            menuItemHelper.replaceImage(oldSideDetails.getImageUrl(), image);
            oldSideDetails.setImageUrl(image.getOriginalFilename());
        }


        oldSideDetails.setName(newSideDetails.getName());
        oldSideDetails.setDescription(newSideDetails.getDescription());
        oldSideDetails.setBasePrice(newSideDetails.getBasePrice());
        oldSideDetails.setSizeOptions(newSideDetails.getSizeOptions());
        oldSideDetails.setIsShareable(newSideDetails.getIsShareable());

        return sideRepository.save(oldSideDetails);
    }

    public Map<String, Boolean> deleteSide(Long id){
        Side side = sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

        if(side.getImageUrl() != null && !side.getImageUrl().isEmpty()){
            menuItemHelper.deleteImage(side.getImageUrl());
        }

        sideRepository.delete(side);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
    }


    // SIZE OPTIONS
    public Set<SizeOption> getSideSizes(Long id){
        Side side = sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));

        return side.getSizeOptions();
    }

    public Side addSideSizeOption(Long id, Long sizeId){
        Side side = sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId)
                .orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        side.getSizeOptions().add(size);

        return sideRepository.save(side);
    }

    public Side removeSideSizeOption(Long id, Long sizeId){
        Side side = sideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Side with id: " + id + " does not exist"));
        SizeOption size = sizeOptionRepository.findById(sizeId)
                .orElseThrow( () -> new ResourceNotFoundException("Size with id: " + sizeId + " does not exist"));

        side.getSizeOptions().remove(size);

        return sideRepository.save(side);
    }
}