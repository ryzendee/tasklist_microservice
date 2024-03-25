package com.app.authservice.mapper;

public interface MapToTarget <O, T> {

    T map(O currentObject);
}
