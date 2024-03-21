package com.app.authservice.mapper;

public interface MapToEntity <E, D> {

    E toEntity(D dto);
}
