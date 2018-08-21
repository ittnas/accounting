package userInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.InputMap;

import coreClasses.Account;
import coreClasses.AccountMap;
import coreClasses.Note;
import coreClasses.NoteHolder;

public class CreateNoteFromTablePanel extends JPanel implements ActionListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField dateField;
	private JTextField sumField;
	private Java2sAutoComboBox sourceAccountComboBox;
	private Java2sAutoComboBox targetAccountComboBox;
	private JTextField descriptionField;
	private JLabel numberLabel;
	private JTextField factorField;
	
	private AccountMap accountTree;
	private ArrayList<NoteHolder> notes;
	private AccountingGUI GUI;
	private GridBagConstraints constraints;
	private JButton addNoteButton;
	private JButton skipNoteButton;
	private JButton skipAllButton;
	private JButton addAllButton;
	private int currentNote = 1;

	public CreateNoteFromTablePanel(AccountMap accountTree, ArrayList<NoteHolder> notes,AccountingGUI GUI) {
		super();
		this.accountTree = accountTree;
		this.notes = notes;
		this.GUI = GUI;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		
		numberLabel = new JLabel("");
		numberLabel.setFont(AccountingGUI.font);
		numberLabel.setOpaque(false);
		numberLabel.setForeground(AccountingGUI.fontColor);
		numberLabel.setPreferredSize(new Dimension(300,20));
		setNoteNumber(numberLabel, 1, notes.size());
		
		JLabel dateLabel = new JLabel("Päivämäärä");
		dateLabel.setFont(AccountingGUI.font);
		dateLabel.setOpaque(false);
		dateLabel.setForeground(AccountingGUI.fontColor);
		dateField = new JFormattedTextField(AccountingGUI.createMaskFormatter());
		dateField.setOpaque(true);
		dateField.setColumns(7);
		dateField.setForeground(AccountingGUI.fontColor);
		dateField.addFocusListener(new DateFieldFocusListener());
		dateField.setFont(AccountingGUI.font);
		dateField.addFocusListener(this);
		JLabel sumLabel = new JLabel("Summa");
		sumLabel.setFont(AccountingGUI.font);
		sumLabel.setOpaque(false);
		sumLabel.setForeground(AccountingGUI.fontColor);
		sumField = new JTextField();
		sumField.setOpaque(true);
		sumField.setColumns(7);
		sumField.setForeground(AccountingGUI.fontColor);
		sumField.setFont(AccountingGUI.font);
		sumField.addFocusListener(new SumFieldFocusListener());
		sumField.addFocusListener(this);
		
		JLabel factorLabel = new JLabel("Kerroin");
		factorLabel.setFont(AccountingGUI.font);
		factorLabel.setOpaque(false);
		factorLabel.setForeground(AccountingGUI.fontColor);
		
		factorField = new JTextField();
		factorField.setOpaque(true);
		factorField.setColumns(4);
		factorField.setForeground(AccountingGUI.fontColor);
		factorField.setFont(AccountingGUI.font);
		factorField.addFocusListener(new SumFieldFocusListener());
		factorField.setText("1");
		
		JLabel sourceAccountLabel = new JLabel("Lähdetili");
		sourceAccountLabel.setFont(AccountingGUI.font);
		sourceAccountLabel.setOpaque(false);
		sourceAccountLabel.setForeground(AccountingGUI.fontColor);
		ArrayList<String> accountNames = accountTree.getAccountNames(false);
		
		FocusListener fl = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(e.getSource() == targetAccountComboBox.getEditor().getEditorComponent()) {
					targetAccountComboBox.getEditor().selectAll();
				}
				if(e.getSource() == sourceAccountComboBox.getEditor().getEditorComponent()) {
					sourceAccountComboBox.getEditor().selectAll();
				}
				/*
				ComboBoxEditor editor = ((JComboBox<Account>)e.getSource()).getEditor();
		        JTextField textField = (JTextField)editor.getEditorComponent();
		        textField.setCaretPosition(0);
		        */
			}
		};
		
		sourceAccountComboBox = new Java2sAutoComboBox(accountNames);
		sourceAccountComboBox.setEditable(true);
		sourceAccountComboBox.addActionListener(this);
		sourceAccountComboBox.setFont(AccountingGUI.font);
		sourceAccountComboBox.setForeground(AccountingGUI.fontColor);
		sourceAccountComboBox.getEditor().getEditorComponent().addFocusListener(fl);

		
		JLabel targetAccountLabel = new JLabel("Kohdetili");
		targetAccountLabel.setFont(AccountingGUI.font);
		targetAccountLabel.setOpaque(false);
		targetAccountLabel.setForeground(AccountingGUI.fontColor);
		
		targetAccountComboBox = new Java2sAutoComboBox(accountNames);
		targetAccountComboBox.setEditable(true);
		targetAccountComboBox.addActionListener(this);
		targetAccountComboBox.setFont(AccountingGUI.font);
		targetAccountComboBox.setForeground(AccountingGUI.fontColor);
		targetAccountComboBox.getEditor().getEditorComponent().addFocusListener(fl);
		
		JLabel descriptionLabel = new JLabel("Kuvaus");
		descriptionLabel.setFont(AccountingGUI.font);
		descriptionLabel.setOpaque(false);
		descriptionLabel.setForeground(AccountingGUI.fontColor);
		descriptionField = new JTextField();
		descriptionField.setOpaque(true);
		descriptionField.setColumns(20);
		descriptionField.setForeground(AccountingGUI.fontColor);
		descriptionField.setFont(AccountingGUI.font);
		descriptionField.addFocusListener(this);
		
		addNoteButton = new JButton("Lisää");
		addNoteButton.setForeground(AccountingGUI.fontColor);
		addNoteButton.setFont(AccountingGUI.font);
		addNoteButton.addActionListener(this);
		
		addAllButton = new JButton("Lisää kaikki");
		addAllButton.setForeground(AccountingGUI.fontColor);
		addAllButton.setFont(AccountingGUI.font);
		addAllButton.addActionListener(this);
		
		// Enables activating the button by pressing enter when it is focused
		@SuppressWarnings("serial")
		Action accept = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		       //((JButton)e.getSource()).doClick();
		    	addNoteButton.doClick();
		    	descriptionField.selectAll();
		    }
		};
