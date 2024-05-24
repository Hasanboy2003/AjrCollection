package com.yurakamri.ajrcollection.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserSearch extends AbsUUIDEntity {

    @Column(nullable = false, updatable = false)
    private String searchText;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
