package gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import logika.Szkodliwe;
import logika.Wyjazd;

public class OpisWyjazdu {
	private Wyjazd wyjazd;
	private String opis;
	
	public OpisWyjazdu(Wyjazd wyjazd){
		this.wyjazd = wyjazd;
		this.opis = createText();
	}
	
	public String getOpis(){
		return opis;
	}
	
	private String createText(){
		String text="";
		String[] akapit = new String[8];
		
		SimpleDateFormat dateFrm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
		Date dataUsun = new Date();
		try{
			dataUsun = dateSql.parse(wyjazd.getOpisy()[5]);
		}
		catch (Exception e)
		{
			dataUsun = null;
			System.out.println(e);
		}
		akapit[0] = "Nr meldunku: "+wyjazd.getIdMeldunku()+"\t\tData: "+dateFrm.format(wyjazd.getDate())+"\t\tRodzaj: "+wyjazd.getOpisy()[0];
		akapit[1] = "\n\nAdres: \n"+wyjazd.getOpisy()[1]+", ul."+wyjazd.getOpisy()[2];
		akapit[2] = "\n\nCzasy:"
					+"\nCzas zg³oszenia: "+dateFrm.format(wyjazd.getDate())+"\tCzas usuniêcia: "+dateFrm.format(dataUsun)+"\tRó¿nica: "+wyjazd.getCzas()+" min.";
		akapit[3] = "\n\nObiekt: \n"+wyjazd.getOpisy()[3];
		akapit[4] = "\n\nRodzaj prowadzonych dzia³añ: \n"+wyjazd.getOpisy()[7];
		akapit[5] = "\nDzia³ania prowadzono z u¿yciem sprzêtu: \n"+wyjazd.getOpisy()[8];
		akapit[6] = "\nMiejsce prowadzonych dzia³añ: \n"+wyjazd.getOpisy()[6];
		akapit[7] = "\nOpis: \n"+wyjazd.getOpisy()[4];
		for(int i=0;i<akapit.length;i++){
			text = text + akapit[i];
		}
		return text;
	}
	
}
