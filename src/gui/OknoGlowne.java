package gui;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;

import logika.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import tabela.ColumnGroup;
import tabela.GroupableTableColumnModel;
import tabela.GroupableTableHeader;
import tabela.MultiLineTableCellRenderer;
import tabela.NazwiskoCellRenderer;
import tabela.KategoriaCellRenderer;

import java.text.SimpleDateFormat;


public class OknoGlowne implements ActionListener {
 
	private JFrame frame;
	
	protected ImageIcon connectButtonIcon, exportButtonIcon, executeButtonIcon, editButtonIcon, printButtonIcon;
	protected JButton connectButton, exportButton, executeButton, editButton, printButton;
	protected JLabel zmianaLabel, polaczonoLabel;
	protected JRadioButton zmiana1Button, zmiana2Button, zmiana3Button; 
	protected JRadioButton przedzialCzas1Button, przedzialCzas2Button, przedzialCzas3Button;
	protected JComboBox miesiaceList, rokList, rokMiesiacList;
	protected JMenuBar menuBar;
	protected JMenu plikMenu, ustawieniaMenu, oProgramieMenu;
	protected JMenuItem drukujMenuItem, ustawDrukMenuItem, zamknijMenuItem, polaczMenuItem, ustawieniaMenuItem, ustawBazeMenuItem;
	protected JToolBar toolBar;
	protected JScrollPane scrollPanel;
	protected JPanel statusBar;
	protected JTextArea textArea;
	protected JDateChooser dateFromChooser, dateToChooser;
	
	protected JTable table;
	
	protected Date dateFrom, dateTo;
	protected byte przedzialCzasu=0;
	protected int zmiana=0;
	
	private ArrayList<String[]> tabelaCSV;
	
	
	private Szkodliwe szkodliwe;
	
	public OknoGlowne(JFrame frame){
		this.frame = frame;
	}

	// metoda tworz¹ca Menu
	public JMenuBar createMenuBar() {
		menuBar = new JMenuBar();
    	
    	plikMenu = new JMenu("Plik");
    	plikMenu.setMnemonic(KeyEvent.VK_P);
    	menuBar.add(plikMenu);
		
    	polaczMenuItem = new JMenuItem("Po³¹cz z baz¹ danych");
    	plikMenu.add(polaczMenuItem);
    	
    	drukujMenuItem = new JMenuItem("Drukuj");
    	plikMenu.add(drukujMenuItem);
    	
    	ustawDrukMenuItem = new JMenuItem("Ustawienia drukowania");
    	plikMenu.add(ustawDrukMenuItem);
    	
    	zamknijMenuItem = new JMenuItem("Zamknij");
    	plikMenu.add(zamknijMenuItem);
    	
    	ustawieniaMenu = new JMenu("Ustawienia");
    	ustawieniaMenu.setMnemonic(KeyEvent.VK_U);
    	menuBar.add(ustawieniaMenu);
		
    	ustawBazeMenuItem = new JMenuItem("Ustawienia bazy danych");
    	ustawieniaMenu.add(ustawBazeMenuItem);
    	
    	ustawieniaMenuItem = new JMenuItem("Ustawienia tabeli");
    	ustawieniaMenu.add(ustawieniaMenuItem);
    	
    	oProgramieMenu = new JMenu("O programie");
    	oProgramieMenu.setMnemonic(KeyEvent.VK_O);
    	menuBar.add(oProgramieMenu);
  
    	return menuBar;
	}
	
