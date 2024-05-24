package com.yurakamri.ajrcollection.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
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
/*Exeption ga tushgani uchun qo`yildi bu annotatsiya*/
public class Brand extends AbsLongEntity {

    @Column(nullable = false, unique = true)
    String name;

    boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    Attachment logo;

    @OneToMany(mappedBy = "brand")
    List<Product> products;

    public Brand(String name, boolean active) {
        this.name = name;
        this.active = active;
    }
}
