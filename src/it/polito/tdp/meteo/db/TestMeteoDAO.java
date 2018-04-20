package it.polito.tdp.meteo.db;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class TestMeteoDAO {

	public static void main(String[] args) {

		MeteoDAO dao = new MeteoDAO();

		List<Rilevamento> list = dao.getAllRilevamenti();

		// STAMPA: localita, giorno, mese, anno, umidita (%)
		for (Rilevamento r : list) {
			System.out.format("%-10s %2td/%2$2tm/%2$4tY %3d%%\n", r.getLocalita(), r.getData(), r.getUmidita());
		}
		
		
		String result="";
		List<Citta>citta= new ArrayList<Citta>();
		citta= dao.getCitta();
		for(Citta s : citta) {
			result+= s +" ha umidita media mensile relativa al mese selezionato di "+dao.getAvgRilevamentiLocalitaMese(12, s.getNome())+"\n";
			
		}
		System.out.println(result);
		System.out.println(dao.getAllRilevamentiLocalitaMese(12, "Genova"));
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(1, "Genova"));
//		
//		System.out.println(dao.getAllRilevamentiLocalitaMese(5, "Milano"));
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(5, "Milano"));
//		
//		System.out.println(dao.getAllRilevamentiLocalitaMese(5, "Torino"));
//		System.out.println(dao.getAvgRilevamentiLocalitaMese(5, "Torino"));
		
	}

}
