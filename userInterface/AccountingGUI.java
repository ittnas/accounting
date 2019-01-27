package userInterface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import javax.swing.ListSelectionModel;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.border.TitledBorder;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultTreeCellRenderer;

import coreClasses.Account;
import coreClasses.AccountMap;
import coreClasses.AccountMapWriter;
import coreClasses.AccountMapXMLWriter;
import coreClasses.AccountTree;
import coreClasses.ExcelNoteReader;
import coreClasses.GraphReader;
import coreClasses.GraphState;
import coreClasses.GraphWriter;
import coreClasses.Note;
import coreClasses.NoteHolder;
import coreClasses.TableNoteReader;
import coreClasses.Template;
import coreClasses.GraphState.StepType;
import dataStructures.SortedList;

public class AccountingGUI extends JFrame implements ActionListener,
		WindowListener, ComponentListener, MouseListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new AccountingGUI();
	}

	
	private JTextArea statusTextArea;
	private JPanel noteBook;
	private JPanel statistics;
	private JMenuItem newFile;
	private JFileChooser fc;
	private JMenuItem openFile;
	private JMenuItem saveAs;
	private JMenuItem save;
	private JMenuItem exit;
	private Properties defaultProps;
	private Properties applicationProps;
	private Properties cache;
	public static int fontSize;
	public static Color fontColor;
	public static Font font;
	public static Font titleFont;
	public static Font boldFont;
	private JPanel accountManager;
	private JFormattedTextField dateField;
	private JTextField sumField;
	private AccountMap accountTree;
	private JButton addNoteButton;
	public static Color lineColor;
	private Java2sAutoComboBox accountChooser;
	private JFormattedTextField dateStartField;
	public static Color errorColor;
	private JFormattedTextField dateEndField;
	private JButton searchAccountButton;
	private JPanel noteBookContainer;
	private GridBagConstraints noteBookConstraints;
	public static DateFormat dateFormat;
	private JScrollPane scrollPane;
	private AddNotePanel addNotePanel;
	private JPanel accountChooserPanel;
	private JTable noteTable;
	private JPopupMenu editMenu;
	private JDialog addNotesFromTableDialog;
	private JMenuItem editNoteItem;
	private JMenuItem deleteNoteItem;
	private String[] currentSearch;
	private JTextField editSumField;
	private JButton editNoteButton;
	private JDialog editDialog;
	private JTextField accountValueField;
	private JTextField accountIntervalValueFIeld;
	private JScrollPane accountScrollPane;
	private JPanel lowerPanel;
	private JScrollPane accountManagerScroll;
	private JPopupMenu accountMenu;
	private JMenuItem renameAccountItem;
	private JMenuItem changeDescriptionAccountItem;
	private JMenuItem addChildAccountItem;
	private JMenuItem addAccountInPlaceItem;
	private JMenuItem removeAccountNotRecursiveItem;
	private JMenuItem removeAccountRecursiveItem;
	private AccountTree accountManagerTree;
	private JButton accountOkButton;
	private JButton accountCancelButton;
	private String fileName;
	private String saveName;
	private boolean askSave = false;
	private JMenuItem addNotes;
	private JMenuItem addNotesFromTable;
	private JMenu tools;
	private JPanel statisticsMainPanel;
	public GraphState defaultGraphState;
	private ArrayList<Java2sAutoComboBox> comboboxUpdateList;
	private ArrayList<GraphPanel> graphPanels;
	private ParallelGroup vertical;
	private SequentialGroup horizontal;
	private JPanel reports;
	private Java2sAutoComboBox templateSelector;
	private SimpleDateFormat messageWindowDateFormat;
	private JMenuItem moveAccountItem;

	public AccountingGUI() {
		super();
		initialize();
		createWindow();
		addWindowListener(this);
		this.addComponentListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(1200, 1000));
		setTitle("Kirjanpito");
		setVisible(true);
	}

	public void createWindow() {
		setContentPane(createContentPane());
		setJMenuBar(createMenuBar());
		getContentPane().add(createStatusWindow(), BorderLayout.SOUTH);
		getContentPane().add(createTabs(), BorderLayout.CENTER);
	}

	private JTabbedPane createTabs() {
		JTabbedPane tabbedPane = new JTabbedPane();
		noteBook = new JPanel();
		noteBook.setOpaque(false);
		statistics = new JPanel();
		statistics.setOpaque(false);
		accountManager = new JPanel();
		accountManager.setOpaque(false);
		reports = new JPanel();
		reports.setOpaque(false);
		tabbedPane.addTab("Päiväkirja", noteBook);
		tabbedPane.addTab("Tilien hallinta", accountManager);
		tabbedPane.addTab("Tilastot", statistics);
		tabbedPane.addTab("Raportit", reports);
		return tabbedPane;
	}

	private JScrollPane createStatusWindow() {

		statusTextArea = new JTextArea();
		statusTextArea.setRows(4);
		statusTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.getViewport().add(statusTextArea);
		return scrollPane;
	}

	@SuppressWarnings("serial")
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("Tiedosto");
		file.setMnemonic(KeyEvent.VK_T);
		menuBar.add(file);
		tools = new JMenu("Työkalut");
		file.setMnemonic(KeyEvent.VK_K);
		menuBar.add(tools);
		newFile = new JMenuItem("Uusi");
		newFile.setMnemonic(KeyEvent.VK_U);
		//fc = new JFileChooser(".");
		String initialPath = cache.getProperty("openFilePath");
		if(initialPath == null || Files.notExists(Paths.get(initialPath))) {
			initialPath = ".";
		}
		fc = new JFileChooser(initialPath){
			@Override
			public void approveSelection(){
			    File f = getSelectedFile();
			    if(f.exists() && getDialogType() == SAVE_DIALOG){
			        int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
			        switch(result){
			            case JOptionPane.YES_OPTION:
			                super.approveSelection();
			                return;
			            case JOptionPane.NO_OPTION:
			                return;
			            case JOptionPane.CANCEL_OPTION:
			                super.cancelSelection();
			                return;
			        	}
			    }
			    super.approveSelection();
			}
		};

		newFile.addActionListener(this);
		file.add(newFile);
		openFile = new JMenuItem("Avaa");
		openFile.setMnemonic(KeyEvent.VK_A);
		openFile.addActionListener(this);
		file.add(openFile);
		saveAs = new JMenuItem("Tallenna nimellä");
		saveAs.setMnemonic(KeyEvent.VK_V);
		saveAs.addActionListener(this);
		file.add(saveAs);
		save = new JMenuItem("Tallenna");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(this);
		file.add(save);

		exit = new JMenuItem("Lopeta");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(this);
		file.add(exit);
		
		addNotes = new JMenuItem("Lisää merkinnät tiedostosta");
		addNotes.setMnemonic(KeyEvent.VK_L);
		addNotes.addActionListener(this);
		tools.add(addNotes);
		tools.setEnabled(false);
		
		addNotesFromTable = new JMenuItem("Lisää merkinnät taulukosta");
		addNotesFromTable.setMnemonic(KeyEvent.VK_I);
		addNotesFromTable.addActionListener(this);
		tools.add(addNotesFromTable);
		tools.setEnabled(false);
		return menuBar;
	}

	private JPanel createContentPane() {
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(new BorderLayout());
		return contentPane;
	}

	private void initialize() {
		// TODO käsittele virhe, kun defaultPropertiesejä ei löydy
		defaultProps = new Properties();
		try {
			FileInputStream in = new FileInputStream("defaultProperties.txt");
			defaultProps.load(in);
			in.close();

			applicationProps = new Properties();
			applicationProps.putAll(defaultProps);
			try {
				in = new FileInputStream("Properties.txt");
			} catch (FileNotFoundException e) {
				new FileWriter("Properties.txt");
			}
			in = new FileInputStream("Properties.txt");
			applicationProps.load(in);
			in.close();
			
			try {
				fontSize = Integer.parseInt(applicationProps
						.getProperty("fontSize"));
			} catch (Exception e) {
				fontSize = 24;
			}
		} catch (IOException e) {
			System.err
					.print("File defaultProperties.txt was not found or could not be read.\n");
			System.exit(ERROR);
		}
		
		cache = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream("cache.txt");
			cache.load(in);
			in.close();
		} catch (IOException e) {
			// Cache.txt could not be found. Not a problem.
		}

		try {
			dateFormat = new SimpleDateFormat(applicationProps
					.getProperty("dateFormat"));
		} catch (Exception e) {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		}
		try {
			messageWindowDateFormat = new SimpleDateFormat(applicationProps
						.getProperty("messageWindowDateFormat"));
		} catch (Exception e) {
			messageWindowDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		}
		fontColor = parseColor("fontColor");
		lineColor = parseColor("lineColor");
		errorColor = parseColor("errorColor");
		try {
			String fontString = applicationProps.getProperty("font");
			font = Font.decode(fontString + "-" + fontSize);
			String newFont = font.getFontName() + "-BOLDITALIC" + "-"
					+ (int) (font.getSize() * 1.5);
			titleFont = Font.decode(newFont);
			boldFont = Font.decode(font.getFontName() + "-BOLDITALIC" + "-"
					+ font.getSize());
		} catch (Exception e) {
			font = Font.decode("Times-" + fontSize);
		}
		
		comboboxUpdateList = new ArrayList<Java2sAutoComboBox>();
		graphPanels = new ArrayList<GraphPanel>();
	}

	private Color parseColor(String colorName) {
		String colorComponents[] = applicationProps.getProperty(colorName)
				.split(",");
		int colorRGB[] = new int[4];
		for (int i = 0; i < colorComponents.length; i++) {
			colorRGB[i] = Integer.parseInt(colorComponents[i].trim());
		}
		return new Color(colorRGB[0], colorRGB[1], colorRGB[2], colorRGB[3]);
	}
	
	private void initializeNoteBook() {
		noteBook.add(createNoteBook());
		accountManagerScroll = createAccountScrollPane(accountTree.getRoot(), true);
		accountManagerTree = (AccountTree) accountManagerScroll
				.getViewport().getView();
		accountManagerTree.addMouseListener(this);
		resizeManagerPanel();
		accountManagerScroll.addMouseListener(this);
		createEditAccountsMenu();

		accountManager.add(accountManagerScroll);
		accountManager.addComponentListener(this);
		accountChooser.setSelectedItem(accountTree.getRoot().getName());
		searchAccount();
		tools.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit) {
			windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

		} else if (e.getSource() == newFile) {
			if(accountTree != null) {
				AccountingGUI gui = new AccountingGUI();
				gui.newFile();
			} else {
				newFile();
			}
			
		} else if(e.getSource() == openFile) {
			
			int returnVal = fc.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();

	            fileName = file.getPath();
	            openFile(fileName,fileName);
	            
	            try {
	            File fn = new File(fileName);
	            cache.setProperty("openFilePath", fn.getParent());
	            File cacheFile = new File("cache.txt");
	            cacheFile.createNewFile();
	            cache.store(new FileOutputStream("cache.txt"),null);
	            } catch (IOException er) {
	            	// Do nothing
	            }
	            
	        }

		} else if (e.getSource() == save) {
			save();
		} else if (e.getSource() == saveAs) {
			saveAs();
		}else if (e.getSource() == addNoteButton
				|| e.getSource() == editNoteButton) {
			AddNotePanel panel = (AddNotePanel) ((JButton) e.getSource())
					.getParent();
			try {
				Account credit = accountTree.getAccount(panel
						.getCreditAccountCombobox().getSelectedItem()
						.toString().trim());
				Account debet = accountTree.getAccount(panel
						.getDebetAccountCombobox().getSelectedItem().toString()
						.trim());
				if (debet == null || credit == null || debet.equals(credit)) {
					throw new NullPointerException();
				}
				double value = Double
						.parseDouble(panel.getSumField().getText());
				String description = panel.getDescriptionField().getText();
				Date date = dateFormat.parse(panel.getDateField().getText());

				new Note(value, date, description, debet, credit);
				askSave = true;
				if (e.getSource() == addNoteButton) {
					String message = "Lisättiin merkintä " + description + ".";
					updateStatus(message);
					panel.getDateField().requestFocus();
					
				}
				if (e.getSource() == editNoteButton) {

					String message = "Muokattiin merkintää " + description
							+ ".";
					updateStatus(message);
					panel.getNote().remove();
					editDialog.dispose();
				}
				if (currentSearch != null) {
					setNoteTableContent(currentSearch[0], currentSearch[1],
							currentSearch[2]);
				} else {
					searchAccount();
				}
			} catch (ParseException ex) {
				updateStatus("Virheellinen päivämäärä");
				return;
			} catch (NumberFormatException ex) {
				updateStatus("Virheellinen summa");
				return;
			} catch (NullPointerException ex) {
				updateStatus("Virheellinen tili");
				return;
			}
		} else if (e.getSource() == searchAccountButton) {

			searchAccount();
		} else if (e.getSource() instanceof JComboBox) {
			JComboBox box = (JComboBox) e.getSource();
			String selection = (String) box.getSelectedItem();
			if(selection != null){
				selection = selection.trim();
			}
			if (!accountTree.getAccounts().containsKey(selection)) {
				box.getEditor().getEditorComponent().setForeground(errorColor);
			} else {
				box.getEditor().getEditorComponent().setForeground(fontColor);
			}

		} else if (e.getSource() == editNoteItem) {

			NoteTableModel model = (NoteTableModel) noteTable.getModel();
			int row = noteTable.getSelectedRow();
			if (row < 0) {
				updateStatus("Muokattavaa merkintää ei ole valittu.");
			} else {
				Note selectedNote = model.getNoteAt(row);
				createEditingWindow(selectedNote);

			}
		} else if (e.getSource() == deleteNoteItem) {

			NoteTableModel model = (NoteTableModel) noteTable.getModel();
			int row = noteTable.getSelectedRow();
			if (row < 0) {
				updateStatus("Poistettavaa merkintää ei ole valittu.");
			} else {
				Note selectedNote = model.getNoteAt(row);
				if (approveDeletion()) {
					selectedNote.remove();
					String message = "Merkintä \""
							+ selectedNote.getDescription() + "\" poistettiin.";
					askSave = true;
					if (currentSearch != null) {
						setNoteTableContent(currentSearch[0], currentSearch[1],
								currentSearch[2]);
					} else {
						searchAccount();
					}
					updateStatus(message);
				} else {
					String message = "Merkinnän \""
							+ selectedNote.getDescription()
							+ "\" poistaminen peruttiin.";
					updateStatus(message);
				}
			}
		} else if (e.getSource() == renameAccountItem) {
			Account selected = (Account) accountManagerTree.getSelectionPath()
					.getLastPathComponent();
			if (selected != null) {
				String newName = (String) JOptionPane.showInputDialog(
						accountManagerTree, String.format(
								"Nimeä tili %s uudelleen", selected.getName()),
						"Nimeä uudelleen", JOptionPane.INFORMATION_MESSAGE,
						null, null, selected.getName());
				if (newName != null) {
					String oldName = selected.getName();
					updateStatus(String.format(
							"Tili %s nimettiin uudelleen tiliksi %s.", selected
									.getName(), newName));
					askSave = true;
					selected.setName(newName);
					accountTree.updateAccountName(oldName, selected);
					updateComboBoxValues(oldName, newName);
				} else {
					updateStatus(String.format(
							"Tilin %s uudelleen nimeäminen peruttiin.",
							selected.getName()));
				}
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == changeDescriptionAccountItem) {
			Account selected = (Account) accountManagerTree.getSelectionPath()
					.getLastPathComponent();
			if (selected != null) {
				String newDescr = (String) JOptionPane.showInputDialog(
						accountManagerTree, String.format(
								"Anna tilille %s uusi kuvaus", selected
										.getName()), "Muuta kuvausta",
						JOptionPane.INFORMATION_MESSAGE, null, null, selected
								.getDescription());
				if (newDescr != null) {
					selected.setDescription(newDescr);
					updateStatus(String.format(
							"Tilin %s kuvaukseksi asetettiin %s.", selected
									.getName(), selected.getDescription()));
					askSave = true;
				} else {
					updateStatus(String.format(
							"Tilin %s kuvauksen muokkaaminen peruttiin.",
							selected.getName()));
				}
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == addChildAccountItem) {
			if (accountManagerTree.getSelectionPath() != null) {
				createAddAccountWindow(true);
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}

		} else if (e.getSource() == moveAccountItem) {
			if(accountManagerTree.getSelectionPath() != null) {
				Account selected = (Account) accountManagerTree.getSelectionPath()
						.getLastPathComponent();
				createMoveAccountWindow(selected);
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == accountCancelButton) {
			((JDialog) ((AddAccountPanel) ((JButton) e.getSource()).getParent())
					.getTopLevelAncestor()).dispose();
		} else if (e.getSource() == accountOkButton) {
			Account account = ((AddAccountPanel) ((JButton) e.getSource())
					.getParent()).getAccount();

			if (((AddAccountPanel) ((JButton) e.getSource()).getParent())
					.addAsChild()) {
				Account parent = (Account) accountManagerTree
						.getSelectionPath().getLastPathComponent();
				parent.addChild(account);
				
			} else {
				Account child = (Account) accountManagerTree.getSelectionPath()
						.getLastPathComponent();
				Account parent = child.getParent();
				account.addChild(child);
				
				if (parent != null) {
					parent.addChild(account);
				} else {
					accountTree.setRoot(account);
				}
			}
			accountTree.addAccount(account);
			updateStatus(String.format("Lisättiin tili %s.", account.getName()));
			askSave = true;
			updateComboBoxValues(null, account.getName());
			accountManagerTree.setModel(new AccountModel(accountTree.getRoot()));
			accountManagerTree.expandAll();
			accountManagerTree.updateUI();
			((JDialog) ((AddAccountPanel) ((JButton) e.getSource()).getParent())
					.getTopLevelAncestor()).dispose();
			if (accountScrollPane != null) {
				AccountTree tree = (AccountTree) accountScrollPane
						.getViewport().getView();
				tree.expandAll();
				tree.updateUI();
			}
		} else if (e.getSource() == addAccountInPlaceItem) {
			if (accountManagerTree.getSelectionPath() != null) {
				createAddAccountWindow(false);
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == removeAccountNotRecursiveItem) {
			if (accountManagerTree.getSelectionPath() != null) {
				Account deletable = (Account) accountManagerTree
						.getSelectionPath().getLastPathComponent();
				if (accountTree.removeAccount(deletable, false)) {
					updateStatus(String.format("Tili %s poistettiin.",
							deletable));
					askSave = true;
					updateComboBoxValues(deletable.getName(), null);
					accountManagerTree.setModel(new AccountModel(accountTree.getRoot()));
					accountManagerTree.expandAll();
					accountManagerTree.updateUI();
					if (noteTable != null) {
						searchAccount();
					}

					if (accountScrollPane != null) {
						AccountTree tree = (AccountTree) accountScrollPane
								.getViewport().getView();
						tree.expandAll();
						tree.updateUI();
					}
				} else {
					updateStatus("Et voi poistaa juuritiliä.");
				}
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == removeAccountRecursiveItem) {
			if (accountManagerTree.getSelectionPath() != null) {
				Account deletable = (Account) accountManagerTree
						.getSelectionPath().getLastPathComponent();
				removeComboBoxNames(deletable);
				if (accountTree.removeAccount(deletable, true)) {
					updateComboBoxValues(deletable.getName(), null);
					updateStatus(String.format(
							"Tili %s alatileineen poistettiin.", deletable));
					askSave = true;
				} else {
					updateStatus("Juuritiliä ei voida poistaa. Sen alatilit on poistettu.");
				}
				
				if (noteTable != null) {
					searchAccount();
				}
				accountManagerTree.setModel(new AccountModel(accountTree.getRoot()));
				accountManagerTree.expandAll();
				accountManagerTree.updateUI();
				if (accountScrollPane != null) {
					AccountTree tree = (AccountTree) accountScrollPane
							.getViewport().getView();
					tree.expandAll();
					tree.updateUI();
				}
			} else {
				updateStatus("Yhtään tiliä ei ole valittu.");
			}
		} else if (e.getSource() == addNotes) {
			addNotesFromFile();
		} else if (e.getSource() == addNotesFromTable) {
			addNotesFromTable();
		}
	}

	private void addNotesFromFile() {
		String initialPath = cache.getProperty("openFilePath");
		if(initialPath == null || Files.notExists(Paths.get(initialPath))) {
			initialPath = ".";
		}
		JFileChooser chooser = new JFileChooser(initialPath);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			ExcelNoteReader reader = new ExcelNoteReader(file.getAbsolutePath(), this.accountTree);
			ArrayList<NoteHolder> notes = reader.readNotes();
			updateStatus(reader.printErrors());
			if(reader.getErrorCount() == 0) {
				for (NoteHolder noteData : notes) {
					new Note(noteData.getValue(), noteData.getDate(), noteData.getDescription(),noteData.getDebet(), noteData.getCredit());
				}
			}
		}
		
	}
	
	private void addNotesFromTable() {
		String initialPath = cache.getProperty("openFilePath");
		if(initialPath == null || Files.notExists(Paths.get(initialPath))) {
			initialPath = ".";
		}
		JFileChooser chooser = new JFileChooser(initialPath);
		chooser.setDialogTitle("Valitse tiedosto, josta merkinnät lisätään.");
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			
			TableNoteReader reader = new TableNoteReader(file);
			ArrayList<NoteHolder> notes = reader.readNotes();
			updateStatus(reader.printErrors());
			if(reader.getErrorCount() == 0) {
				updateStatus(String.format("Onnistuneesti luettu %d merkintää tiedostosta %s.",notes.size(),file.getAbsolutePath()));
			}
			
			createAddNotesFromTableDialog(notes);
			if (currentSearch != null) {
				setNoteTableContent(currentSearch[0], currentSearch[1],
						currentSearch[2]);
			} else {
				searchAccount();
			}
			askSave = true;
			
		}
	}

	private void createAddNotesFromTableDialog(ArrayList<NoteHolder> notes) {
		addNotesFromTableDialog = new JDialog(this, "Lisää merkinnät taulukosta");
		addNotesFromTableDialog.setModal(true);
		
		CreateNoteFromTablePanel contentPane = new CreateNoteFromTablePanel(accountTree, notes,this);
		addNotesFromTableDialog.setForeground(fontColor);
		addNotesFromTableDialog.setFont(font);

		addNotesFromTableDialog.setContentPane(contentPane);
		addNotesFromTableDialog.setLocationRelativeTo(this);
		addNotesFromTableDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addNotesFromTableDialog.pack();
		addNotesFromTableDialog.setVisible(true);
		
	}


	private void save() {
		if(saveName == null) {
			saveAs();
		} else {
			if (accountTree != null) {
				performSaving(saveName);
			} else {
				updateStatus("Ei tallennettavaa kirjanpitoa.");
			}
		}
		
	}
	
	public void performSaving(String saveName) {

		String extension = getExtension(saveName);
		int extLength = 0;
		if(extension != null) {
			 extLength = extension.length();
		}
		String graphSaveName = null;
		//TODO bad naming system. Can't find the graph file if saveName ends with .grp
		if(!saveName.toLowerCase().endsWith(".grp")) {
			String subStr = saveName.substring(0, saveName.length()-extLength);
			if(subStr.endsWith(".")) {
				graphSaveName = subStr + "grp";
			} else {
				graphSaveName = subStr + ".grp";
			}
		} else {
			//graphSaveName = saveName + ".grp";
			updateStatus("Valittu nimi ei kelpaa.");
			return;
		}
		ArrayList<GraphState> states = getGraphStates();
		//AccountMapWriter writer = new AccountMapWriter(saveName, accountTree);
		AccountMapWriter writer = new AccountMapXMLWriter(saveName, accountTree);
		writer.writeMap();
		GraphWriter gwrt = new GraphWriter(graphSaveName, states);
		gwrt.writeState();
		if (writer.getErrorCount() != 0 && gwrt.getErrorCount() != 0) {
			updateStatus(writer.printsErrors());
			updateStatus(gwrt.printsErrors());
        } else {
            updateStatus("Kirjanpito tallennettu tiedostoon " + saveName
                    + ".\n" + "Kuvaajat tallennettu tiedostoon " + graphSaveName +".\n");
            askSave = false;
        }
	}
	
	
    public String getExtension(File f) {
    	String s = f.getName();
    	return getExtension(s);
    }
    
    public String getExtension(String fileName) {
    	String ext = null;
        
        int i = fileName.lastIndexOf('.');

        if (i > 0 &&  i < fileName.length() - 1) {
            ext = fileName.substring(i+1).toLowerCase();
        } else ext = null;
        return ext;
    }

	private void saveAs() {
		if(accountTree != null) {
			int input = fc.showSaveDialog(this);
			if(input == JFileChooser.APPROVE_OPTION) {
				File savedFile = fc.getSelectedFile();
				String extension = getExtension(savedFile);
				if (extension != null) {
					saveName = savedFile.getAbsolutePath();	
				} else {
					String nameCandidate = savedFile.getAbsolutePath() + ".acc";
					//boolean success = savedFile.renameTo(new File(nameCandidate));
					savedFile = new File(nameCandidate);
					saveName = nameCandidate;
					/* if (success) {
						saveName = nameCandidate;
					} else {
						updateStatus("Annettua tiedostonimeä ei voida käyttää.");
						return;
					}
					*/
				}
            //String input = (String) JOptionPane.showInputDialog(this,
            //        "Anna tiedoston nimi", "Tallenna nimellä",
            //        JOptionPane.PLAIN_MESSAGE, null, null, null);
			/*if (input != null) {
                {
                    if (input.trim().equals("")) {
                        saveName = "default.acc";
                    } else if (!input.endsWith(".acc")) {
                        saveName = input + ".acc";
                    } else {
                    	saveName = input;
                    }
                } */
                performSaving(saveName);
			}
        } else {
            updateStatus("Ei tallennettavaa kirjanpitoa.\n");
        }
		
	}
	
	private ArrayList<GraphState> getGraphStates() {
		ArrayList<GraphState> states = new ArrayList<GraphState>();
		for(GraphPanel panel : graphPanels) {
			GraphState state = panel.collectGraphInformation();
			states.add(state);
		}
		return states;
	}

	private void newFile() {
		accountTree = new AccountMap();
		accountTree.setRoot(new Account("Juuri", "Juuritili"));
		initializeNoteBook();
		initializeStatistics(null);
	}

	private void openFile(String fileName, String saveName) {
		//coreClasses.AccountMapReader reader = new coreClasses.AccountMapReader(saveName);
		coreClasses.AccountMapReader reader = new coreClasses.AccountMapXMLReader(saveName);
		
		if (accountTree != null) {
			AccountingGUI gui = new AccountingGUI();
			gui.openFile(fileName, saveName);
		} else {
			System.out.println("Opening!");
			accountTree = reader.readMap();
			updateStatus(reader.printErrors());
			if (accountTree == null) {
				return;
			}
			this.saveName = saveName;
			this.fileName = fileName;
			initializeNoteBook();
			String extension = getExtension(saveName);
			int extLength = 0;
			if(extension != null) {
				extLength = extension.length();
			}
				
			String graphSaveName = null;
			//TODO bad naming system. Can't find the graph file if saveName ends with .grp
			if(!saveName.toLowerCase().endsWith(".grp")) {
				String subStr = saveName.substring(0, saveName.length()-extLength);
				if(subStr.endsWith(".")) {
					graphSaveName = subStr + "grp";
				} else {
					graphSaveName = subStr + ".grp";
				}
			} else {
				//graphSaveName = saveName + ".grp";
				updateStatus("Kirjanpitoa vastaavia graafeja ei löytynyt");
				return;
			}
			coreClasses.GraphReader grReader = new GraphReader(graphSaveName, getAccountMap());
			ArrayList<GraphState> stateList = grReader.readGraph();
			if(stateList != null) {
				initializeStatistics(stateList);
			} else {
				initializeStatistics(null);
			}
		}
		
	}
	
	/**
	 * Creates GraphPanels according to stateList and adds them to statistics. If There is only one GraphPanel, it is removed and the state is replaced. 
	 * @param stateList List of GraphStates to be added
	 */

	private void setGraphs(ArrayList<GraphState> stateList) {
		if(graphPanels.size() == 1) {
			GraphPanel deletable = graphPanels.get(0);
			for(GraphState state : stateList) {
				addGraphPanel(state);
			}
			removeGraphPanel(deletable);
		}
	}

	private void removeComboBoxNames(Account deletable) {
		for (Account child : deletable.getChildren()) {
			updateComboBoxValues(child.getName(), null);
			removeComboBoxNames(child);
		}
	}

	private void updateComboBoxValues(String oldName, String newName) {
		if (oldName != null) {
			List dataList = comboboxUpdateList.get(0).getDataList();
			dataList.remove(oldName);
			for(Java2sAutoComboBox box : comboboxUpdateList) {
				Object item = box.getSelectedItem();
				Collections.sort(dataList);
				box.setDataList(dataList);
				if(dataList.contains(item)) {
					box.setSelectedItem(item);
				}
			}
			/*List dataList = accountChooser.getDataList();
			dataList.remove(oldName);
			accountChooser.setDataList(dataList);
			addNotePanel.getDebetAccountCombobox().setDataList(dataList);
			addNotePanel.getCreditAccountCombobox().setDataList(dataList);
			*/
			//They all have the same editor...
			//addNotePanel.getDebetAccountCombobox().removeItem(oldName);
			//addNotePanel.getCreditAccountCombobox().removeItem(oldName);
		}
		if (newName != null) {
			List dataList = comboboxUpdateList.get(0).getDataList();
			dataList.add(newName);
			for(Java2sAutoComboBox box : comboboxUpdateList) {
				Object item = box.getSelectedItem();
				box.setDataList(dataList);
				box.setSelectedItem(item);
			}
			//They all have the same editor...
			//addNotePanel.getDebetAccountCombobox().addItem(newName);
			//addNotePanel.getCreditAccountCombobox().addItem(newName);
			
		}

	}
	
	private void createEditingWindow(Note note) {
		editDialog = new JDialog(this, "Muokkaa merkintää");
		editDialog.setModal(true);
		AddNotePanel contentPane = new AddNotePanel(accountTree, note);
		editNoteButton = new JButton("Muokkaa");
		editNoteButton.setForeground(fontColor);
		editNoteButton.setFont(font);
		editNoteButton.addActionListener(this);
		
		Action accept = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		       editNoteButton.doClick();
		    }
		};
		
		InputMap im = contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		contentPane.getActionMap().put("pressed",accept);
		
		JTextField editor = (JTextField)contentPane.getDebetAccountCombobox().getEditor().getEditorComponent();
		editor.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editor.getActionMap().put("pressed",accept);
		
		editor = (JTextField)contentPane.getCreditAccountCombobox().getEditor().getEditorComponent();
		editor.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editor.getActionMap().put("pressed",accept);

		/*
		editNoteButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editNoteButton.getActionMap().put("pressed",
                accept);
	   */
		contentPane.addButton(editNoteButton);
		editDialog.setContentPane(contentPane);
		editDialog.setLocationRelativeTo(this);
		editDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		editDialog.pack();
		editDialog.setVisible(true);
	}

	private void createAddAccountWindow(boolean addAsChild) {
		AddAccountPanel addAccount = new AddAccountPanel(addAsChild);
		accountOkButton = new JButton("OK");
		accountOkButton.addActionListener(this);
		accountCancelButton = new JButton("Cancel");
		accountCancelButton.addActionListener(this);
		addAccount.addButton(accountOkButton);
		addAccount.addButton(accountCancelButton);
		JDialog dialog = new JDialog(this);
		dialog.setTitle("Lisää uusi tili");
		dialog.setContentPane(addAccount);
		dialog.setLocationRelativeTo(accountManagerTree);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	private void createMoveAccountWindow(Account sourceAccount) {
		MoveAccountPanel moveAccount = new MoveAccountPanel(sourceAccount,accountManagerTree,accountTree,this);
		JDialog dialog = new JDialog(this);
		dialog.setTitle("Siirrä tili " + sourceAccount.getName());
		dialog.setContentPane(moveAccount);
		dialog.setLocationRelativeTo(accountManagerTree);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}

	private boolean approveDeletion() {
		return true;
		// TODO tee popup
	}
	
	/**
	 *  Writes the given message to the status window. This should be the primary method of communicating info to the user.
	 *  TODO: write the message also to a log file. 
	 * @param message to be written.
	 */

	public void updateStatus(String message) {
		String dateString = messageWindowDateFormat.format(new Date());
		String fullMessage = dateString + ": " + message;
		if (message.endsWith("\n")) {
			statusTextArea.append(fullMessage);
		} else {
			statusTextArea.append(fullMessage + "\n");
		}
	}

	private JPanel createNoteBook() {
		noteBookContainer = new JPanel();
		noteBookContainer.setOpaque(false);
		noteBookContainer.setLayout(new GridBagLayout());
		noteBookConstraints = new GridBagConstraints(0, 0, 2, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3,
						4, 3, 4), 0, 0);
		noteBookConstraints.fill = GridBagConstraints.BOTH;
		noteBookContainer.add(createAddNotePanel(), noteBookConstraints);
		noteBookConstraints.gridy++;
		noteBookContainer.add(createAccountChooser(), noteBookConstraints);
		noteBookConstraints.gridwidth = 1;
		noteBookConstraints.gridy++;
		return noteBookContainer;
	}

	private void searchAccount() {

		String startDateText = dateStartField.getText();
		String endDateText = dateEndField.getText();
		String account = (String) accountChooser.getSelectedItem();
		currentSearch = new String[] { startDateText, endDateText, account };
		setNoteTableContent(startDateText, endDateText, account);
	}

	private void setNoteTableContent(String startDateText, String endDateText,
			String accountName) {

		Date start = null;
		try {
			if (startDateText.trim().equals("")) {
				start = null;
			} else {
				start = dateFormat.parse(startDateText);
			}
		} catch (ParseException err) {
			updateStatus("Huono päivämäärä!");
			return;
		}
		Date end = null;
		try {
			if (endDateText.trim().equals("")) {
				end = null;
			} else {
				end = dateFormat.parse(endDateText);
			}
		} catch (ParseException err) {
			updateStatus("Huono päivämäärä!");
			return;
		}
		if (!accountTree.getAccounts().containsKey(accountName)) {

			// Nämä tehdään jo toisaalla
			// String message = "No account called " + account + ".";
			// updateStatus(message);
			return;
		}

		Account account = accountTree.getAccount(accountName);

		if (start != null && end != null && start.compareTo(end) > 0) {
			Date temp = start;
			start = end;
			end = temp;
			updateStatus("Päivämäärät väärin päin.");
		}
		// Notes in reversed order, so end and start are reversed too.
		SortedList<Note> notes = accountTree.getNotesInRange(account, end,
				start);
		if (noteTable != null) {
			noteTable.setModel(new NoteTableModel(notes, dateFormat));
			AccountTree tree = (AccountTree) accountScrollPane.getViewport()
					.getView();
			tree.setModel(new AccountModel(account));
			tree.expandAll();
		} else {
			noteBookConstraints.fill = GridBagConstraints.VERTICAL;
			noteBookConstraints.fill = GridBagConstraints.BOTH;
			lowerPanel = new JPanel();
			lowerPanel.setOpaque(false);
			accountScrollPane = createAccountScrollPane(account, false);

			GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1,
					0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0);

			lowerPanel.add(createNoteContainers(notes), constraints);
			constraints.gridx++;
			lowerPanel.add(accountScrollPane, constraints);
			resizeLowerPanel();
			noteBookContainer.add(lowerPanel, noteBookConstraints,
					noteBookConstraints.gridy);
			noteBookContainer.validate();
		}
		accountValueField.setText((String.format("%.2f EUR", account.getValue())));
		accountIntervalValueFIeld.setText((String.format("%.2f EUR",
				countValue(account, notes))));
	}

	public double countValue(Account enquirer, SortedList<Note> notes) {
		double value = 0.0;
		for (Note note : notes) {
			value += note.getSignedValue(enquirer);
		}
		return value;
	}

	@SuppressWarnings("serial")
	private AddNotePanel createAddNotePanel() {
		addNotePanel = new AddNotePanel(accountTree, null);
		addNotePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(lineColor), "Lisää merkintä",
				TitledBorder.LEFT, TitledBorder.TOP, boldFont, fontColor));
		addComboboxToUpdateList(addNotePanel.getDebetAccountCombobox());
		addComboboxToUpdateList(addNotePanel.getCreditAccountCombobox());
		addNoteButton = new JButton("Lisää");
		addNoteButton.setOpaque(false);
		addNoteButton.setForeground(fontColor);
		addNoteButton.addActionListener(this);
		
		Action accept = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		       addNoteButton.doClick();
		    }
		};
		
		InputMap im = addNotePanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		addNotePanel.getActionMap().put("pressed",accept);
		
		JTextField editor = (JTextField)addNotePanel.getDebetAccountCombobox().getEditor().getEditorComponent();
		editor.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editor.getActionMap().put("pressed",accept);
		
		editor = (JTextField)addNotePanel.getCreditAccountCombobox().getEditor().getEditorComponent();
		editor.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editor.getActionMap().put("pressed",accept);
			
		/*
		addNoteButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		addNoteButton.getActionMap().put("pressed",
                accept);
		*/
		
		addNotePanel.addButton(addNoteButton);
		/*
		 * addNotePanel = new JPanel();
		 * addNotePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
		 * .createLineBorder(lineColor), "Lisää merkintä", TitledBorder.LEFT,
		 * TitledBorder.TOP, boldFont, fontColor));
		 * addNotePanel.setOpaque(false); addNotePanel.setLayout(new
		 * GridBagLayout()); JLabel dateLabel = new JLabel("Päivämäärä");
		 * dateLabel.setFont(font); dateLabel.setOpaque(false);
		 * dateLabel.setForeground(fontColor); dateField = new
		 * JFormattedTextField(createMaskFormatter());
		 * dateField.setOpaque(true); dateField.setColumns(5);
		 * dateField.setForeground(fontColor); dateField.addFocusListener(this);
		 * JLabel sumLabel = new JLabel("Summa"); sumLabel.setFont(font);
		 * sumLabel.setOpaque(false); sumLabel.setForeground(fontColor);
		 * sumField = new JTextField(); sumField.setOpaque(true);
		 * sumField.setColumns(5); sumField.setForeground(fontColor);
		 * sumField.addFocusListener(this); JLabel depetAccountLabel = new
		 * JLabel("Debet-tili"); depetAccountLabel.setFont(font);
		 * depetAccountLabel.setOpaque(false);
		 * depetAccountLabel.setForeground(fontColor);
		 * 
		 * depetAccountComboBox =
		 * createComboBox(accountTree.getAccountNames(true));
		 * 
		 * JLabel creditAccountLabel = new JLabel("Credit-tili");
		 * creditAccountLabel.setFont(font);
		 * creditAccountLabel.setOpaque(false);
		 * creditAccountLabel.setForeground(fontColor);
		 * 
		 * creditAccountComboBox =
		 * createComboBox(accountTree.getAccountNames(true));
		 * 
		 * addNotePanel.setLayout(new GridBagLayout());
		 * 
		 * JLabel descriptionLabel = new JLabel("Kuvaus");
		 * descriptionLabel.setFont(font); descriptionLabel.setOpaque(false);
		 * descriptionLabel.setForeground(fontColor); descriptionField = new
		 * JTextField(); descriptionField.setOpaque(true);
		 * descriptionField.setColumns(12);
		 * descriptionField.setForeground(fontColor);
		 * 
		 * addNoteButton = new JButton("Lisää"); addNoteButton.setOpaque(false);
		 * addNoteButton.setForeground(fontColor);
		 * addNoteButton.addActionListener(this);
		 * 
		 * GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1,
		 * 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6,
		 * 4, 6, 4), 0, 0); addNotePanel.add(dateLabel, constraints);
		 * constraints.gridx++; addNotePanel.add(descriptionLabel, constraints);
		 * constraints.gridx++; addNotePanel.add(sumLabel, constraints);
		 * constraints.gridx++; addNotePanel.add(depetAccountLabel,
		 * constraints); constraints.gridx++;
		 * addNotePanel.add(creditAccountLabel, constraints); constraints.gridx
		 * = 0; constraints.gridy = 2; addNotePanel.add(dateField, constraints);
		 * constraints.gridx++; addNotePanel.add(descriptionField, constraints);
		 * constraints.gridx++; addNotePanel.add(sumField, constraints);
		 * constraints.gridx++; addNotePanel.add(depetAccountComboBox,
		 * constraints); constraints.gridx++;
		 * addNotePanel.add(creditAccountComboBox, constraints);
		 * constraints.gridx++; addNotePanel.add(addNoteButton, constraints);
		 */
		return addNotePanel;
	}

	public JComboBox createComboBox(String[] names) {
		//TODO turha??
		JComboBox box = new JComboBox(names);
		box.setEditable(true);
		box.addActionListener(this);
		return box;
	}

	/*
	 * private String[] createAccountArray(Account root, boolean all) {
	 * ArrayList<Account> children; if(all) { children = root.getChildren(); }
	 * else { children = root.getChildrenWithNoChilds(); } children.add(root);
	 * String[] childrenNames = new String[children.size()]; int i = 0;
	 * for(Account child : children) { childrenNames[i] = child.getName(); i++;
	 * } return childrenNames; }
	 */
	/*
	 * private JPanel createNoteEditingButtonPanel() { JPanel buttonPanel = new
	 * JPanel(); buttonPanel.setOpaque(false);
	 * buttonPanel.setBorder(BorderFactory.createLineBorder(lineColor));
	 * buttonPanel.setLayout(new GridBagLayout());
	 * 
	 * editNoteButton = new JButton(); editNoteButton.setText("Edit");
	 * editNoteButton.setForeground(fontColor); editNoteButton.setFont(font);
	 * editNoteButton.addActionListener(this);
	 * 
	 * removeNoteButton = new JButton(); removeNoteButton.setText("Remove");
	 * removeNoteButton.setForeground(fontColor);
	 * removeNoteButton.setFont(font); removeNoteButton.addActionListener(this);
	 * 
	 * buttonPanel.add(editNoteButton); buttonPanel.add(removeNoteButton);
	 * 
	 * return buttonPanel; }
	 */
	private JPanel createAccountChooser() {
		accountChooserPanel = new JPanel();
		accountChooserPanel.setOpaque(false);
		accountChooserPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(lineColor),
				"Näytä tapahtumahistoria", TitledBorder.LEFT, TitledBorder.TOP,
				boldFont, fontColor));
		accountChooserPanel.setLayout(new GridBagLayout());
		JLabel nameLabel = new JLabel("Tili");
		nameLabel.setFont(font);
		nameLabel.setOpaque(false);
		nameLabel.setForeground(fontColor);

		accountChooser = new Java2sAutoComboBox(accountTree.getAccountNames(false));
		addComboboxToUpdateList(accountChooser);
		accountChooser.setForeground(fontColor);
		accountChooser.setFont(font);
		accountChooser.addActionListener(this);

		JLabel dateLabel = new JLabel("Ajalta");
		dateLabel.setFont(font);
		dateLabel.setOpaque(false);
		dateLabel.setForeground(fontColor);

		MaskFormatter mask = createMaskFormatter();
		dateStartField = new JFormattedTextField(mask);
		dateStartField.setOpaque(true);
		dateStartField.setForeground(fontColor);
		dateStartField.setFont(font);
		dateStartField.setColumns(7);
		dateStartField.addFocusListener(this);

		dateEndField = new JFormattedTextField(mask);
		dateEndField.setOpaque(true);
		dateEndField.setForeground(fontColor);
		dateEndField.setFont(font);
		dateEndField.setColumns(7);
		dateEndField.addFocusListener(this);

		searchAccountButton = new JButton("Hae");
		searchAccountButton.setForeground(fontColor);
		searchAccountButton.setFont(boldFont);
		searchAccountButton.addActionListener(this);
		searchAccountButton.setFont(font);

		accountValueField = new JTextField();
		accountValueField.setOpaque(false);
		accountValueField.setForeground(fontColor);
		accountValueField.setFont(boldFont);
		accountValueField.setEnabled(false);
		accountValueField.setBorder(BorderFactory.createEmptyBorder());
		accountValueField.setDisabledTextColor(fontColor);

		accountIntervalValueFIeld = new JTextField();
		accountIntervalValueFIeld.setOpaque(false);
		accountIntervalValueFIeld.setForeground(fontColor);
		accountIntervalValueFIeld.setFont(boldFont);
		accountIntervalValueFIeld.setEnabled(false);
		accountIntervalValueFIeld.setBorder(BorderFactory.createEmptyBorder());
		accountIntervalValueFIeld.setDisabledTextColor(fontColor);

		GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(6, 4, 6, 4), 0, 0);

		accountChooserPanel.add(nameLabel, constraints);
		constraints.gridx++;
		accountChooserPanel.add(dateLabel, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		accountChooserPanel.add(accountChooser, constraints);
		constraints.gridx++;
		accountChooserPanel.add(dateStartField, constraints);
		constraints.gridx++;
		accountChooserPanel.add(dateEndField, constraints);
		constraints.gridx++;
		accountChooserPanel.add(searchAccountButton, constraints);
		constraints.gridx++;
		accountChooserPanel.add(accountValueField, constraints);
		constraints.gridx++;
		accountChooserPanel.add(accountIntervalValueFIeld, constraints);
		constraints.weightx = 1;
		constraints.gridx++;
		JPanel fillPanel = new JPanel();
		fillPanel.setOpaque(false);
		accountChooserPanel.add(fillPanel, constraints);
		return accountChooserPanel;
	}

	public static MaskFormatter createMaskFormatter() {
		MaskFormatter formatter = null;
		SimpleDateFormat form = (SimpleDateFormat) dateFormat;
		String mask = "";
		int lenght = form.toPattern().length();
		for (int i = 0; i < lenght; i++) {
			mask = mask.concat("*");
		}

		try {
			formatter = new MaskFormatter(mask);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter;
	}

	/*
	 * private JScrollPane createNoteContainers(SortedList<Note> notes) {
	 * notePanel = new JPanel(); notePanel.setOpaque(false);
	 * 
	 * notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.Y_AXIS)); if
	 * (notes.size() == 0) {
	 * 
	 * JTextField noNotes = new JTextField("Ei merkintäjä");
	 * noNotes.setFont(boldFont); noNotes.setForeground(fontColor);
	 * noNotes.setBorder(BorderFactory.createEmptyBorder());
	 * noNotes.setEditable(false); notePanel.add(noNotes); } for (Note note :
	 * notes) { JFormattedTextField dateField = new JFormattedTextField();
	 * dateField.setEditable(false); dateField.setForeground(fontColor);
	 * dateField.setFont(font); dateField.setValue((String)
	 * dateFormat.format(note.getDate()));
	 * dateField.setBorder(BorderFactory.createEmptyBorder());
	 * 
	 * JTextField description = new JTextField();
	 * description.setEditable(false); description.setForeground(fontColor);
	 * description.setFont(font); description.setText(note.getDescription());
	 * description.setBorder(BorderFactory.createEmptyBorder());
	 * 
	 * JTextField value = new JTextField(); value.setEditable(false);
	 * value.setForeground(fontColor); value.setFont(font); value.setText(new
	 * Double(note.getValue()).toString());
	 * value.setBorder(BorderFactory.createEmptyBorder());
	 * 
	 * JComboBox debet = createComboBox(accounTree.getAccountNames(true));
	 * debet.setSelectedItem(note.getDebet().getName());
	 * debet.setEditable(false); debet.setFont(font);
	 * debet.setForeground(fontColor);
	 * 
	 * JComboBox credit = createComboBox(accounTree.getAccountNames(true));
	 * credit.setEditable(false); credit.setFont(font);
	 * credit.setForeground(fontColor);
	 * credit.setSelectedItem(note.getCredit().getName());
	 * 
	 * NoteContainer cont = new NoteContainer(note, dateField, description,
	 * credit, debet, value); cont.addMouseListener(this); notePanel.add(cont);
	 * } scrollPane = new JScrollPane(notePanel); scrollPane.setOpaque(false);
	 * scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	 * resizeScrollPane(); return scrollPane; }
	 */

	private void createEditNoteMenu() {
		editMenu = new JPopupMenu();
		editNoteItem = new JMenuItem("muokkaa");
		editNoteItem.addActionListener(this);
		editNoteItem.setForeground(fontColor);
		deleteNoteItem = new JMenuItem("poista");
		deleteNoteItem.addActionListener(this);
		deleteNoteItem.setForeground(fontColor);
		editMenu.add(editNoteItem);
		editMenu.add(deleteNoteItem);
	}

	private void createEditAccountsMenu() {
		accountMenu = new JPopupMenu();
		renameAccountItem = new JMenuItem("Nimeä uudelleen");
		renameAccountItem.addActionListener(this);
		renameAccountItem.setForeground(fontColor);
		changeDescriptionAccountItem = new JMenuItem("Muuta kuvausta");
		changeDescriptionAccountItem.addActionListener(this);
		changeDescriptionAccountItem.setForeground(fontColor);
		addChildAccountItem = new JMenuItem("Lisää alatili");
		addChildAccountItem.addActionListener(this);
		addChildAccountItem.setForeground(fontColor);
		addAccountInPlaceItem = new JMenuItem("Lisää tili vanhan paikalle");
		addAccountInPlaceItem.addActionListener(this);
		addAccountInPlaceItem.setForeground(fontColor);
		removeAccountNotRecursiveItem = new JMenuItem("Poista tili");
		removeAccountNotRecursiveItem.addActionListener(this);
		removeAccountNotRecursiveItem.setForeground(fontColor);
		removeAccountRecursiveItem = new JMenuItem(
				"Poista tili sekä kaikki alitilit");
		removeAccountRecursiveItem.addActionListener(this);
		removeAccountRecursiveItem.setForeground(fontColor);
		moveAccountItem = new JMenuItem("Siirrä tili");
		moveAccountItem.addActionListener(this);
		moveAccountItem.setForeground(fontColor);

		accountMenu.add(renameAccountItem);
		accountMenu.add(changeDescriptionAccountItem);
		accountMenu.add(addChildAccountItem);
		accountMenu.add(addAccountInPlaceItem);
		accountMenu.add(removeAccountNotRecursiveItem);
		accountMenu.add(removeAccountRecursiveItem);
		accountMenu.add(moveAccountItem);
	}
	
	public void deleteGraph(Component removable) {
		statisticsMainPanel.remove(removable);
		statisticsMainPanel.validate();
		
	}

	private JScrollPane createNoteContainers(SortedList<Note> notes) {
		noteTable = new JTable(new NoteTableModel(notes, dateFormat));
		noteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(SwingConstants.RIGHT);
		TableColumnModel tcm = noteTable.getColumnModel();
		tcm.getColumn(2).setCellRenderer(r);
		
		//TODO alignment ei säily
		scrollPane = new JScrollPane(noteTable);
		noteTable.setFillsViewportHeight(true);
		createEditNoteMenu();
		int i = 0;
		TableColumn column = noteTable.getColumnModel().getColumn(i);
		column.setPreferredWidth(30);
		i++;
		column = noteTable.getColumnModel().getColumn(i);
		column.setPreferredWidth(200);
		i++;
		column = noteTable.getColumnModel().getColumn(i);
		column.setPreferredWidth(20);
		i++;
		column = noteTable.getColumnModel().getColumn(i);
		column.setPreferredWidth(30);
		i++;
		column = noteTable.getColumnModel().getColumn(i);
		column.setPreferredWidth(30);
		
		noteTable.addMouseListener(this);

		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		return scrollPane;
	}

	private void resizeLowerPanel() {
		double height = noteBook.getSize().getHeight()
				- accountChooserPanel.getSize().getHeight()
				- addNotePanel.getSize().getHeight() - 30;
		double width = (this.getSize().getWidth() - 10);
		// lowerPanel.setPreferredSize(new Dimension((int) width, (int)
		// height));
		lowerPanel.getComponent(0).setPreferredSize(
				new Dimension((int) (width * 0.7 - 10), (int) height));
		lowerPanel.getComponent(1).setPreferredSize(
				new Dimension((int) (width * 0.3 - 10), (int) height));
		lowerPanel.updateUI();
	}

	private void resizeManagerPanel() {
		double height = accountManager.getHeight() - 5;
		double width = this.getSize().getWidth() - 20;
		accountManagerScroll.setPreferredSize(new Dimension((int) width,
				(int) height));
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (accountTree != null && askSave) {
            int answer = JOptionPane
                    .showConfirmDialog(this,
                            "Haluatko tallentaa muutokset ennen sulkemista?", "",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                save();
                dispose();
            } else if (answer == JOptionPane.NO_OPTION) {
                dispose();
            } else if(answer == JOptionPane.CANCEL_OPTION) {
            	
            }
        } else {
            dispose();
        }
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// T

	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (lowerPanel != null) {
			resizeLowerPanel();
		}
		if (accountManagerScroll != null) {
			resizeManagerPanel();
		}

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			editMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void maybeShowAccountPopup(MouseEvent e) {
		if (e.isPopupTrigger() && accountManagerTree.getSelectionCount() > 0) {
			accountMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		/*
		 * if (e.getSource() instanceof NoteContainer) { NoteContainer cont =
		 * (NoteContainer) e.getSource(); cont.activate(); }
		 */
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == noteTable) { // Mouse is pressed at noteTable
			// Only create the popup if pressed at a row.
			int r = noteTable.rowAtPoint(e.getPoint());
	        if (r >= 0 && r < noteTable.getRowCount()) {
	            noteTable.setRowSelectionInterval(r, r); // Select the row
	        } else {
	            return; // Do nothing
	        	//table.clearSelection();
	        }
			
			maybeShowPopup(e);
		}
		if (e.getSource() == accountManagerTree) {
			maybeShowAccountPopup(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == noteTable) {
			maybeShowPopup(e);
		}
		if (e.getSource() == accountManagerTree) {
			maybeShowAccountPopup(e);
		}
	}

	public static Date parseDate(String dateString)
			throws IllegalDateException, NullPointerException {
		if (dateString.equals("")) {
			throw new NullPointerException("Empty string.");
		}
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String[] parts = dateString.split("\\.");
		if (parts.length == 3) {
			try {
				return dateFormat.parse(dateString);
			} catch (ParseException e) {
				throw new IllegalDateException("Date is illegal.");
			}
		}
		if (parts.length == 2) {
			String formattedDate = parts[0]
					+ "-"
					+ parts[1]
					+ "-"
					+ new Integer(Calendar.getInstance().get(Calendar.YEAR))
							.toString();
			try {
				return format.parse(formattedDate);
			} catch (ParseException e) {
				throw new IllegalDateException("Given date is too short.");
			}
		}
		if (parts.length == 1) {
			Calendar cal = Calendar.getInstance();
			String year = new Integer(cal.get(Calendar.YEAR)).toString();
			String month = new Integer(cal.get(Calendar.MONTH) + 1).toString();
			String formattedDate = parts[0] + "-" + month + "-" + year;
			try {
				return format.parse(formattedDate);
			} catch (ParseException e) {
				throw new IllegalDateException("Given date is too short.");
			}
		}
		return null;
	}

	@Override
	public void focusGained(FocusEvent e) {

	}

	@Override
	public void focusLost(FocusEvent ev) {
		if (!ev.isTemporary()) {

			if (ev.getSource().equals(sumField)
					|| ev.getSource().equals(editSumField)) {
				JTextField source = (JTextField) ev.getSource();
				String sumString = source.getText();
				try {
					double sum = Double.parseDouble(sumString);
					source.setForeground(fontColor);
					if (sum < 0) {
						source.setText(new Double(-sum).toString());
					}
				} catch (NumberFormatException e) {
					source.setForeground(errorColor);
				}
			}

			if (ev.getSource() instanceof JFormattedTextField) {
				JFormattedTextField source = (JFormattedTextField) ev
						.getSource();
				String newValue = source.getText().trim();
				try {
					source.setValue(dateFormat.format(parseDate(newValue)));
					source.setForeground(fontColor);
				} catch (NullPointerException e) {
					if (source.equals(dateField)) {

						// source.setValue("");
					}
				} catch (IllegalDateException e) {
					source.setForeground(errorColor);
				}
			}
		}

	}

	private JScrollPane createAccountScrollPane(Account root, boolean isEditable) {
		JScrollPane scroll = new JScrollPane();
		scroll.setOpaque(false);

		AccountModel model = new AccountModel(root);

		AccountTree tree = new AccountTree(model);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		tree.setCellRenderer(renderer);

		tree.expandAll();
		scroll.setViewportView(tree);
		return scroll;
	}
	
	private void initializeReports() {
		reports.setLayout(new BorderLayout());
		JPanel optionsPanel = new JPanel();
		optionsPanel.setOpaque(false);
		//Read list of existing reports
		templateSelector = new Java2sAutoComboBox(null);
		templateSelector.setForeground(fontColor);
		templateSelector.setFont(font);
		JButton importButton = new JButton("Tuo pohja");
		importButton.setForeground(fontColor);
		importButton.setFont(font);
		importButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {

				int returnVal = fc.showOpenDialog(((JButton)evt.getSource()).getParent());
				String fileName = null;
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            fileName = file.getPath();
		            Template temp = new Template(fileName);
		            importTemplate(temp);
		        } else {
		        	return;
		        }
			}
		});
		
	}
	
	public void importTemplate(Template temp) {
		
	}
	
	private void initializeStatistics(ArrayList<GraphState> states) {
		GridLayout gridLayout = new GridLayout(0,1);
		statistics.setLayout(gridLayout);
		statistics.setOpaque(false);
		statisticsMainPanel = new JPanel();
		JScrollPane statisticsMainScroll = new JScrollPane(statisticsMainPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		statisticsMainScroll.getVerticalScrollBar().setUnitIncrement(16);
		statisticsMainScroll.setOpaque(true);
		statistics.add(statisticsMainScroll);
		//statisticsMainPanel.setLayout(new GridLayout(0,1));
		GroupLayout grp = new GroupLayout(statisticsMainPanel);
		vertical = grp.createParallelGroup();
		horizontal = grp.createSequentialGroup();
		statisticsMainPanel.setLayout(grp);
		
		GraphReader defGraphReader = new GraphReader("default.grp", getAccountMap());
		ArrayList<GraphState> defList = defGraphReader.readGraph();
		if(defList != null && defList.size() > 0 ) {
			defaultGraphState = defList.get(0);
		} else {
			defaultGraphState = new GraphState(null, null, 1, StepType.MONTH, null);
		}
		
		if(states != null && states.size() > 0) {
			for(GraphState state : states) {
				GraphPanel panel = addGraphPanel(state);
				panel.fireReadyEvent();
			}
		
		} else {
			addGraphPanel();
		}
		
		statisticsMainPanel.setOpaque(false);
	}
	
	public GraphPanel addGraphPanel() {
		return addGraphPanel(defaultGraphState);
	}
	
	public GraphPanel addGraphPanel(GraphState state) {
		GraphPanel panel = new GraphPanel(this,state);
		graphPanels.add(panel);
		GroupLayout grp = (GroupLayout)statisticsMainPanel.getLayout();
		horizontal.addGroup(grp.createParallelGroup().addComponent(panel,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE));
		// The groups are set in with wrong methods, because they are constructed along wrong axes.
		grp.setHorizontalGroup(vertical);
		vertical.addComponent(panel,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE);
		grp.setVerticalGroup(horizontal);
		statistics.revalidate();
		return panel;
	}
	
	public void removeGraphPanel(GraphPanel graph) {
		if(graphPanels.size() != 1) {
			statisticsMainPanel.remove(graph);
			graphPanels.remove(graph);
			statistics.revalidate();
		} else {
			//TODO do something different like set the default graph state
		}
	}
	
	public GraphState getDefaultGraphState() {
		return defaultGraphState;
	}
	
	public AccountMap getAccountMap() {
		return accountTree;
	}
	/**
	 * Adds the combo box to the list of boxes whose names will be updated when a new account is created.
	 **/
	public void addComboboxToUpdateList(Java2sAutoComboBox box) {
		comboboxUpdateList.add(box);
	}
	
	/**
	 * Removes the combo box from the list of boxes whose names will be updated when a new account is created.
	 **/
	public boolean removeFromComboboxUpdateList(Java2sAutoComboBox box) {
		return comboboxUpdateList.remove(box);
	}
}
