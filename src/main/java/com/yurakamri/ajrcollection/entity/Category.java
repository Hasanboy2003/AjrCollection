package com.yurakamri.ajrcollection.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// Exeption ga tushgani uchun qo`yildi bu annotatsiya
public class Category extends AbsUUIDEntity {

    @Column(nullable = false, unique = true)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    Category parentCategory;

    @OneToMany(mappedBy = "category")
    List<Product> products;

    public Category(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
}
