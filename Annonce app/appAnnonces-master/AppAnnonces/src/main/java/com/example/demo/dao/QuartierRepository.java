package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Quartier;

public interface QuartierRepository extends JpaRepository<Quartier, Long> {

}
