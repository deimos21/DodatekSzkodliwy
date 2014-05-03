package logika;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Eksportuj {

	ArrayList <String[]> tabela;
	File plik;
	
	public Eksportuj(ArrayList <String[]> tabela, File plik){
		this.tabela = tabela;
		this.plik = plik;
	}
	
	public void writeToFile() throws IOException{
		FileWriter fstream = new FileWriter(plik);
		BufferedWriter out = new BufferedWriter(fstream);
		
		for(int i=0;i<tabela.size();i++){
			for(int j=0;j<tabela.get(i).length;j++){
				out.write(tabela.get(i)[j]+";");
			}
			out.write("\n");
		}
		
		out.close();
	}
}