package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity // comment
public class Advertising extends AbsUUIDEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Attachment attachment;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    String destination;

    //yangi field qo`shdik. Userga faqat aktiv reklamalar ko`rinadigan qilish uchun
    boolean active;


    public Advertising(String title, String description, String destination, boolean active) {
        this.title = title;
        this.description = description;
        this.destination = destination;
        this.active = active;
    }
}
