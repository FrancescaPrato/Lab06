package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {
	

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento> ();
		final String sql = "SELECT Umidita, Data  FROM situazione WHERE localita =? AND MONTH(data)=?";
	
		
		try {
			
			
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			// Rilevamento(String localita, Date data, int umidita)
			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			
			while (rs.next()) {

				Rilevamento r = new Rilevamento(localita, rs.getDate("Data"), rs.getInt("Umidita") );
				rilevamenti.add(r);
			}

			conn.close();
		

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return rilevamenti;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		
	double avg= 0.0;	
	
	
		if(this.getAllRilevamentiLocalitaMese(mese, localita).size()==0)
			return null;
		
		for(Rilevamento r: this.getAllRilevamentiLocalitaMese(mese, localita))
			avg+= r.getUmidita();
	
	return avg/this.getAllRilevamentiLocalitaMese(mese, localita).size();
			
	}
	
	public int getAvgRilevamentiLocalitaMeseInt(int mese, String localita) {
		
	int avg= 0;	
	
	
		if(this.getAllRilevamentiLocalitaMese(mese, localita).size()==0)
			return 0;
		
		for(Rilevamento r: this.getAllRilevamentiLocalitaMese(mese, localita))
			avg+= r.getUmidita();
	
	return avg/this.getAllRilevamentiLocalitaMese(mese, localita).size();
			
	}


	
	public List<Citta> getCitta() {
		
		List<Citta> citta = new ArrayList<Citta>();
		final String sql = "SELECT Localita  FROM situazione ";
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String s = rs.getString("Localita");
				Citta nuova = new Citta(s);
				if(!citta.contains(nuova))
						citta.add(new Citta(s));
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
}
