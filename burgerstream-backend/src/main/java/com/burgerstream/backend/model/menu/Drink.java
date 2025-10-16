package com.burgerstream.backend.model.menu;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drinks")
public class Drink extends MenuItem{

    @Column(name = "is_carbonated")
    private Boolean isCarbonated = false;

    @Column(name = "is_lactose_free")
    private Boolean isLactoseFree = false;

    @ManyToMany
    @JoinTable(
            name = "drink_size_options",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "size_option_id")
    )
    private Set<SizeOption> sizeOptions = new HashSet<>();

    public Drink() {}

    public Boolean getCarbonated() {
        return isCarbonated;
    }

    public void setCarbonated(Boolean carbonated) {
        isCarbonated = carbonated;
    }

    public Boolean getLactoseFree() {
        return isLactoseFree;
    }

    public void setLactoseFree(Boolean dairy) {
        isLactoseFree = dairy;
    }

    public Set<SizeOption> getSizeOptions() {
        return sizeOptions;
    }

    public void setSizeOptions(Set<SizeOption> sizeOptions) {
        this.sizeOptions = sizeOptions;
    }
}