package logika;

import java.sql.*;
import java.util.*;

public class Wyszukaj{
    
    private ArrayList<Object[]> tabela;
    private ResultSet rs;;
    private ResultSetMetaData rsmd;
	
	public Wyszukaj(BazaDanych bd, String query){
		try{
			Connection connect = bd.getConnect();
			if(bd.isConnected()){
				Statement stat = connect.createStatement();
            	rs = stat.executeQuery(query);
            	rsmd = rs.getMetaData();
            
            	int iloscKolumn = rsmd.getColumnCount();
            	Object kolumny[]; 
            	tabela = new ArrayList<Object[]>();
            
            	try {
            		while(rs.next()){
            			kolumny = new Object[iloscKolumn];
            			if(rs.getObject(1)!=null){
            				for(int i=0;i<iloscKolumn;i++)
    							kolumny[i] = rs.getObject(i+1);
    						tabela.add(kolumny);
    					}
    				}
            	}
            	catch (SQLException e){
    				System.out.println("Blad podczas zapytania do bazy w klasie Wyszukaj: "+e);
    			}

            	stat.close();
            	//connect.close();
			}
		}
		catch(SQLException e){
			System.out.println("B³¹d w klasie wyszukaj: [" + e.getMessage() + "]");
		}
	}

	public ArrayList<Object[]> getArrayList(){
		return tabela;
	}
}

