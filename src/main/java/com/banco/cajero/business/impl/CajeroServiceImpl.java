package com.banco.cajero.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.cajero.business.CajeroService;
import com.banco.cajero.business.dtos.DtoEfectivo;
import com.banco.cajero.dao.BilleteDao;
import com.banco.cajero.dao.MonedaDao;
import com.banco.cajero.dao.model.Billete;
import com.banco.cajero.dao.model.Moneda;

@Service("cajeroService")
public class CajeroServiceImpl implements CajeroService {

	@Autowired
	private MonedaDao monedaDao;

	@Autowired
	private BilleteDao billeteDao;

	private static final String B_1000 = "B_1000";
	private static final String B_500 = "B_500";
	private static final String B_200 = "B_200";
	private static final String B_100 = "B_100";
	private static final String B_50 = "B_50";
	private static final String B_20 = "B_20";

	private static final String M_10 = "M_10";
	private static final String M_5 = "M_5";
	private static final String M_2 = "M_2";
	private static final String M_1 = "M_1";
	private static final String M_05 = "M_05";

	@Override
	public Double consultarDisponible() {
		List<Moneda> monedas = monedaDao.findAll();
		List<Billete> billetes = billeteDao.findAll();

		Double disponible = 0.0;

		if (disponibleBilletes(billetes) != null) {
			disponible = disponibleBilletes(billetes);
		}

		if (disponibleMonedas(monedas) != null) {
			disponible = disponible != null ? disponible + disponibleMonedas(monedas) : disponibleMonedas(monedas);
		}

		return disponible;
	}

	@Override
	public DtoEfectivo retirarEfectivo(Double cantidad) {
		if (cantidad > consultarDisponible()) {
			return null;
		}

		DtoEfectivo efectivo = new DtoEfectivo();
		List<Billete> bRetiroList = new ArrayList<>();
		List<Moneda> mRetiroList = new ArrayList<>();

		List<Moneda> monedas = monedaDao.findAll();
		List<Billete> billetes = billeteDao.findAll();

		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_1000, 1000);
		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_500, 500);
		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_200, 200);
		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_100, 100);
		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_50, 50);
		cantidad = retiroBilletes(billetes, cantidad, efectivo, bRetiroList, B_20, 20);

		cantidad = retiroMonedas(monedas, cantidad, efectivo, mRetiroList, M_10, 10);
		cantidad = retiroMonedas(monedas, cantidad, efectivo, mRetiroList, M_5, 5);
		cantidad = retiroMonedas(monedas, cantidad, efectivo, mRetiroList, M_2, 2);
		cantidad = retiroMonedas(monedas, cantidad, efectivo, mRetiroList, M_1, 1);
		cantidad = retiroMonedas(monedas, cantidad, efectivo, mRetiroList, M_05, 0.5);

		return efectivo;
	}

	private Double retiroBilletes(List<Billete> billetes, Double cantidad, DtoEfectivo efectivo,
			List<Billete> bRetiroList, String tipoBillete, int valor) {

		if (cantidad >= Double.valueOf(valor)) {
			Billete bRetiro = new Billete();
			Double redFloor = Math.floor(cantidad / valor);
			Billete nuevoBillete = billetes.stream()
					.filter(billete -> billete.getTipoBillete().equalsIgnoreCase(tipoBillete)).toList().get(0);

			if (redFloor <= nuevoBillete.getCantidad()) {
				nuevoBillete.setCantidad(nuevoBillete.getCantidad() - redFloor.intValue());
				cantidad = cantidad - (redFloor * valor);
				bRetiro.setTipoBillete(tipoBillete);
				bRetiro.setCantidad(redFloor.intValue());

			} else {
				cantidad = cantidad - (nuevoBillete.getCantidad() * valor);
				bRetiro.setTipoBillete(tipoBillete);
				bRetiro.setCantidad(nuevoBillete.getCantidad());
				nuevoBillete.setCantidad(0);
			}

			billeteDao.save(nuevoBillete);
			bRetiroList.add(bRetiro);

		}
		efectivo.setBilletes(bRetiroList);

		return cantidad;
	}

	private Double retiroMonedas(List<Moneda> monedas, Double cantidad, DtoEfectivo efectivo, List<Moneda> mRetiroList,
			String tipoMoneda, double valor) {

		if (cantidad >= Double.valueOf(valor)) {
			Moneda mRetiro = new Moneda();
			Double redFloor = Math.floor(cantidad / valor);
			Moneda nuevaMoneda = monedas.stream().filter(moneda -> moneda.getTipoMoneda().equalsIgnoreCase(tipoMoneda))
					.toList().get(0);

			if (redFloor <= nuevaMoneda.getCantidad()) {
				nuevaMoneda.setCantidad(nuevaMoneda.getCantidad() - redFloor.intValue());
				cantidad = cantidad - (redFloor * valor);
				mRetiro.setTipoMoneda(tipoMoneda);
				mRetiro.setCantidad(redFloor.intValue());

			} else {
				cantidad = cantidad - (nuevaMoneda.getCantidad() * valor);
				mRetiro.setTipoMoneda(tipoMoneda);
				mRetiro.setCantidad(nuevaMoneda.getCantidad());
				nuevaMoneda.setCantidad(0);
			}

			monedaDao.save(nuevaMoneda);
			mRetiroList.add(mRetiro);

		}
		efectivo.setMonedas(mRetiroList);

		return cantidad;
	}

	private Double disponibleBilletes(List<Billete> billetes) {
		Double disponible = 0.0;
		for (Billete billete : billetes) {
			switch (billete.getTipoBillete()) {
			case B_1000:
				disponible += Double.valueOf(1000) * billete.getCantidad();
				break;
			case B_500:
				disponible += Double.valueOf(500) * billete.getCantidad();
				break;
			case B_200:
				disponible += Double.valueOf(200) * billete.getCantidad();
				break;
			case B_100:
				disponible += Double.valueOf(100) * billete.getCantidad();
				break;
			case B_50:
				disponible += Double.valueOf(50) * billete.getCantidad();
				break;
			case B_20:
				disponible += Double.valueOf(20) * billete.getCantidad();
				break;

			default:
				break;
			}
		}
		return disponible;
	}

	private Double disponibleMonedas(List<Moneda> monedas) {
		Double disponible = 0.0;
		for (Moneda moneda : monedas) {
			switch (moneda.getTipoMoneda()) {
			case M_10:
				disponible += Double.valueOf(10) * moneda.getCantidad();
				break;
			case M_5:
				disponible += Double.valueOf(5) * moneda.getCantidad();
				break;
			case M_2:
				disponible += Double.valueOf(2) * moneda.getCantidad();
				break;
			case M_1:
				disponible += Double.valueOf(1) * moneda.getCantidad();
				break;
			case M_05:
				disponible += Double.valueOf(0.5) * moneda.getCantidad();
				break;

			default:
				break;
			}
		}
		return disponible;
	}

	@Override
	public DtoEfectivo consultarEfectivo() {
		List<Moneda> monedas = monedaDao.findAll();
		List<Billete> billetes = billeteDao.findAll();

		DtoEfectivo efectivo = new DtoEfectivo();
		efectivo.setBilletes(billetes);
		efectivo.setMonedas(monedas);
		return efectivo;
	}

}
