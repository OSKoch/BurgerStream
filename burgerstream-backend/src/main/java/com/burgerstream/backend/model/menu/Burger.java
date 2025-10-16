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

    public Boolean getVegan() {
        return isVegan;
    }

    public void setVegan(Boolean vegan) {
        isVegan = vegan;
        this.setLactoseFree(true);
        if (vegan && getChicken()){
            this.setChicken(false);
        }
    }

    public Boolean getChicken() {
        return isChicken;
    }

    public void setChicken(Boolean chicken) {
        isChicken = chicken;
        if (chicken && getVegan()){
            this.setVegan(false);
        }
    }

    public Boolean getLactoseFree() {
        return isLactoseFree;
    }

    public void setLactoseFree(Boolean lactoseFree) {
        isLactoseFree = lactoseFree;
    }
}