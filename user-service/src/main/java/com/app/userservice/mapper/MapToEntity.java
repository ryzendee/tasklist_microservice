package com.app.userservice.mapper;

public interface MapToEntity <E, D> {

    E toEntity(D dto);
}
