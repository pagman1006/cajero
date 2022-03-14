package com.banco.cajero.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.cajero.dao.model.Moneda;

public interface MonedaDao extends JpaRepository<Moneda, Long> {

}
