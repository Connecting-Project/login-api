package com.hawaiianpizza.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
public class Product {
    @Id
    private String id;
    private String category;
    private String buy;
    private String manufacturer;
    @Column(name = "productName")
    private String productName;
    private int price;
    private int quantity;
    private int sn;
}
