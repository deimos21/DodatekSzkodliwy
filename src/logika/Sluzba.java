package logika;

import java.util.ArrayList;
import java.util.Calendar;

public class Sluzba {
	private int id;
	private Calendar data;
	private long kat1;
	private long kat2;
	private long kat3;
	private long kat4;
	private ArrayList<Wyjazd> wyjazdy;
	
	public Sluzba(int id, Calendar data){
		setId(id);
		setData(data);
		setKat1(0);
		setKat2(0);
		setKat3(0);
		setKat4(0);
		wyjazdy = new ArrayList<Wyjazd>();
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setData(Calendar data){
		this.data = data;
	}
	
	public void setKat1(long kat1){
		this.kat1 = kat1;
	}
	
	public void setKat2(long kat2){
		this.kat2 = kat2;
	}
	
	public void setKat3(long kat3){
		this.kat3 = kat3;
	}
	
	public void setKat4(long kat4){
		this.kat4 = kat4;
	}
	
	public void addKat1(long kat1){
		this.kat1 += kat1;
	}
	
	public void addKat2(long kat2){
		this.kat2 += kat2;
	}
	
	public void addKat3(long kat3){
		this.kat3 += kat3;
	}
	
	public void addKat4(long kat4){
		this.kat4 += kat4;
	}
	
	public void addWyjazd(Wyjazd wyjazd){
		wyjazdy.add(wyjazd);
	}
	
	public void setWyjazd(int indexWyjazdu, Wyjazd wyjazd){
		wyjazdy.get(indexWyjazdu).setCzas(wyjazd.getCzas());
		wyjazdy.get(indexWyjazdu).setData(wyjazd.getDate());
		wyjazdy.get(indexWyjazdu).setKategoria(wyjazd.getKategoria());
		wyjazdy.get(indexWyjazdu).setOpisy(wyjazd.getOpisy());
	}
	
	public int getId(){
		return id;
	}
	
	public Calendar getDate(){
		return data;
	}
	
	public long getKat1(){
		return kat1;
	}
	
	public long getKat2(){
		return kat2;
	}
	
	public long getKat3(){
		return kat3;
	}
	
	public long getKat4(){
		return kat4;
	}
	
	
	public ArrayList<Wyjazd> getWyjazdy(){
		return wyjazdy;
	}
}
