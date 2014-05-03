package logika;

import java.sql.*;

public class BazaDanych {   
    private String host;
    private String sciezka;
    private String uzytkownik;
    private String haslo;
    private Connection connect;
    private Statement stm;
    private ResultSet rs;
    private boolean connection;

    public BazaDanych(String host, String sciezka, String uzytkownik, String haslo) {
        this.host = host;
        this.sciezka = sciezka;
        this.uzytkownik = uzytkownik;
        this.haslo = haslo;
        try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
        }
        catch(java.lang.ClassNotFoundException e) {
                System.out.println("Nie mog� odnale�� klasy [" + e.getMessage() + "]");
                return;
        }       
        try {
            this.connect = DriverManager.getConnection("jdbc:firebirdsql:"+ this.host + ":"+ this.sciezka, this.uzytkownik, this.haslo);
            this.stm = this.connect.createStatement();
            this.rs = null;
            connection = true;
        } catch (Exception e) {
            System.out.println("Problem podczas �aczenia z baz� danych [" + e.getMessage() + "]");
            connection = false;
        }
    } 
    public boolean isConnected(){
    	return connection;
    }
    public Connection getConnect(){
    	return connect;
    }
    public boolean zamknijBaze() {
        try {
            this.connect.close();
            return true;
        } catch(Exception e) {
            System.out.println("Problem z zamkni�ciem po��czenia bazy danych [" + e.getMessage() + "]");
        }
        return false;
    }
}