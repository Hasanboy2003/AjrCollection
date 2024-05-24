package com.yurakamri.ajrcollection.mapper.base;

/**
 * @param <E>  - ENTITY
 * @param <CD> - CREATE DTO
 * @param <UD> - UPDATE DTO
 */
public interface BaseMapper<E, UD, CD> {

    E fromCreateDto(CD createDto);

    void fromUpdateDto(UD updateDto, E target);
}
