package com.app.taskservice.mapper;

public interface MapToEntity <E, D> {

    E toEntity(D dto);
}
