package com.banco.cajero.business;

import com.banco.cajero.business.dtos.DtoEfectivo;

public interface CajeroService {
	
	public Double consultarDisponible();
	public DtoEfectivo retirarEfectivo(Double cantidad);
	public DtoEfectivo consultarEfectivo();

}