/*
		addNoteButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		addNoteButton.getActionMap().put("pressed",
                accept);
		
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		this.getActionMap().put("pressed",accept);
		descriptionField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		descriptionField.getActionMap().put("pressed",accept);
		sumField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		sumField.getActionMap().put("pressed",accept);
		dateField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		dateField.getActionMap().put("pressed",accept);
		*/
		//From https://stackoverflow.com/questions/24336767/how-to-accept-a-value-in-a-swing-jcombobox-with-the-tab-key
        /*
		InputMap im = targetAccountComboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke existingKeyStroke = KeyStroke.getKeyStroke("ENTER");
        KeyStroke rightKS = KeyStroke.getKeyStroke("RIGHT");
        im.put(rightKS, im.get(existingKeyStroke));
*/
		
		
		InputMap im = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		this.getActionMap().put("pressed",accept);
		
		JTextField editor = (JTextField)targetAccountComboBox.getEditor().getEditorComponent();
		editor.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		editor.getActionMap().put("pressed",accept);

		skipNoteButton = new JButton("Ohita");
		skipNoteButton.setForeground(AccountingGUI.fontColor);
		skipNoteButton.setFont(AccountingGUI.font);
		skipNoteButton.addActionListener(this);
		
		skipAllButton = new JButton("Ohita kaikki");
		skipAllButton.setForeground(AccountingGUI.fontColor);
		skipAllButton.setFont(AccountingGUI.font);
		skipAllButton.addActionListener(this);
		
		constraints = new GridBagConstraints(0, 0, 1, 1, 1,
				1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(6, 4, 6, 4), 0, 0);
		
		constraints.gridwidth=3;
		add(numberLabel,constraints);
		constraints.gridwidth=1;
		constraints.gridx = 3;
		add(factorLabel,constraints);
		constraints.gridx++;
		add(skipAllButton,constraints);
		constraints.gridy++;
		constraints.gridx = 3;
		add(factorField,constraints);
		constraints.gridx++;
		add(skipNoteButton,constraints);
		constraints.gridx = 0;
		add(sourceAccountLabel, constraints);
		constraints.gridx = 1;
		add(sourceAccountComboBox, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		add(dateLabel, constraints);
		constraints.gridx++;
		add(sumLabel, constraints);
		constraints.gridx++;
		add(descriptionLabel, constraints);
		constraints.gridx++;
		add(targetAccountLabel, constraints);
		constraints.gridx++;
		add(addAllButton, constraints);
		constraints.gridx=0;
		constraints.gridy++;
		
		add(dateField, constraints);
		constraints.gridx++;
		add(sumField, constraints);
		constraints.gridx++;
		add(descriptionField, constraints);
		constraints.gridx++;
		add(targetAccountComboBox, constraints);
		constraints.gridx++;
		add(addNoteButton, constraints);
		
		if(notes.size() > 0) {
			setCurrentNoteInformation(currentNote);
		}
	}
	
	void setNoteNumber(JLabel targetLabel, int currentNote, int totalNumberOfNotes) {
		String labelText = String.format("Käsitellään merkintää %d / %d",currentNote, totalNumberOfNotes);
		targetLabel.setText(labelText);
	}
	
	public void setCurrentNoteInformation(int currentNote) {
		double factor;
		try{
		 factor = Double
				.parseDouble(factorField.getText());
		} catch(NumberFormatException e) {
			factor = 1;
		}
		descriptionField.setText(notes.get(currentNote-1).getDescription());
		sumField.setText(new Double(Math.round(notes.get(currentNote-1).getValue()*factor*100.0)/100.0).toString());
		//sumField.setText(new Double(notes.get(currentNote-1).getValue()*factor).toString());
		dateField.setValue(AccountingGUI.dateFormat.format(notes.get(currentNote-1).getDate()));
	}

	@Override
	public void focusGained(FocusEvent e) {
		//((JTextField)(e.getSource())).setCaretPosition(0);
		((JTextField)(e.getSource())).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == skipAllButton) {
			SwingUtilities.getWindowAncestor(this).dispose();
			String message = "Ohitettiin loput merkinnät.";
			GUI.updateStatus(message);
		} else if(e.getSource() == skipNoteButton) {
			 currentNote++;
			 if(currentNote > notes.size()) {
				 SwingUtilities.getWindowAncestor(this).dispose();
			 } else {
				 setCurrentNoteInformation(currentNote);
				 setNoteNumber(numberLabel,currentNote,notes.size());
			 }
			 String message = "Ohitettiin merkintä " + (currentNote-1) + ".";
				GUI.updateStatus(message);
			 
		} else if(e.getSource() == addNoteButton) {
			try {
			Account target = accountTree.getAccount(targetAccountComboBox.getSelectedItem()
					.toString().trim());
			Account source = accountTree.getAccount(sourceAccountComboBox.getSelectedItem()
					.toString().trim());
			if (target == null || source == null || source.equals(target)) {
				throw new NullPointerException();
			}
			
			double value = Double
					.parseDouble(sumField.getText());
			String description = descriptionField.getText();
			Date date = AccountingGUI.dateFormat.parse(dateField.getText());
			
			if(value < 0) {
				new Note(Math.abs(value), date, description, target, source);
			} else {
				new Note(Math.abs(value), date, description, source, target);
			}
			
			String message = "Lisättiin merkintä " + description + ".";
			GUI.updateStatus(message);
			
			currentNote++;
			if(currentNote > notes.size()) {
				 SwingUtilities.getWindowAncestor(this).dispose();
			 } else {
				 setCurrentNoteInformation(currentNote);
				 setNoteNumber(numberLabel,currentNote,notes.size());
			 }
			descriptionField.requestFocus();
			
			
			} catch (ParseException ex) {
				GUI.updateStatus("Virheellinen päivämäärä.");
				return;
			} catch (NumberFormatException ex) {
				GUI.updateStatus("Virheellinen summa.");
				return;
			} catch (NullPointerException ex) {
				GUI.updateStatus("Virheellinen tili.");
				//targetAccountComboBox.setForeground(AccountingGUI.errorColor);
				return;
			}
		 } else if(e.getSource() == addAllButton) {
					try {
						while(currentNote <= notes.size()) {
					Account target = accountTree.getAccount(targetAccountComboBox.getSelectedItem()
							.toString().trim());
					Account source = accountTree.getAccount(sourceAccountComboBox.getSelectedItem()
							.toString().trim());
					if (target == null || source == null || source.equals(target)) {
						throw new NullPointerException();
					}
					double value = Double
							.parseDouble(sumField.getText());
					String description = descriptionField.getText();
					Date date = AccountingGUI.dateFormat.parse(dateField.getText());
					
					if(value < 0) {
						new Note(Math.abs(value), date, description, target, source);
					} else {
						new Note(Math.abs(value), date, description, source, target);
					}
					
					String message = "Lisättiin merkintä " + description + ".";
					GUI.updateStatus(message);
					
					currentNote++;
					if(currentNote > notes.size()) {
						 SwingUtilities.getWindowAncestor(this).dispose();
					 } else {
						 setCurrentNoteInformation(currentNote);
						 setNoteNumber(numberLabel,currentNote,notes.size());
					 }
						}
					
					
					} catch (ParseException ex) {
						//updateStatus("Virheellinen päivämäärä");
						return;
					} catch (NumberFormatException ex) {
						//updateStatus("Virheellinen summa");
						return;
					} catch (NullPointerException ex) {
						//targetAccountComboBox.setForeground(AccountingGUI.errorColor);
						//updateStatus("Virheellinen tili");
						return;
				 }
		 }
		
	}

}
