package logika;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.toedter.calendar.JCalendar;

public class Szkodliwe{
	
	private int zmiana;
	private Date dateFrom, dateTo;
	private BazaDanych bd;
	private String host;
    private String sciezka;
    private String uzytkownik;
    private String haslo;
    private String query;
    private boolean isConnected;
    private Strazak[][] strazacy; 
	
	public Szkodliwe(){
		
		
		//na probe
		host = "127.0.0.1";
		uzytkownik = "xxxxx";
		haslo = "xxxx";
		sciezka = "xxxx";
		// end na probe
		
		
		connectDB();
		if(isConnected){
			createFiremanArray();
		}
	}
	
	public void setZmiana(int zmiana){
		this.zmiana = zmiana;
	}
	
	public void setDates(Date dateFrom, Date dateTo){
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public void setSource(String sciezka){
		this.sciezka = sciezka;
	}
	
	public void setUser(String uzytkownik){
		this.uzytkownik = uzytkownik;
	}
	
	public void setPassword(String haslo){
		this.haslo = haslo;
	}
	
	public void connectDB(){
		bd = new BazaDanych(host, sciezka, uzytkownik, haslo);
		if(bd.isConnected()) isConnected=true;
		else isConnected=false;
	}
	
	public boolean isConnected(){
		return isConnected; 
	}
	
	public Strazak[][] getFireman(){
		return strazacy;
	}		
	
	private void createFiremanArray(){
                //--------- TAJNE----------
                //zapytanie sql do bazy danych zewnêtnego systemu 
		String qr = "xxxxxx";
		Wyszukaj wyszukaj = new Wyszukaj(bd, qr);
		
		ArrayList<Object[]> tabela = wyszukaj.getArrayList();
		
		//ZMIANA jedna wielka (powodz)
		int iloscZmian = 1;
		int iloscZmiany = tabela.size() / iloscZmian +10;
	   
	    strazacy = new Strazak[3][iloscZmiany];
	    
		int i=0;
		int j1=0;
		int j2=0;
		int j3=0;
		String nazwa="";
		String id="";
		int kat=0;

		for(int k=0;k<tabela.size();k++){
			kat = Integer.parseInt(tabela.get(k)[2].toString());
			nazwa = tabela.get(k)[1].toString();
			id = tabela.get(k)[0].toString();
			
			//jedna wielka zmiana do 772 /zmiana I do 760
			//zmiana I
			if((kat>=755)&&(kat<=772)){
				strazacy[0][j1] = new Strazak();
				strazacy[0][j1].setName(nazwa);
				strazacy[0][j1].setId(id);
				j1++;
			}
			/*
			else
			//zmiana II
			if((kat>=761)&&(kat<=766)){
				strazacy[1][j2] = new Strazak();
				strazacy[1][j2].setName(nazwa);
				strazacy[1][j2].setId(id);
				j2++;
			}
			else
			//zmiana III
			if((kat>=768)&&(kat<=772)){
				strazacy[2][j3] = new Strazak();
				strazacy[2][j3].setName(nazwa);
				strazacy[2][j3].setId(id);
				j3++;
			}
			*/
			//tutaj mozna jeszcze zmniejszyc rozmiary poszczegolnych zmian na takie jaka jest faktyczna ilosc znalezionych osob
			//do zrobienia
			
		}
		
		
	}
	
	private void createQuery(){
		
		String query;
		
		//konwersja dat na format obs³ugiwany w sql
		SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String sDateFrom = "'"+dateFormat.format(dateFrom)+"'";
		String sDateTo = "'"+dateFormat.format(dateTo)+"'";

		String id;
		
		
		
		for(int i=0;i<strazacy[zmiana].length;i++){
			if(strazacy[zmiana][i]!=null){
				id = strazacy[zmiana][i].getId();
                                //--------- TAJNE----------
                                //zapytanie sql do bazy danych zewnêtnego systemu 
				query="xxxxxx";
				
				strazacy[zmiana][i].setQuery(query);
			}
		}
	}
	
	public void createResult(){
		
		createQuery();
		ArrayList<Object[]> lista; //lista wyników zapytania
		SimpleDateFormat dateFrm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
		
		//dla kazdego strazaka  
		for(int i=0;i<strazacy[zmiana].length;i++){
			if(strazacy[zmiana][i]!=null){
				//zapytaj baze o informacje o strazaku i umiesc wyniki w lista
				Wyszukaj wyszukaj = new Wyszukaj(bd, strazacy[zmiana][i].getQuery());
				lista = wyszukaj.getArrayList();
				
				//dodaj kazdemu strazakowi sluzbe nalezaca do zadanego okresu czasu
				Calendar[] daty = findAllDatesOfPeriod(); 
				for(int k=0;k<daty.length;k++){
					Sluzba sluzba = new Sluzba(k,daty[k]);
					strazacy[zmiana][i].addSluzba(k,sluzba);
				}
				
				//dla kazdego znalezionego wyjazdu
				for(int j=0;j<lista.size();j++){
					//dodaj wyjazd strazakowi jesli nie znajduje sie poza czasem trwania sluzby
					//mozliwe ze strazak pracowal wtedy na innej zmianie
					Date dataWyjazdu = new Date();
					try{
						dataWyjazdu = dateFrm.parse(lista.get(j)[1].toString());
					}
					catch (Exception e)
					{
						dataWyjazdu = null;
						System.out.println(e);
					}
					//zmienic na !pozaDataSluzby(dataWyjazdu)
					if(true){
						//dodaj wyjazd do odpowiedniej sluzby strazaka
						for(int l=0;l<strazacy[zmiana][i].getSluzby().size();l++){
							//jesli dataWyjazdu jest wieksza od datySluzby i mniejsza od datySluzby+1 dzien (86400000ms) to dodaj ja do sluzby
							Date dataSluzby = strazacy[zmiana][i].getSluzby().get(l).getDate().getTime();
							if((dataWyjazdu.getTime()>=dataSluzby.getTime())&&(dataWyjazdu.getTime()<dataSluzby.getTime()+86400000)){
								int idWyjazdu = strazacy[zmiana][i].getSluzby().size(); //dodawanie wyjazdu na ostatnia pozycje w lisie
								int idMeldunku = Integer.parseInt(lista.get(j)[5].toString());
								long czas = Long.parseLong(lista.get(j)[3].toString());
								if(czas>1440) czas = 1440; //jesli wiecej niz sluzba
								String rodzaj="";
								if(Integer.parseInt(lista.get(j)[6].toString())==1) rodzaj = "P";
								if(Integer.parseInt(lista.get(j)[6].toString())==2) rodzaj = "MZ";
								
								String[] rodzajDzialanTab = {
										"Podawanie œrodków gaœniczych w natarciu",
										"Podawanie œrodków gaœniczych w obronie",
										"Sch³adzanie obiektów urz¹dzeñ itp.",
										"Uwalnianie ludzi",
										"Uwalnianie zwierz¹t",
										"Ewakuacja ludzi",
										"Ewakuacja zwierz¹t",
										"Ewakuacja mienia",
										"Transport poszkodowanych w strefie zagro¿enia",
										"Zabezpieczenie miejsca zdarzenia",
										"Zabezpieczenie imprez masowych",
										"Rozcinanie, rozginanie konstrukcji, urz¹dzeñ, maszyn",
										"Prace rozbiórkowe konstrukcji budowlanych",
										"Podnoszenie elementów konstrukcji, maszyn, urz¹dzeñ",
										"Przemieszczanie elementów konstrukcji, urz¹dzeñ, maszyn",
										"Odgruzowywanie, odkopywanie",
										"Wykonywanie wykopów, podkopów, przebiæ",
										"Otwieranie pomieszczeñ",
										"Oddymianie, przewietrzanie",
										"Ustalanie, rozpoznawanie substancji chemicznych i innych",
										"Okreœlanie stref zagro¿enia",
										"Neutralizacja, sorpcja substancji chemicznych i innych",
										"Uszczelnianie zbiorników, cystern, ruroci¹gów",
										"Zbieranie, usuwanie, zmywanie sub. chem. i innych",
										"Ograniczanie rozlewów, wycieków",
										"Pompowanie sub. ropopochodnych, chem. i innych",
										"Wypompowanie wody i innych p³ynów z obiektów",
										"Wykonywanie pasów ochronnych, przecinek",
										"Wycinanie, usuwanie drzew i innych obiektów przyrody",
										"Przet³aczanie wody na du¿e odleg³oœci przy po¿arach",
										"Dowo¿enie, dostarczanie wody przy po¿arach",
										"Dostarczanie wody dla ludnoœci lub dla podtrzymania procesów technicznych"
								};
								String[] uzytySprzetTab = {
										"Podrêcznego sprzêtu gaœniczego",
										"Podrêcznego sprzêtu burz¹cego",
										"Pomp szlamowych",
										"Pomp typowych, po¿arniczych",
										"Pomp do innych mediów",
										"Separatorów olejowych",
										"Skimerów",
										"Zapór, tam",
										"Ubrañ gazoszczelnych",
										"Ubrañ ochronnych, chemicznych",
										"Ubrañ ochron. - ¿aroochronnych",
										"Urz¹dzeñ pomiarowych",
										"Aparatów ochr. dróg oddech.",
										"Narzêdzi hydraulicznych",
										"Narzêdzi pneumatycznych",
										"Ratowniczego ludzi",
										"Drabin przenoœnych",
										"Drabin mech. i podnoœników",
										"Mech. pi³ do ciêcia drewna",
										"Mech. pi³ do ciêcia betonu i stali",
										"Aparatów do ciêcia p³omieniem",
										"Agregatów pr¹dotwórczych",
										"Oœwietleniowego",
										"Do nurkowania",
										"Ratownictwa wysokoœciowego",
										"Zestawu opatrunkowego",
										"Przywr. dr¹¿noœæ dróg oddech.",
										"Wspomagaj¹cego masa¿ serca",
										"Do tlenoterapii 100% tlenem",
										"Worka samorozprê¿nalnego",
										"Respiratora",
										""
								};
								
								String[] miejsceDzialanTab = {
										"Wewn¹trz obiektów na poziomie piwnic",
										"Wewn¹trz obiektów na poziomie partery",
										"Wewn¹trz obiektów na poziomie piêtrach 1-3",
										"Wewn¹trz obiektów na poziomie piêtrach 4-7",
										"Wewn¹trz obiektów na poziomie piêtrach powy¿ej 7",
										"Na dachach, poddaszach",
										"Wewn¹trz szybów, kominów, wind",
										"Pod wod¹",
										"Pod ziemi¹, wew. studni, tuneli, jaskiñ",
										"W wykopach, na osuwiskach, zawa³ach",
										"Na wysokoœci",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										"",
										""
								};
								
								String miejsceDzialan=""; 
								String rodzajDzialan="";
								String uzytySprzet="";
								for(int n=0;n<32;n++){
									if(convertBinary(Integer.parseInt(lista.get(j)[11].toString()))[n]) 
										miejsceDzialan = miejsceDzialan+(n+1)+". "+miejsceDzialanTab[n]+"\n";
									if(convertBinary(Integer.parseInt(lista.get(j)[12].toString()))[n]) 
										rodzajDzialan = rodzajDzialan+(n+1)+". "+rodzajDzialanTab[n]+"\n";
									if(convertBinary(Integer.parseInt(lista.get(j)[13].toString()))[n]) 
										uzytySprzet = uzytySprzet+(n+1)+". "+uzytySprzetTab[n]+"\n";
								}
								
								String[] opisy = {
										//	.......
										rodzaj,	// 0 - rodzaj
										lista.get(j)[7].toString(),  // 1 - miejscowosc
										lista.get(j)[8].toString(),  // 2 - ulica
										lista.get(j)[9].toString(),  // 3 - obiekt
										lista.get(j)[10].toString(), // 4 - opis
										lista.get(j)[2].toString(),  // 5 - data usun
										miejsceDzialan, // 6 - miejsce dzialan
										rodzajDzialan, // 7 - rodzaj dzialan
										uzytySprzet // 8 - uzyty sprzet
										//	........
								};
								boolean[] sprzet = convertBinary(Integer.parseInt(lista.get(j)[13].toString()));
								boolean[] rodzajD = convertBinary(Integer.parseInt(lista.get(j)[12].toString()));
								boolean[] miejsce = convertBinary(Integer.parseInt(lista.get(j)[11].toString()));
								byte kat = przydzielKategorie(rodzaj, rodzajD, sprzet, miejsce);
								Wyjazd wyjazd = new Wyjazd(idWyjazdu, idMeldunku, dataWyjazdu, czas, kat, opisy);
								strazacy[zmiana][i].addWyjazd(l, wyjazd);
								if(kat==0) strazacy[zmiana][i].getSluzby().get(l).addKat1(czas);
								if(kat==1) strazacy[zmiana][i].getSluzby().get(l).addKat2(czas);
								if(kat==2) strazacy[zmiana][i].getSluzby().get(l).addKat3(czas);
								if(kat==3) strazacy[zmiana][i].getSluzby().get(l).addKat4(czas);
							}
						}
					}
					
				}
			}
		}
		printZmiana();
	}
	private byte przydzielKategorie(String rodz, boolean[] rodzaj, boolean[] sprzet, boolean[] miejsce){
		byte kat = 0;
		if(rodz.equals("P")||rodzaj[13]||rodzaj[14]||rodzaj[15]||rodzaj[16]||rodzaj[17]
		   ||miejsce[8]||miejsce[11]||sprzet[14]||sprzet[20]||sprzet[21]||sprzet[24])
			kat = 1;
		if(rodzaj[22]||rodzaj[24]||rodzaj[27]||sprzet[10]||sprzet[11])
			kat = 2;
		if(miejsce[8]||miejsce[9]||miejsce[10])
			kat = 3;
		for(int i=0;i<32;i++){
			System.out.println((i+1)+". rodzaj:         "+rodzaj[i]);
			System.out.println((i+1)+". sprzet:         "+sprzet[i]);
			System.out.println((i+1)+". miejsce:         "+miejsce[i]);
		}
		return kat;
	}
	
	private boolean[] convertBinary(int wartosc){
		boolean[] pozycje_tmp = new boolean[32];
		boolean[] pozycje = new boolean[32];
		int wart_tmp = wartosc;
		int i=0;
		while(wart_tmp>0){
			if(wart_tmp%2==1) pozycje_tmp[i] = true;
			else pozycje_tmp[i] = false;	
			wart_tmp = wart_tmp/2;
			i++;
		}
		i--;
		int k=0;
		for(int j=0;j<32;j++){
			if(i>=0) pozycje[j]=pozycje_tmp[k];
			else pozycje[j] = false;
			i--;
			k++;
		}
		return pozycje;
	}
	
	public void sumowanieCzasow(){
		//Sumowanie czasow dla poszczegolnej sluzby
		for(int i=0;i<strazacy[zmiana].length;i++){
			if(strazacy[zmiana][i]!=null){
				for(int j=0;j<strazacy[zmiana][i].getSluzby().size();j++){
					strazacy[zmiana][i].getSluzby().get(j).setKat1(0);
					strazacy[zmiana][i].getSluzby().get(j).setKat2(0);
					strazacy[zmiana][i].getSluzby().get(j).setKat3(0);
					strazacy[zmiana][i].getSluzby().get(j).setKat4(0);
					for(int k=0;k<strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().size();k++){
						byte kat = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getKategoria();
						long czas = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
						if(kat==0)
							strazacy[zmiana][i].getSluzby().get(j).addKat1(czas);
						if(kat==1)
							strazacy[zmiana][i].getSluzby().get(j).addKat2(czas);
						if(kat==2)
							strazacy[zmiana][i].getSluzby().get(j).addKat3(czas);
						if(kat==3)
							strazacy[zmiana][i].getSluzby().get(j).addKat4(czas);
					}
				}
			}
		}
		
		//Sumowanie czasow dla tych samych kategorii
		for(int i=0;i<strazacy[zmiana].length;i++){
			if(strazacy[zmiana][i]!=null){
				long kat1=0, kat2=0, kat3=0, kat4=0;
				for(int j=0;j<strazacy[zmiana][i].getSluzby().size();j++){
					for(int k=0;k<strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().size();k++){
						byte kat = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getKategoria();
						if(kat==0)
							kat1 += strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
						if(kat==1)
							kat2 += strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
						if(kat==2)
							kat3 += strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
						if(kat==3)
							kat4 += strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
					}
				}
				strazacy[zmiana][i].setKat1(kat1);
				strazacy[zmiana][i].setKat2(kat2);
				strazacy[zmiana][i].setKat3(kat3);
				strazacy[zmiana][i].setKat4(kat4);
			}
		}
	}

	
	public Calendar[] findAllDatesOfPeriod(){
		int [] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    	// dla roku przestepnego
    	if (((dateFrom.getYear() % 4 == 0) && (dateFrom.getYear() % 100 != 0)) || (dateFrom.getYear() % 400 == 0)) daysInMonths[1] = 29;
    	
    	//01-01-2000 - I zmiana
    	Calendar pierwszyDzien = Calendar.getInstance();
    	pierwszyDzien.set(2000, 0, 1, 8, 0, 0);//1 stycznia 2000r. 08:00:00 - pracowala zmiana I
    	
    	Calendar poczatekPrzedzialu = Calendar.getInstance();
    	poczatekPrzedzialu.setTime(dateFrom);
    	
    	byte zmiana_tmp=1;
    	long roznica = Math.round(dateFrom.getTime()/86400000) - Math.round(pierwszyDzien.getTimeInMillis()/86400000);
    	
    	//zmiana I else II else III
    	if (roznica %3 == 0){
    		zmiana_tmp=1;
    	}
    	else
    	if ((roznica+2) %3 == 0){
    		zmiana_tmp=2;
    	}
    	else
    	if ((roznica+1) %3 == 0){
    		zmiana_tmp=3;
    	}
    	
    	if(zmiana==0){
    		//tutaj pozmieniane
    		if(zmiana_tmp==2) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 0);
    		else
    		if(zmiana_tmp==3) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 0);;
    	}
    	else
    	if(zmiana==1){
    		if(zmiana_tmp==1) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 1);
    		else
    		if(zmiana_tmp==3) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 2);
    	}
    	else
    	if(zmiana==2){
    		if(zmiana_tmp==1) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 2);
    		else
    		if(zmiana_tmp==2) poczatekPrzedzialu.add(Calendar.DAY_OF_MONTH, 1);
    	}
			
    	//znajdywanie wszystkich dat w przedziale	
    		
    	Calendar iData = Calendar.getInstance();
    	//liczenie dat
    	iData.setTime(poczatekPrzedzialu.getTime());
		int iloscDat = 0;
		while(iData.getTimeInMillis()<=dateTo.getTime()){ 
			iloscDat++;
			iData.add(Calendar.DAY_OF_MONTH, 1); //dodaj trzy dni do daty	(zmienione na 1)	
		}
		
		Calendar jData = Calendar.getInstance();
		Calendar[] daty = new Calendar[iloscDat];
    	
		//zapisywanie dat
		iData.setTime(poczatekPrzedzialu.getTime());
		for(int i=0;i<iloscDat;i++){
			daty[i] = Calendar.getInstance();
			daty[i].setTime(iData.getTime());;
			iData.add(Calendar.DAY_OF_MONTH, 1); //dodaj trzy dni do daty (zmienione na 1)
		}
		
    	return daty;
	}
	
	private boolean pozaDataSluzby(Date data){
		boolean poza = false;
		Calendar[] iData = findAllDatesOfPeriod();
	
		for(int n=0;n<iData.length;n++){
			long dataZna = data.getTime();
			long iDat = iData[n].getTimeInMillis();
			poza = false;
		
			if(Math.round(dataZna/86400000)==Math.round(iDat/86400000)){
				break;
			}
			else
				if(Math.round(dataZna/86400000)==Math.round(iDat/86400000)+1){
					if(dataZna>iDat+86400000){
						poza = true;
						break;
					}
					else
						break;
				}
				else
					if(Math.round(dataZna/86400000)==Math.round(iDat/86400000)-1){
						poza = true;
						break;
					}
		}
		return poza;
	}

	
	private void printZmiana(){
		SimpleDateFormat  dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat  dateFormatMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(int i=0;i<strazacy[zmiana].length;i++){
			if(strazacy[zmiana][i]!=null){
				System.out.println(i+1+". "+strazacy[zmiana][i].getName());
				//System.out.println(strazacy[zmiana][i].getQuery());
				for(int j=0;j<strazacy[zmiana][i].getSluzby().size();j++){
					Date d = strazacy[zmiana][i].getSluzby().get(j).getDate().getTime();
					String sd = dateFormat.format(d);
					System.out.println("\t"+(j+1)+". Sluzba: "+sd);
					for(int k=0;k<strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().size();k++){
						Date da = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getDate();
						String sda = dateFormatMin.format(da);
						String opis = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getOpisy()[3];
						long czas = strazacy[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getCzas();
						System.out.println("\t\t"+(k+1)+". Wyjazd: "+sda+", Czas: "+czas+",  Obiekt: "+opis);
					}
				}
				System.out.println("_________________________________________________________________________\n");
			}
		}
	}
}