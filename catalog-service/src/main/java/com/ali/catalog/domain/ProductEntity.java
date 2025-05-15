package com.ali.catalog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Product code is required") private String code;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Product name is required") private String name;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    @NotEmpty(message = "Product price is required") @DecimalMin("0.1") private BigDecimal price;

    public ProductEntity() {}

    public ProductEntity(Long id, String code, String name, String description, String imageUrl, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty(message = "Product code is required") String getCode() {
        return code;
    }

    public void setCode(@NotEmpty(message = "Product code is required") String code) {
        this.code = code;
    }

    public @NotEmpty(message = "Product name is required") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Product name is required") String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotEmpty(message = "Product price is required") @DecimalMin("0.1") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotEmpty(message = "Product price is required") @DecimalMin("0.1") BigDecimal price) {
        this.price = price;
    }
}
