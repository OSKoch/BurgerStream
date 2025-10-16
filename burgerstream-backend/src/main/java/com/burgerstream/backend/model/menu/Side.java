package com.burgerstream.backend.model.menu;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sides")
public class Side extends MenuItem{

    @Column(name = "is_shareable")
    private Boolean isShareable = false;

    public Side() {}

    @ManyToMany
    @JoinTable(
            name = "side_size_options",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "size_option_id")
    )
    private Set<SizeOption> sizeOptions = new HashSet<>();

    public Boolean getShareable() {
        return isShareable;
    }

    public void setShareable(Boolean shareable) {
        isShareable = shareable;
    }

    public Set<SizeOption> getSizeOptions() {
        return sizeOptions;
    }

    public void setSizeOptions(Set<SizeOption> sizeOptions) {
        this.sizeOptions = sizeOptions;
    }
}