	//metoda tworz¹ca ToolBar (pasek z przyciskami i ustawieniami w oknie g³ównym)
    public Component createToolBar() {
    	
    	
    	toolBar = new JToolBar("pasek narzedzi");
    	toolBar.setFloatable(false);
    	
    	//Przycisk polacz z baz¹
    	
    	connectButtonIcon = createImageIcon("connect.png");
        
    	connectButton = new JButton("Polacz z BD", connectButtonIcon);
    	connectButton.setVerticalTextPosition(AbstractButton.BOTTOM);
    	connectButton.setHorizontalTextPosition(AbstractButton.CENTER);
    	connectButton.setMaximumSize(new Dimension(90,90));
    	
    	connectButton.addActionListener(this);   
        
    	//Pole wyboru zmiany s³u¿bowej - radio button
    	zmiana1Button = new JRadioButton("I  ");
    	zmiana2Button = new JRadioButton("II ");
    	zmiana3Button = new JRadioButton("III");
    	
        //pogrupowanie radio buttons.
        ButtonGroup groupZmiana = new ButtonGroup();
        groupZmiana.add(zmiana1Button);
        groupZmiana.add(zmiana2Button);
        groupZmiana.add(zmiana3Button);
        
        //wyrównanie radiobutton na srodek
        zmiana1Button.setHorizontalAlignment(AbstractButton.CENTER);
        zmiana2Button.setHorizontalAlignment(AbstractButton.CENTER);
        zmiana3Button.setHorizontalAlignment(AbstractButton.CENTER);
        
        //Dodanie przycisków do ActionListener
        zmiana1Button.addActionListener(this);
        zmiana2Button.addActionListener(this);
        zmiana3Button.addActionListener(this);


        //panel zmiany
        
        JPanel zmianaPanel = new JPanel(new GridLayout(0, 1));
        zmianaPanel.setBorder(BorderFactory.createTitledBorder("Zmiana:"));
        zmianaPanel.setMaximumSize(new Dimension(60,110));
        
        zmianaPanel.add(zmiana1Button);
        zmianaPanel.add(zmiana2Button);
        zmianaPanel.add(zmiana3Button);
        
        
        //panel przedzia³u czasowego
        
        //Pole wyboru pzredzia³u czasowego - radio button
        przedzialCzas1Button = new JRadioButton("miesi¹c:");
        przedzialCzas2Button = new JRadioButton("rok:");
        przedzialCzas3Button = new JRadioButton("zakres:");
        
        przedzialCzas1Button.addActionListener(this);
        przedzialCzas2Button.addActionListener(this);
        przedzialCzas3Button.addActionListener(this);
        
    	
        //pogrupowanie radio buttons.
        ButtonGroup groupCzas = new ButtonGroup();
        groupCzas.add(przedzialCzas1Button);
        groupCzas.add(przedzialCzas2Button);
        groupCzas.add(przedzialCzas3Button);
        
        
        //combobox z miesi¹cami
        String[] miesiaceString = { "Styczeñ", "Luty", "Marzec", "Kwiecieñ", "Maj", "Czerwiec", "Lipiec",
        							"Sierpieñ", "Wrzesieñ", "PaŸdziernik", "Listopad", "Grudzieñ"};
        
        miesiaceList = new JComboBox(miesiaceString);
        
        //combobox z rokiem - to jest do przerobki -tu musi pobierac z bazy
        String[] rokString = { "2000", "2001", "2002", "2003", "2004", "2005", "2006",
        							"2007", "2008", "2009", "2010", "2011"};
        
        rokList = new JComboBox(rokString);
        rokList.addActionListener(this); 
        
        rokMiesiacList = new JComboBox(rokString);
        rokMiesiacList.addActionListener(this); 
        
        //Wybór daty od do
        dateFromChooser = new JDateChooser();
        
        dateToChooser = new JDateChooser();
        
        JPanel zakresDatyPanel = new JPanel();
        zakresDatyPanel.add(dateFromChooser);
        zakresDatyPanel.add(new JLabel(" - "));
        zakresDatyPanel.add(dateToChooser);
        
    	
        //panel przedzialu czasowego z ramk¹ i pogrupowaniem komponentow za pomoca GridBagLayout
        JPanel przedzialCzasPanel = new JPanel();
        przedzialCzasPanel.setBorder(BorderFactory.createTitledBorder("Przedzia³ czasowy:"));
        przedzialCzasPanel.setMaximumSize(new Dimension(300,110));
        
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        przedzialCzasPanel.setLayout(gridbag);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        przedzialCzasPanel.add(przedzialCzas1Button, c);
        przedzialCzasPanel.add(miesiaceList, c);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        przedzialCzasPanel.add(rokMiesiacList, c);
        
        c.gridwidth = 1; 
        przedzialCzasPanel.add(przedzialCzas2Button, c);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        przedzialCzasPanel.add(rokList, c);

        c.gridwidth = 1; 
        przedzialCzasPanel.add(przedzialCzas3Button, c);
        
        c.gridwidth = GridBagConstraints.REMAINDER;
        przedzialCzasPanel.add(zakresDatyPanel, c);

        //Przycisk Wykonaj
        
        executeButtonIcon = createImageIcon("execute.png");
        
        executeButton = new JButton("Wykonaj", executeButtonIcon);
        executeButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        executeButton.setHorizontalTextPosition(AbstractButton.CENTER);
        executeButton.setMaximumSize(new Dimension(90,90));
        
        
        executeButton.addActionListener(this);
        
        //Przycisk Edytuj
        
        editButtonIcon = createImageIcon("edit.png");
        
        editButton = new JButton("Edytuj", editButtonIcon);
        editButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        editButton.setHorizontalTextPosition(AbstractButton.CENTER);
        editButton.setMaximumSize(new Dimension(90,90));
        
        editButton.addActionListener(this);
        
        
        //Przycisk Ustawienia
        
        exportButtonIcon = createImageIcon("csv.png");
        
        exportButton = new JButton("Eksportuj", exportButtonIcon);
        exportButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        exportButton.setHorizontalTextPosition(AbstractButton.CENTER);
        exportButton.setMaximumSize(new Dimension(90,90));
        
        exportButton.addActionListener(this); 
        
        
        //Przycisk Drukuj
        
        printButtonIcon = createImageIcon("print.png");
        
        printButton = new JButton("Drukuj", printButtonIcon);
        printButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        printButton.setHorizontalTextPosition(AbstractButton.CENTER);
        printButton.setMaximumSize(new Dimension(90,90));
        
        printButton.addActionListener(this);
        
        //Dodawanie komponentów do toolbar
        
        toolBar.add(connectButton);
        toolBar.addSeparator();
        toolBar.add(zmianaPanel);
        toolBar.addSeparator();
        toolBar.add(przedzialCzasPanel);
        toolBar.addSeparator();
        toolBar.add(executeButton);
        toolBar.addSeparator();
        toolBar.add(editButton);
        toolBar.addSeparator();
        toolBar.add(exportButton);
        toolBar.addSeparator();
        toolBar.add(printButton);
        
        //Ustawienie startowych przedzialow czasu
        przedzialCzas1Button.setSelected(true);
		Date teraz = new Date();
		miesiaceList.setSelectedIndex(teraz.getMonth());
		rokList.setSelectedIndex(9);
		rokMiesiacList.setSelectedIndex(9);
		dateFromChooser.setDate(teraz);
		dateToChooser.setDate(teraz);
		zmiana1Button.setSelected(true);
        
        //ustawienie pozycji poczatkowych
		dateFromChooser.setEnabled(false);
		dateToChooser.setEnabled(false);
		executeButton.setEnabled(false);
		editButton.setEnabled(false);
		printButton.setEnabled(false);
		przedzialCzas1Button.setEnabled(false);
		miesiaceList.setEnabled(false);
		zmiana1Button.setEnabled(false);
		zmiana2Button.setEnabled(false);
		zmiana3Button.setEnabled(false);
		przedzialCzas1Button.setEnabled(false);
		przedzialCzas2Button.setEnabled(false);
		przedzialCzas3Button.setEnabled(false);
		rokList.setEnabled(false);
		rokMiesiacList.setEnabled(false);
		
        return toolBar;
    }
 
