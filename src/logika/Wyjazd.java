package logika;

import java.util.Date;

public class Wyjazd {
	private int id;
	private int idMeldunku;
	private Date data;
	private long czas;
	private byte kategoria;
	private String[] opisy;
	
	public Wyjazd(int id, int idMeldunku, Date data, long czas, byte kat, String[] opisy){
		setId(id);
		setIdMeldunku(idMeldunku);
		setData(data);
		setCzas(czas);
		setKategoria(kat);
		setOpisy(opisy);
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setIdMeldunku(int id){
		this.idMeldunku = id;
	}
	
	
	public void setData(Date data){
		this.data = data;
	}
	
	public void setCzas(long czas){
		this.czas = czas;
	}
	
	public void setKategoria(byte kat){
		this.kategoria = kat;
	}
	
	public void setOpisy(String[] opisy){
		this.opisy = opisy;
	}
	
	public int getId(){
		return id;
	}
	
	public int getIdMeldunku(){
		return idMeldunku;
	}
	
	public Date getDate(){
		return data;
	}
	
	public long getCzas(){
		return czas;
	}
	
	public byte getKategoria(){
		return kategoria;
	}
	
	public String[] getOpisy(){
		return opisy;
	}
	
}
