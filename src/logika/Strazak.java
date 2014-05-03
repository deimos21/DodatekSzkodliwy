package logika;

import java.util.*;

public class Strazak {
	//protected na private
	
	private String id;
	private String name;
	private ArrayList<Sluzba> sluzby;
	private long kat1, kat2, kat3, kat4;
	private float wartosc; //wartosc w zl. w przyszlosci
	
	private String query; //zapytanie do serwera bazy danych dotyczace wszystkich informacji o danym strazaku
	
	public Strazak(){
		sluzby = new ArrayList<Sluzba>();
	}
	
	public void setQuery(String query){
		this.query = query;
	}
	
	public String getQuery(){
		return query;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
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
	
	public void addSluzba(int indexSluzby, Sluzba sluzba){
		this.sluzby.add(indexSluzby, sluzba);
	}
	
	public void setSluzba(int idSluzby, Sluzba sluzba){
		sluzby.get(idSluzby).setData(sluzba.getDate());
		sluzby.get(idSluzby).setKat1(sluzba.getKat1());
		sluzby.get(idSluzby).setKat2(sluzba.getKat2());
		sluzby.get(idSluzby).setKat3(sluzba.getKat3());
		sluzby.get(idSluzby).setKat4(sluzba.getKat4());
	}
	
	public void addWyjazd(int idSluzby, Wyjazd wyjazd){
		sluzby.get(idSluzby).addWyjazd(wyjazd);
	}
	
	public void setWyjazd(int idSluzby, int idWyjazdu, Wyjazd wyjazd){
		sluzby.get(idSluzby).setWyjazd(idWyjazdu, wyjazd);
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<Sluzba> getSluzby(){
		return sluzby;
	}
	
	public void setWartosc(float wartosc){
		this.wartosc = wartosc;
	}
	
	public float getWartosc(){
		return wartosc;
	}
	
	//dorobic metody
}
