package com.banco.cajero.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.cajero.dao.model.Billete;

public interface BilleteDao extends JpaRepository<Billete, Long> {

}
