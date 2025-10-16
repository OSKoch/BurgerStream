package com.burgerstream.backend.model.menu;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "size_options")
public class SizeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "size_label", nullable = false)
    private String sizeLabel;

    @Column(name = "extra_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal extraPrice;

    public SizeOption(){ }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public BigDecimal getExtraPrice() {
        return extraPrice;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setExtraPrice(BigDecimal extraPrice) {
        this.extraPrice = extraPrice;
    }
}