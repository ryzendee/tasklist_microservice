package com.app.authservice.mapper;

public interface MapToDto <E, D> {

    D toDto(E entity);
}
