package org.ismail.gestiondesproduits.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import javax.naming.Name;

@Entity
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private double unitPrice;
    private String description;
    private Integer quantity;
    
    
    public Produit() {}

    public Produit(Long id, String name, double unitPrice, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