    public Component createTablePanel(){
    	
    	table = new JTable();
    	table.setColumnModel(new GroupableTableColumnModel());
    	table.setTableHeader(new GroupableTableHeader((GroupableTableColumnModel)table.getColumnModel()));
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
    	scrollPanel = new JScrollPane(table);

    	return scrollPanel;
    }
    
    public Component createStatusBar(){
    	statusBar = new JPanel();
    	statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
    	JLabel polaczenieLabel = new JLabel("     Po³¹czenie z baz¹ danych:   ");
    	polaczonoLabel = new JLabel("BRAK PO£¥CZENIA   ");
    	polaczonoLabel.setForeground(Color.red);
    	statusBar.add(polaczenieLabel);
    	statusBar.add(polaczonoLabel);
    	
    	return statusBar;
    }
    
    //metoda ustawiaj¹ca datê pocz¹tkow¹ i koñcow¹ przedzia³u czasowego w typie Date
    public void setDates(){
    	dateFrom = new Date();
    	dateTo = new Date();
    	
    	int year = rokMiesiacList.getSelectedIndex()+100;
    	int [] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    	
    	// dla roku przestepnego
    	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) daysInMonths[1] = 29;
    	
    	//01-01-2000 - I zmiana
    	Date pierwszyDzien = new Date(100, 0, 1);
    	
