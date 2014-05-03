package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import logika.Szkodliwe;
import logika.Wyjazd;

public class OknoEdytuj extends JDialog implements ActionListener{
	private OknoGlowne og;
	private Szkodliwe szkodliwe;
	private int nrSluzby;
	private int zmiana;
	private byte kat;
	private ArrayList<Wyjazd> wyjazdy;
	private ArrayList<Wyjazd> wyjazdyAll;
	
	private JList dataChooser;
	private JRadioButton kat1Button;
	private JRadioButton kat2Button;
	private JRadioButton kat3Button;
	private JRadioButton kat4Button;
	private JButton zachowajZmianyButton;
	private JButton anulujButton;
	private JButton zmienKat;
	private JButton pomoc;
	private JTable tabela;
	private JTextArea opis;
	private JScrollPane scrollPane;
	private JTextField czasT;
	
	
	public OknoEdytuj(OknoGlowne og, Szkodliwe szkodliwe, int zmiana){
		this.og = og;
		this.setTitle("Edytuj wyjazdy");
		this.szkodliwe = szkodliwe;
		this.zmiana = zmiana;
		createWyjazdy();
		createAndShowDialog();
	}
	
	
	//metoda tworzaca panel listboxa wyboru daty
	private JPanel createDataChooser(){
		JPanel panelDaty = new JPanel();
		panelDaty.setBorder(BorderFactory.createTitledBorder("Daty:"));
		
		SimpleDateFormat  dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar[] daty = szkodliwe.findAllDatesOfPeriod();
    	int iloscDat = daty.length;
		String[] sDaty = new String[iloscDat];
		for(int i=0;i<iloscDat;i++){
			sDaty[i] = dateFormat.format(daty[i].getTime());
		}
		
		dataChooser = new JList(sDaty);
		dataChooser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataChooser.setLayoutOrientation(JList.VERTICAL);
		dataChooser.setSelectedIndex(0);
		dataChooser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                   nrSluzby = dataChooser.getSelectedIndex();
                   createWyjazdy();
                   updateTabelaWyjazdy();
                   if(wyjazdy==null) zmienKat.setEnabled(false);
                   if(wyjazdy!=null) tabela.changeSelection(0, 1, false, false);
            }
        });


		JScrollPane listScroller = new JScrollPane(dataChooser);
		listScroller.setPreferredSize(new Dimension(90,185));
		listScroller.setMaximumSize(new Dimension(90,185));
		
		nrSluzby = 0;
		
		panelDaty.add(listScroller);
		panelDaty.setPreferredSize(new Dimension(120,220));
		panelDaty.setMaximumSize(new Dimension(120,220));
		panelDaty.setMinimumSize(new Dimension(120,220));
		
		return panelDaty;
	}
	//metoda tworzaca panel tabeli wyjazdy
	private JPanel createTabelaWyjazdow(){
		JPanel panelTabeli = new JPanel();
		panelTabeli.setLayout(new BoxLayout(panelTabeli,BoxLayout.X_AXIS));
		
		tabela = new JTable();
		tabela.getSelectionModel().addListSelectionListener(new RowListener());
		
		scrollPane = new JScrollPane(tabela);
	
		panelTabeli.setBorder(BorderFactory.createTitledBorder("Wyjazdy:"));
		
		panelTabeli.add(scrollPane);
		
		
		return panelTabeli;
	}
	
	//metoda tworzaca panel wyboru kategorii
	private JPanel createPanelKat(){
		JPanel panelKatPrzy = new JPanel();
		panelKatPrzy.setLayout(new BoxLayout(panelKatPrzy,BoxLayout.X_AXIS));
		panelKatPrzy.setBorder(BorderFactory.createTitledBorder("Przydziel rêcznie kategoriê lub czas:"));
		
		JPanel panelKategori = new JPanel();
		panelKategori.setLayout(new BoxLayout(panelKategori,BoxLayout.X_AXIS));
		JLabel katLabel = new JLabel("   Kategoria: ");
		kat1Button = new JRadioButton("I  ");
		kat2Button = new JRadioButton("II ");
		kat3Button = new JRadioButton("III");
		kat4Button = new JRadioButton("IV      ");
		
		//pogrupowanie radio buttons.
        ButtonGroup groupKat = new ButtonGroup();
        groupKat.add(kat1Button);
        groupKat.add(kat2Button);
        groupKat.add(kat3Button);
        groupKat.add(kat4Button);
        
        kat1Button.addActionListener(this);
        kat2Button.addActionListener(this);
        kat3Button.addActionListener(this);
        kat4Button.addActionListener(this);
        
        kat1Button.setHorizontalAlignment(AbstractButton.CENTER);
        kat2Button.setHorizontalAlignment(AbstractButton.CENTER);
        kat3Button.setHorizontalAlignment(AbstractButton.CENTER);
        kat4Button.setHorizontalAlignment(AbstractButton.CENTER);
        
        pomoc = new JButton("Pomoc");
        pomoc.setPreferredSize(new Dimension(80,30));
        pomoc.setMaximumSize(new Dimension(80,30));
        pomoc.addActionListener(this);
       
        panelKategori.add(katLabel);
        panelKategori.add(kat1Button);
        panelKategori.add(kat2Button);
        panelKategori.add(kat3Button);
        panelKategori.add(kat4Button);
        panelKategori.add(pomoc);
        
        JLabel czasLabel = new JLabel("                Czas trwania wyjazdu (min): ");
        czasT = new JTextField();
        czasT.setPreferredSize(new Dimension(50,20));
        czasT.setMaximumSize(new Dimension(50,20));
        panelKategori.add(czasLabel);
        panelKategori.add(czasT);
       
        zmienKat = new JButton("Zapisz");
        zmienKat.setPreferredSize(new Dimension(120,30));
        zmienKat.setMaximumSize(new Dimension(120,30));
        zmienKat.addActionListener(this);
        
        JPanel zmienPanel = new JPanel();
        zmienPanel.add(zmienKat);
        panelKatPrzy.add(panelKategori);
        panelKatPrzy.add(zmienPanel);
        
        
		return panelKatPrzy;
	}
	
	
	//metoda tworzaca panel opisu
	private JPanel createPanelOpis(){
		JPanel panelOpis = new JPanel();
		panelOpis.setLayout(new BoxLayout(panelOpis,BoxLayout.X_AXIS));
		panelOpis.setBorder(BorderFactory.createTitledBorder("Opis wyjazdu:"));
		
		opis = new JTextArea();
		
		if(wyjazdy!=null) {
			
			//popraw to bo jak nie ma wyjazdu na pierwszej sluzby to bl¹d
			OpisWyjazdu	text = new OpisWyjazdu(wyjazdy.get(0));
			
			opis.setText(text.getOpis());
		}
		opis.setMargin(new Insets(20,20,20,20));
		opis.setLineWrap(true);
		opis.setWrapStyleWord(true);
		opis.setCaretPosition(1);
		JScrollPane scp = new JScrollPane(opis);
		scp.setAutoscrolls(false);
		panelOpis.add(scp);
		
		return panelOpis;
	}
	
	//metoda dodajaca koncowy panel z ok i anuluj
	private JPanel createPanelButton(){
		JPanel panelButton = new JPanel();
		zachowajZmianyButton = new JButton("Zachowaj zmiany");
		anulujButton = new JButton("Anuluj");
		zachowajZmianyButton.addActionListener(this);
		anulujButton.addActionListener(this);
		
		panelButton.add(zachowajZmianyButton);
		panelButton.add(anulujButton);
		
		return panelButton;
	}
	
	//metoda wrzucajaca wszystkie komponenty do glownego panelu 
	private void createAndShowDialog(){
		
		wyjazdyAll = new ArrayList<Wyjazd>();
		
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new BoxLayout(panelNorth,BoxLayout.Y_AXIS));
		
		JPanel panelTable = new JPanel();
		panelTable.setLayout(new BoxLayout(panelTable,BoxLayout.X_AXIS));
		
		panelTable.add(createDataChooser());
		panelTable.add(createTabelaWyjazdow());
		
		panelNorth.add(panelTable);
		panelNorth.add(createPanelKat());
		
		this.getContentPane().add(panelNorth, BorderLayout.NORTH);
		
		this.getContentPane().add(createPanelOpis(), BorderLayout.CENTER);
		this.getContentPane().add(createPanelButton(), BorderLayout.SOUTH);
		
		updateTabelaWyjazdy();
	}
	
	private void updateTabelaWyjazdy(){
		JTable tabela_new = new TabelaWyjazdy(szkodliwe, nrSluzby, zmiana);
		tabela.removeAll();
		tabela.setModel(tabela_new.getModel());
		
		//szerokosci
		tabela.getColumnModel().getColumn( 0 ).setCellRenderer( tabela.getTableHeader().getDefaultRenderer() );
		tabela.getColumnModel().getColumn( 0 ).setPreferredWidth( 23 );
		tabela.getColumnModel().getColumn( 0 ).setMaxWidth( 23 );
		tabela.getColumnModel().getColumn( 1 ).setPreferredWidth( 90 );
		tabela.getColumnModel().getColumn( 1 ).setMaxWidth( 90 );
		tabela.getColumnModel().getColumn( 2 ).setPreferredWidth( 120 );
		tabela.getColumnModel().getColumn( 2 ).setMaxWidth( 120 );
		tabela.getColumnModel().getColumn( 3 ).setPreferredWidth( 50 );
		tabela.getColumnModel().getColumn( 3 ).setMaxWidth( 50 );
		tabela.getColumnModel().getColumn( 5 ).setPreferredWidth( 40 );
		tabela.getColumnModel().getColumn( 5 ).setMaxWidth( 40 );
        
        
        //ustawienie czcionki i tla nag³ówka
		tabela.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,13));
		tabela.getTableHeader().setBackground(new Color(220,231,248));
		
		tabela.setPreferredScrollableViewportSize( tabela.getPreferredSize() );
		
		
		
		if(wyjazdy!=null) tabela.changeSelection(0, 1, false, false);
		
		//odswiezanie
    	tabela.repaint();
        scrollPane.repaint();
	}
	
	private void createWyjazdy(){
		wyjazdy = new ArrayList<Wyjazd>();
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
		
	}
	

	//akcja po kliknieciu w wiersz
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
         
            //przypisz do pola wyjazd wyjazd wskazany wierszem tabeli
            
            if(wyjazdy.size()!=0){
            	OpisWyjazdu text;
            	if(tabela.getSelectedRow()==-1) text = new OpisWyjazdu(wyjazdy.get(0));
            	else text = new OpisWyjazdu(wyjazdy.get(tabela.getSelectedRow()));
            	opis.setText(text.getOpis());
            	opis.setCaretPosition(1);
            	if(wyjazdy.get(tabela.getSelectedRow()).getKategoria()==0) kat1Button.setSelected(true);
            	if(wyjazdy.get(tabela.getSelectedRow()).getKategoria()==1) kat2Button.setSelected(true);
            	if(wyjazdy.get(tabela.getSelectedRow()).getKategoria()==2) kat3Button.setSelected(true);
            	if(wyjazdy.get(tabela.getSelectedRow()).getKategoria()==3) kat4Button.setSelected(true);
            }
            opis.repaint();
        }
    }

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==dataChooser){
			nrSluzby = dataChooser.getSelectedIndex();
			updateTabelaWyjazdy();
		}
		if(e.getSource()==kat1Button){
			kat=0;
		}
		if(e.getSource()==kat2Button){
			kat=1;
		}
		if(e.getSource()==kat3Button){
			kat=2;
		}
		if(e.getSource()==kat4Button){
			kat=3;
		}
		if(e.getSource()==zmienKat){
			wyjazdy.get(tabela.getSelectedRow()).setKategoria(kat);
			
			/*if(czasT.getText()!=""){
				int czas;
				try{
					czas = Integer.parseInt(czasT.getText());
					wyjazdy.get(tabela.getSelectedRow()).setCzas(czas);
				}
				catch (Exception ex) {
		            System.out.println("Podana z³a wartosc" + ex.getMessage());
		        }
			}*/
			wyjazdyAll.add(wyjazdy.get(tabela.getSelectedRow()));
			updateTabelaWyjazdy();
		}
		if(e.getSource()==zachowajZmianyButton){
			//dodajNaSzybko();
			//usunNaSzybko();
			og.updateSzkodliwe(wyjazdyAll);
			og.createDataObject();
			og.updateTable();
			this.dispose();
		}
		if(e.getSource()==anulujButton){
			this.dispose();
		}
	}
	
	
	    private void dodajNaSzybko(){
		
		byte kat = 0;
		Wyjazd wyjazd = new Wyjazd(9998,9998,new Date(),1440,kat,new String[2]);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(18).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][28].getSluzby().get(18).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][10].getSluzby().get(19).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][49].getSluzby().get(19).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][65].getSluzby().get(20).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][48].getSluzby().get(20).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][9].getSluzby().get(20).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][37].getSluzby().get(20).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][0].getSluzby().get(20).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][9].getSluzby().get(21).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][49].getSluzby().get(21).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][46].getSluzby().get(21).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][38].getSluzby().get(21).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][63].getSluzby().get(21).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][61].getSluzby().get(22).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][21].getSluzby().get(22).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][27].getSluzby().get(22).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][13].getSluzby().get(22).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][7].getSluzby().get(22).addWyjazd(wyjazd);
		
		//_______________________BYDGOSZCZ____________________________________
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(23).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(24).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(25).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(26).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(27).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(28).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(29).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][23].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][64].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][40].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][4].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][2].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][36].getSluzby().get(30).addWyjazd(wyjazd);
		
		//_________________________________BYDGOSZCZ END_________________
		
		szkodliwe.getFireman()[zmiana][72].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][84].getSluzby().get(23).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][35].getSluzby().get(23).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][48].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][9].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][70].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][11].getSluzby().get(24).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][66].getSluzby().get(24).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][38].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][22].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][46].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][53].getSluzby().get(25).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][47].getSluzby().get(25).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][61].getSluzby().get(26).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][34].getSluzby().get(26).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][22].getSluzby().get(27).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][63].getSluzby().get(27).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][13].getSluzby().get(28).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][37].getSluzby().get(28).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][59].getSluzby().get(29).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][43].getSluzby().get(29).addWyjazd(wyjazd);
		
		szkodliwe.getFireman()[zmiana][68].getSluzby().get(30).addWyjazd(wyjazd);
		szkodliwe.getFireman()[zmiana][5].getSluzby().get(30).addWyjazd(wyjazd);
		
	    }
		private void usunNaSzybko(){
			szkodliwe.getFireman()[zmiana][0].getSluzby().get(0).getWyjazdy().remove(0);
		
		}
		private void dodajNaSzybko2(){
			
			byte kat2 = 3;
			Wyjazd wyjazd2 = new Wyjazd(9998,9998,new Date(),227,kat2,new String[2]);
			szkodliwe.getFireman()[zmiana][27].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][17].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][16].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][2].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][20].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][11].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][9].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][8].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][22].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][2].getSluzby().get(6).addWyjazd(wyjazd2);
			szkodliwe.getFireman()[zmiana][14].getSluzby().get(6).addWyjazd(wyjazd2);
		}
}
