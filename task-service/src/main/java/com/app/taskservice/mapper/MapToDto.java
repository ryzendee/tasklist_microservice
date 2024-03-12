package com.app.taskservice.mapper;

public interface MapToDto<E, D> {

    D toDto(E entity);
}