    	if(przedzialCzasu==0){
    		
    		dateFrom.setDate(1);
    		dateFrom.setMonth(miesiaceList.getSelectedIndex());		
    		dateFrom.setYear(year);
    		dateFrom.setHours(7);			//zmiana sluzby, zdazaja sie wyjazdy przed 8, 15 min rezerwy
    		dateFrom.setMinutes(45);
    		dateFrom.setSeconds(0);
 
    		dateTo.setDate(daysInMonths[dateFrom.getMonth()]);
    		dateTo.setMonth(dateFrom.getMonth());
    		dateTo.setYear(year);
    		dateTo.setHours(0);
    		dateTo.setMinutes(0);
    		dateTo.setSeconds(0);
		
    	}
    	else
    	if(przedzialCzasu==1){
    		
    		dateFrom.setDate(1);
    		dateFrom.setMonth(0);
    		dateFrom.setYear(year);
    		
    		dateTo.setDate(31);
    		dateTo.setMonth(11);
    		dateTo.setYear(year);
    		
    	}
    	else
    	if(przedzialCzasu==2){
    		dateFrom = dateFromChooser.getDate();
    		dateFrom.setHours(7);
    		dateFrom.setMinutes(45);
    		dateFrom.setSeconds(0);
    		
    		dateTo = dateToChooser.getDate();
    		dateTo.setHours(0);
    		dateTo.setMinutes(0);
    		dateTo.setSeconds(0);
    	}
    }
    

    private String[] createColumnNames(){
    	SimpleDateFormat  dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	Calendar[] daty = szkodliwe.findAllDatesOfPeriod();
    	int iloscDat = daty.length;
    	String[] columnNames = new String[iloscDat+6];
    	
    	columnNames[0] = "LP.";
    	columnNames[1] = "NAZWISKO IMIÊ";
    	
    	for(int i=0;i<iloscDat;i++){
    		Date data = daty[i].getTime();
    		columnNames[i+2] = dateFormat.format(data);
    	}
    	
    	columnNames[2+iloscDat]= "I";
    	columnNames[2+iloscDat+1]= "II";
    	columnNames[2+iloscDat+2]= "III";
    	columnNames[2+iloscDat+3]= "IV";
    	
    	return columnNames;
    }
    
    private void createTableModel(JTable table, String[] columnNames){
    	
    	GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
        ColumnGroup g_data = new ColumnGroup( "DATA S£U¯BY");
        for(int i=2;i<columnNames.length-4;i++)
        	g_data.add(cm.getColumn(i));
        ColumnGroup g_kat = new ColumnGroup( "KAT. UCI¥¯LIWOŒCI");
        	g_kat.add(cm.getColumn(columnNames.length-4));
        	g_kat.add(cm.getColumn(columnNames.length-3));
        	g_kat.add(cm.getColumn(columnNames.length-2));
        	g_kat.add(cm.getColumn(columnNames.length-1));
        GroupableTableHeader header = (GroupableTableHeader)table.getTableHeader();
        cm.addColumnGroup(g_data);
        cm.addColumnGroup(g_kat);
        
    }
    
    protected Object[][] createDataObject(){
    	
    	tabelaCSV = new ArrayList<String[]>();
    	
    	
    	szkodliwe.sumowanieCzasow();
    	
    	int iloscKolumn = createColumnNames().length;
    	int iloscWierszy=0;
    	for(int i=0;i<szkodliwe.getFireman()[zmiana].length;i++)
    		if(szkodliwe.getFireman()[zmiana][i]!=null) iloscWierszy++;
    	Object[][] data = new Object[iloscWierszy][iloscKolumn];
    	for(int j=0;j<iloscWierszy;j++){
    		data[j][0] = j+1;
    		data[j][1] = szkodliwe.getFireman()[zmiana][j].getName();
    		String[] wierszCSV = new String[5];
    		wierszCSV[0] = szkodliwe.getFireman()[zmiana][j].getName();
    			for(int l=0;l<szkodliwe.getFireman()[zmiana][j].getSluzby().size();l++){

    				long czas1 = szkodliwe.getFireman()[zmiana][j].getSluzby().get(l).getKat1();
    				long czas2 = szkodliwe.getFireman()[zmiana][j].getSluzby().get(l).getKat2();
    				long czas3 = szkodliwe.getFireman()[zmiana][j].getSluzby().get(l).getKat3();
    				long czas4 = szkodliwe.getFireman()[zmiana][j].getSluzby().get(l).getKat4();
    			
    				//formatowanie stringow do wyswietlenia
    				String sczas1="            ", sczas2="        ", sczas3="\n            ", sczas4="        ";
    				
    				if(czas1!=0) sczas1 = "I-"+formatujCzas(czas1);
    				if(czas2!=0) sczas2 = "   II-"+formatujCzas(czas2);
    				if(czas3!=0) sczas3 = "\nIII-"+formatujCzas(czas3);
    				if(czas4!=0) sczas4 = " IV-"+formatujCzas(czas4);
    				
    				data[j][l+2] = sczas1+sczas2+sczas3+sczas4;
    			}
    		
    		data[j][iloscKolumn-4] = wierszCSV[1] = formatujCzasKat(szkodliwe.getFireman()[zmiana][j].getKat1());
    		data[j][iloscKolumn-3] = wierszCSV[2] = formatujCzasKat(szkodliwe.getFireman()[zmiana][j].getKat2());
    		data[j][iloscKolumn-2] = wierszCSV[3] = formatujCzasKat(szkodliwe.getFireman()[zmiana][j].getKat3());
    		data[j][iloscKolumn-1] = wierszCSV[4] = formatujCzasKat(szkodliwe.getFireman()[zmiana][j].getKat4());
    		
    		tabelaCSV.add(wierszCSV);
	
    	}
    	return data;
    }
    
    private String formatujCzas(long czas){
    	String sczas;
    	long godziny = czas/60;
    	long minuty = czas%60;
    	if(godziny<10) sczas = "0"+godziny;
    	else sczas = Long.toString(godziny);
    	if(minuty<10) sczas = sczas+":0"+minuty;
    	else sczas = sczas+":"+minuty;
    	
    	
    	return sczas;
    }
    
    private String formatujCzasKat(long czas){
    	String sczas;
    	long godziny = czas/60;
    	long minuty = czas%60;
    	if(minuty>30) godziny++;
    	
    	sczas = Long.toString(godziny);
    	
    	return sczas;
    }
   
    //metoda aktualizujaca szkodliwe z okna edytuj
    protected void updateSzkodliwe(ArrayList<Wyjazd> wyjazdy){
    	for(int i=0;i<szkodliwe.getFireman()[zmiana].length;i++)
    		if(szkodliwe.getFireman()[zmiana][i]!=null){
    			//przejdz po wszystkich sluzbach u wszystkich kazdego strazaka
    			System.out.println(i+". Strazak: "+szkodliwe.getFireman()[zmiana][i].getName());
    			for(int j=0;j<szkodliwe.getFireman()[zmiana][i].getSluzby().size();j++){
    				//przejdz po wszystkich wyjazdach w kazdej sluzbie
    				System.out.println("\t"+j+". Sluzba: "+szkodliwe.getFireman()[zmiana][i].getName());
    				for(int k=0;k<szkodliwe.getFireman()[zmiana][i].getSluzby().get(j).getWyjazdy().size();k++){
    					//przejdz po kazdym wyjezdzie w przekazanej liscie
    					System.out.println("\t\t"+k+". Wyjazd: "+szkodliwe.getFireman()[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getIdMeldunku());
    					for(int l=0;l<wyjazdy.size();l++){
    						//jesli znajdziesz wyjazd o tym samym id co w liscie to uaktualnij kategorie
    						System.out.println("\t\t\t"+l+". Wyjazd: "+wyjazdy.get(l).getIdMeldunku());
    						if(szkodliwe.getFireman()[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).getIdMeldunku()
    								==wyjazdy.get(l).getIdMeldunku()){
    							szkodliwe.getFireman()[zmiana][i].getSluzby().get(j).getWyjazdy().get(k).setKategoria(wyjazdy.get(l).getKategoria());
    							System.out.println("\t\t\t\tZGADZA SIE");
    						}
    						
    							
    					}
    				}
    			}
    		}
    }
    
    //metoda aktualizuj¹ca tabele wyników
    public void updateTable(){
    	
    	DefaultTableModel dm = new DefaultTableModel()
    	{
  	      	public Class<String> getColumnClass(int columnIndex) {
  	      		return String.class;
  	      	}
  	      	public boolean isCellEditable(int row, int column) {
  	      		return false;
  	      	}
    	};
    	
        String[] columnNames = createColumnNames();
        Object[][] data = createDataObject();
    	dm.setDataVector(data, columnNames);
    	table.setModel(dm);

    	createTableModel(table, columnNames);
    
    	//kolumna imie i nazwisko rozszerzone, wyrownanie do srodka i pogrubiona czcionka
        table.getColumnModel().getColumn( 1 ).setPreferredWidth( 150 );
        table.getColumnModel().getColumn(1).setCellRenderer(new NazwiskoCellRenderer());

        //pogrubienie LP.
        table.getColumnModel().getColumn( 0 ).setCellRenderer( table.getTableHeader().getDefaultRenderer() );
        table.getColumnModel().getColumn( 0 ).setPreferredWidth( 30 );
        
        table.setPreferredScrollableViewportSize( table.getPreferredSize() );
    	
        //ustawienie szerokosci dat
    	for(int i=2;i<columnNames.length-4;i++){
    		table.getColumnModel().getColumn(i).setPreferredWidth( 90 );
    		//table.getColumnModel().getColumn(i).setCellEditor(cellEditor);
    	}
    	
    	//ustawienie szerokosci Kat. i czcionki i wyrownanie
    	for(int i=4;i>0;i--){
    		table.getColumnModel().getColumn(columnNames.length-i).setPreferredWidth( 45 );
    		table.getColumnModel().getColumn(columnNames.length-i).setCellRenderer(new KategoriaCellRenderer());
    	}
    	
    	//ustawienie czcionki i tla nag³ówka
    	table.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,13));
    	table.getTableHeader().setBackground(new Color(220,231,248));
    	
    	//us
  
    	table.setRowHeight( table.getRowHeight() * 2);
    	
    	table.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
    	TableRowSorter<? extends TableModel> sort = new TableRowSorter<DefaultTableModel>(dm);
    	table.setRowSorter(sort);
    	
    	//odswiezanie
    	table.repaint();
        scrollPanel.repaint();
    } 
    
    public void polaczBD(){
    	
    	szkodliwe = new Szkodliwe();
    	if(szkodliwe.isConnected()){
    		
    		szkodliwe.setZmiana(zmiana);
    		polaczonoLabel.setText("PO£¥CZONO");
    		polaczonoLabel.setForeground(Color.green);
    		polaczonoLabel.repaint();
    		statusBar.repaint();
    		
    		//odblokowanie kontrolek
    		zmiana1Button.setEnabled(true);
    		zmiana2Button.setEnabled(true);
    		zmiana3Button.setEnabled(true);
    		przedzialCzas1Button.setEnabled(true);
    		przedzialCzas2Button.setEnabled(true);
    		przedzialCzas3Button.setEnabled(true);
    		miesiaceList.setEnabled(true);
    		rokMiesiacList.setEnabled(true);
    		
    		executeButton.setEnabled(true);
    	}
    	else{
    		polaczonoLabel.setText("B£¥D PO£¥CZENIA");
    		polaczonoLabel.setForeground(Color.red);
    		polaczonoLabel.repaint();
    		statusBar.repaint();
    	}
    	
    }
    
    public void Wykonaj(){
    	setDates();
    	
    	szkodliwe = new Szkodliwe();
    	
    	szkodliwe.setDates(dateFrom, dateTo);
    	szkodliwe.setZmiana(zmiana);
    	
    	szkodliwe.createResult();

		updateTable(); 
		
    	editButton.setEnabled(true);
    	printButton.setEnabled(true);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==connectButton)
    	{
    		polaczBD();
    	}
    	else
    	if (e.getSource()==zmiana1Button)
    	{
    			zmiana = 0;
    	}
    	else
        if (e.getSource()==zmiana2Button)
        {
        		zmiana = 1;
        }
        else
        if (e.getSource()==zmiana3Button)
        {
        		zmiana = 2;
        }
        else
        if (e.getSource()==przedzialCzas1Button)
        {	
        		przedzialCzasu = 0;
        		//zablokowanie i odblokowanie wybranych kontrolek
        		miesiaceList.setEnabled(true);
        		rokMiesiacList.setEnabled(true);
        		rokList.setEnabled(false);
        		dateFromChooser.setEnabled(false);
        		dateToChooser.setEnabled(false);
        		
        }
        else
        if (e.getSource()==przedzialCzas2Button)
        {
        		przedzialCzasu = 1;
        		//zablokowanie i odblokowanie wybranych kontrolek
        		miesiaceList.setEnabled(false);
        		rokMiesiacList.setEnabled(false);
        		rokList.setEnabled(true);
        		dateFromChooser.setEnabled(false);
        		dateToChooser.setEnabled(false);
        	
        }
        else
        if (e.getSource()==przedzialCzas3Button)
        {
        		przedzialCzasu = 2;
        		//zablokowanie i odblokowanie wybranych kontrolek
        		miesiaceList.setEnabled(false);
        		rokMiesiacList.setEnabled(false);
        		rokList.setEnabled(false);
        		dateFromChooser.setEnabled(true);
        		dateToChooser.setEnabled(true);
		
        }
        else
        if (e.getSource()==miesiaceList)
        {
        	setDates(); 		
        }
        else
        if (e.getSource()==rokMiesiacList)
        {
        	setDates();		
        }
        else
    	if (e.getSource()==rokList)
        {
    		setDates();		
        }
        else
        if (e.getSource()==dateFromChooser)
        {
        	//dateFrom = dateFromChooser.getDate();
        	setDates();
    				
        }
        else
        if (e.getSource()==dateToChooser)
        {
        	//dateTo = dateToChooser.getDate(); 
        	setDates();
        }
        else
        if (e.getSource()==executeButton)
        {
        	Wykonaj();     	
        	
        }
        else
        if (e.getSource()==editButton)
        {
            JDialog oknoEdytuj = new OknoEdytuj(this, szkodliwe, zmiana); 
            oknoEdytuj.setUndecorated(true);
            oknoEdytuj.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            oknoEdytuj.setLocationRelativeTo(plikMenu);
            oknoEdytuj.setSize(new Dimension(800, 600));
            oknoEdytuj.setVisible(true);
        }
        else
        if (e.getSource()==printButton)
        {
        	try {
                table.print();
              } catch (PrinterException pe) {
                System.err.println("B³¹d drukowania: " + pe.getMessage());
              }           		
        }
    	if (e.getSource()==exportButton)
        {
    		final JFileChooser fc = new JFileChooser();
    		fc.setSelectedFile(new File("szkodliwe.csv"));
			int returnVal = fc.showSaveDialog(this.frame);  
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            Eksportuj eksportuj = new Eksportuj(tabelaCSV, file);
	            try {
					eksportuj.writeToFile();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        } else {
	            //Cancel
	        }
        }
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = OknoGlowne.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    
    private static void createAndShowGUI() {
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Szkodliwe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        OknoGlowne app = new OknoGlowne(frame);
        JMenuBar menuBar = app.createMenuBar();
        Component contents = app.createToolBar();
        Component status = app.createStatusBar();
        Component tablePanel = app.createTablePanel();
        
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(contents, BorderLayout.NORTH);
        frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
        frame.getContentPane().add(status, BorderLayout.SOUTH);

        
        frame.setSize(900, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}



