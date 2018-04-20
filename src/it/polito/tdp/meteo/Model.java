package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int COST_PUNTEGGIO = 100000;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	MeteoDAO meteo = new MeteoDAO();
	List<SimpleCity> simpleCity= new ArrayList<SimpleCity>();
	ArrayList<SimpleCity> listaProvvisoria = new ArrayList<SimpleCity>();
	List<Citta> citta;
	ArrayList<SimpleCity> listaTecnico = new ArrayList<SimpleCity>();;
	boolean trovata;
	double punteggio= this.COST_PUNTEGGIO;
	
	public Model() {
		
	meteo = new MeteoDAO();
	
	}

	public String getUmiditaMedia(int mese) {
		
		String result="";
		citta= new ArrayList<Citta>();
		citta= meteo.getCitta();
		simpleCity.clear();
		for(Citta s : citta) {
			result+= s +" ha umidita media mensile relativa al mese selezionato di "+meteo.getAvgRilevamentiLocalitaMese(mese, s.getNome())+"\n";
			
			simpleCity.add(new SimpleCity(s.getNome(), meteo.getAvgRilevamentiLocalitaMeseInt(mese, s.getNome())));
		}

		return result;
	}

	public String trovaSequenza(int mese) {
		listaProvvisoria.clear();
		listaTecnico.clear();
		this.getUmiditaMedia(mese);
		punteggio= this.COST_PUNTEGGIO;
		this.recoursive(simpleCity, listaProvvisoria,  0);
		return this.listaTecnico.toString();
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
	
		for(int i=0 ; i<=this.NUMERO_GIORNI_TOTALI-1 ; i++) {
			
			score+= soluzioneCandidata.get(i).getCosto();
			if(i!=0)
			if(!soluzioneCandidata.get(i-1).equals(soluzioneCandidata.get(i)))
				score+= this.COST;	
			
			
			
		}
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		
		int contatore=0;
		
		//controllo che tutte le citta siano presenti almeno una volta
		if(parziale.size()==this.NUMERO_GIORNI_TOTALI)
		for(SimpleCity ct: simpleCity)
		{
			if(!parziale.contains(ct))
				return false;
		}
		
		//controllo che non ci siano citta viste per piu di sei giorni
		for(SimpleCity ct: simpleCity)
		{
			contatore=0;
			for(SimpleCity c: parziale) {
				if(c.equals(ct))
					contatore++;
			}
			
			if(contatore>this.NUMERO_GIORNI_CITTA_MAX)
				return false;
		}
		// controllo che il tecnico stia in città almeno 3 giorni
		SimpleCity citta= parziale.get(0);
		contatore=1;
		for(int i=1; i<parziale.size();i++) {
			
			if(parziale.get(i).equals(citta))
				contatore++;
					
			else {
				if(contatore<this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					return false;
				else {
					contatore=1;
					citta = parziale.get(i);
					}
				
			}
			
		}
		

		return true;
	}

	//alla prima invocazione passo un punteggio altissimo!!!!
	public void recoursive( List<SimpleCity> listaCitta, ArrayList<SimpleCity> listaCittaPerGiorno , int livello){
	
		//se la lista è piena...
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			
			if(this.punteggioSoluzione(listaCittaPerGiorno)<punteggio)
			{
		
			this.listaTecnico.clear();
			this.listaTecnico.addAll(listaCittaPerGiorno);
			punteggio=this.punteggioSoluzione(listaCittaPerGiorno);
			System.out.println("soluzione : "+listaCittaPerGiorno +"   costo: "+punteggio);
			return;
			}
		}
		
		for(SimpleCity c: listaCitta) {
			
			listaCittaPerGiorno.add(c);
			
			
			if(this.controllaParziale(listaCittaPerGiorno)) {
				this.recoursive(listaCitta, listaCittaPerGiorno,  livello+1);
			}
			
			listaCittaPerGiorno.remove(livello);
		}
	}
}
