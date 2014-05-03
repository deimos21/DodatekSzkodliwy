package gui;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logika.Szkodliwe;
import logika.Wyjazd;

public class TabelaWyjazdy extends JTable {
	private Szkodliwe szkodliwe;
	private int nrSluzby;
	private int zmiana;
	
	public TabelaWyjazdy(Szkodliwe szkodliwe, int nrSluzby, int zmiana){
		this.szkodliwe = szkodliwe;
		this.nrSluzby = nrSluzby;
		this.zmiana = zmiana;
		
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(createDataObject(), createColumnNames());
		this.setModel(dm);
		
	}
	
	private String[] createColumnNames(){
		String[] columnNames = new String[6];
		columnNames[0] = "Lp.";
		columnNames[1] = "Nr meldunku";
		columnNames[2] = "Data";
		columnNames[3] = "Rodzaj";
		columnNames[4] = "Obiekt";
		columnNames[5] = "Kat.";
		
		return columnNames;
	}
	
	private Object[][] createDataObject(){
		ArrayList<Wyjazd> wyjazdy = new ArrayList<Wyjazd>();
		for(int i=0;i<szkodliwe.getFireman()[zmiana].length;i++){
			if(szkodliwe.getFireman()[zmiana][i]!=null){
			//szukamy wyjazdu wsrod wszystkich strazakow danej zmiany o sluzbie=nrSluzby
			//wsrod jego wszystkich wyjazdow, ktory juz jest w spisie wyjazdy
			//jesli jest to juz Jest = true, jesli nie to dodajemy go do lisy
			
			for(int k=0;k<szkodliwe.getFireman()[zmiana][i].getSluzby().get(nrSluzby).getWyjazdy().size();k++){
				boolean juzJest = false;
				for(int j=0;j<wyjazdy.size();j++){
					if(wyjazdy.get(j).getIdMeldunku()
							==szkodliwe.getFireman()[zmiana][i].getSluzby().get(nrSluzby).getWyjazdy().get(k).getIdMeldunku()){
						juzJest = true;
						break ;
					}
				}
				if(!juzJest){
					wyjazdy.add(szkodliwe.getFireman()[zmiana][i].getSluzby().get(nrSluzby).getWyjazdy().get(k));
				}
			}
		}
		}
		Object[][] data = new Object[wyjazdy.size()][6];
		for(int i=0;i<wyjazdy.size();i++){
			data[i][0] = Integer.toString(i+1);
			data[i][1] = Integer.toString(wyjazdy.get(i).getIdMeldunku());
			SimpleDateFormat dateFrm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			data[i][2] = dateFrm.format(wyjazdy.get(i).getDate());
			data[i][3] = wyjazdy.get(i).getOpisy()[0];
			data[i][4] = wyjazdy.get(i).getOpisy()[3];
			data[i][5] = wyjazdy.get(i).getKategoria()+1;
		}	
		return data;		
	}
}
