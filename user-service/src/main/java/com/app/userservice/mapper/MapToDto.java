package com.app.userservice.mapper;

public interface MapToDto<E, D> {

    D toDto(E entity);
}
