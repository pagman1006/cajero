package com.banco.cajero.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banco.cajero.business.dtos.DtoEfectivo;
import com.banco.cajero.business.impl.CajeroServiceImpl;

@RestController
public class CajeroController {
	
	@Autowired
	private CajeroServiceImpl cajeroService;
	
	@RequestMapping(value = "/cuenta/saldo", method = RequestMethod.GET)
	public Double consultaSaldo() {
		Double efectivo = cajeroService.consultarDisponible();
		
		return efectivo;
	}

	
	@RequestMapping(value = "/cuenta/retiro/{retiro}", method = RequestMethod.GET)
	public DtoEfectivo retirarEfectivo(@PathVariable(name = "retiro") String retiro) {
		DtoEfectivo efectivo = new DtoEfectivo();
		if (validarCantidadRetiro(retiro)) {
			efectivo = cajeroService.retirarEfectivo(Double.valueOf(retiro) / 100);
			cajeroService.consultarEfectivo();
		}
		
		return efectivo;
	}
	
	@RequestMapping(value = "/cuenta/saldo/efectivo", method = RequestMethod.GET)
	public DtoEfectivo consultarEfectivo() {
		return cajeroService.consultarEfectivo();
	}
	
	private boolean validarCantidadRetiro(String retiro) {
		if (retiro != null) {
			String regex = "[0-9]+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(retiro);
			return m.matches();
		}
		
		return false;
	}
}
