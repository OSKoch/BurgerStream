package com.burgerstream.backend.model.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "burgers")
public class Burger extends MenuItem{

    @Column(name = "is_vegan")
    private Boolean isVegan = false;

    @Column(name = "is_chicken")
    private Boolean isChicken = false;

    @Column(name = "is_lactose_free")
    private Boolean isLactoseFree = false;

    public Boolean getIsVegan() {
        return isVegan;
    }

    public void setIsVegan(Boolean vegan) {
        isVegan = vegan;
        this.setIsLactoseFree(true);
        if (vegan && getIsChicken()){
            this.setIsChicken(false);
        }
    }

    public Boolean getIsChicken() {
        return isChicken;
    }

    public void setIsChicken(Boolean chicken) {
        isChicken = chicken;
        if (chicken && getIsVegan()){
            this.setIsVegan(false);
        }
    }

    public Boolean getIsLactoseFree() {
        return isLactoseFree;
    }

    public void setIsLactoseFree(Boolean lactoseFree) {
        isLactoseFree = lactoseFree;
    }
}