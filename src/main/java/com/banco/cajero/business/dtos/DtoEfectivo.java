package com.banco.cajero.business.dtos;

import java.util.List;

import com.banco.cajero.dao.model.Billete;
import com.banco.cajero.dao.model.Moneda;

public class DtoEfectivo {
	
	private List<Billete> billetes;
	private List<Moneda> monedas;
	public List<Billete> getBilletes() {
		return billetes;
	}
	public void setBilletes(List<Billete> billetes) {
		this.billetes = billetes;
	}
	public List<Moneda> getMonedas() {
		return monedas;
	}
	public void setMonedas(List<Moneda> monedas) {
		this.monedas = monedas;
	}

}
