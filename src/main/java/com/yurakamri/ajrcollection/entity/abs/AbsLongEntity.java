package com.yurakamri.ajrcollection.entity.abs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class AbsLongEntity extends AbsMainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
