package userInterface;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import coreClasses.AccountMap;
import coreClasses.Note;

public class AddNotePanel extends JPanel implements ActionListener, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFormattedTextField dateField;
	private JTextField sumField;
	private Java2sAutoComboBox debetAccountComboBox;
	private Java2sAutoComboBox creditAccountComboBox;
	private JTextField descriptionField;
	
	private AccountMap accountTree;
	private GridBagConstraints constraints;
	private Note selectedNote;

	public AddNotePanel(AccountMap accountTree, Note note) {
		super();
		this.accountTree = accountTree;
		selectedNote = note;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		
		FocusListener comboboxFL = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(e.getSource() == debetAccountComboBox.getEditor().getEditorComponent()) {
					debetAccountComboBox.getEditor().selectAll();
					JTextField editor = (JTextField)debetAccountComboBox.getEditor().getEditorComponent();
					editor.setCaretPosition(0);
				}
				if(e.getSource() == creditAccountComboBox.getEditor().getEditorComponent()) {
					creditAccountComboBox.getEditor().selectAll();
					JTextField editor = (JTextField)creditAccountComboBox.getEditor().getEditorComponent();
					editor.setCaretPosition(0);
				}
				/*
				ComboBoxEditor editor = ((JComboBox<Account>)e.getSource()).getEditor();
		        JTextField textField = (JTextField)editor.getEditorComponent();
		        textField.setCaretPosition(0);
		        */
			}
		};
		
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
		JLabel depetAccountLabel = new JLabel("Debet-tili");
		depetAccountLabel.setFont(AccountingGUI.font);
		depetAccountLabel.setOpaque(false);
		depetAccountLabel.setForeground(AccountingGUI.fontColor);
		ArrayList<String> accountNames = accountTree.getAccountNames(false);
		debetAccountComboBox = new Java2sAutoComboBox(accountNames);
		debetAccountComboBox.setEditable(true);
		debetAccountComboBox.addActionListener(this);
		debetAccountComboBox.setFont(AccountingGUI.font);
		debetAccountComboBox.setForeground(AccountingGUI.fontColor);
		debetAccountComboBox.getEditor().getEditorComponent().addFocusListener(comboboxFL);

		JLabel creditAccountLabel = new JLabel("Credit-tili");
		creditAccountLabel.setFont(AccountingGUI.font);
		creditAccountLabel.setOpaque(false);
		creditAccountLabel.setForeground(AccountingGUI.fontColor);

		creditAccountComboBox = new Java2sAutoComboBox(accountNames);
		creditAccountComboBox.setEditable(true);
		creditAccountComboBox.addActionListener(this);
		creditAccountComboBox.setFont(AccountingGUI.font);
		creditAccountComboBox.setForeground(AccountingGUI.fontColor);
		creditAccountComboBox.getEditor().getEditorComponent().addFocusListener(comboboxFL);
		JLabel descriptionLabel = new JLabel("Kuvaus");
		descriptionLabel.setFont(AccountingGUI.font);
		descriptionLabel.setOpaque(false);
		descriptionLabel.setForeground(AccountingGUI.fontColor);
		descriptionField = new JTextField();
		descriptionField.setOpaque(true);
		descriptionField.setColumns(12);
		descriptionField.setForeground(AccountingGUI.fontColor);
		descriptionField.setFont(AccountingGUI.font);
		descriptionField.addFocusListener(this);

		constraints = new GridBagConstraints(0, 0, 1, 1, 1,
				1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(6, 4, 6, 4), 0, 0);
		add(dateLabel, constraints);
		constraints.gridx++;
		add(descriptionLabel, constraints);
		constraints.gridx++;
		add(sumLabel, constraints);
		constraints.gridx++;
		add(depetAccountLabel, constraints);
		constraints.gridx++;
		add(creditAccountLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(dateField, constraints);
		constraints.gridx++;
		add(descriptionField, constraints);
		constraints.gridx++;
		add(sumField, constraints);
		constraints.gridx++;
		add(debetAccountComboBox, constraints);
		constraints.gridx++;
		add(creditAccountComboBox, constraints);
		if(selectedNote != null) {
			descriptionField.setText(selectedNote.getDescription());
			sumField.setText((new Double(selectedNote.getValue())).toString());
			debetAccountComboBox.setSelectedItem(selectedNote.getDebet().getName());
			creditAccountComboBox.setSelectedItem(selectedNote.getCredit().getName());
			dateField.setValue(AccountingGUI.dateFormat.format(note.getDate()));
		}
	}
	
	public void addButton(JButton button) {
		constraints.gridx++;
		add(button, constraints);
	}
	
	public Java2sAutoComboBox getDebetAccountCombobox() {
		return debetAccountComboBox;
	}
	
	public Java2sAutoComboBox getCreditAccountCombobox() {
		return creditAccountComboBox;
	}
	
	public JTextField getSumField() {
		return sumField;
	}
	
	public JTextField getDescriptionField() {
		return descriptionField;
	}
	
	public JFormattedTextField getDateField() {
		return dateField;
	}
	
	public Note getNote() {
		return selectedNote;
	}
	public ArrayList<Component> getTextComponents() {
		ArrayList<Component> list = new ArrayList<Component>();
		list.add(dateField);
		list.add(sumField);
		list.add(debetAccountComboBox);
		list.add(creditAccountComboBox);
		list.add(descriptionField);
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			JComboBox box = (JComboBox) e.getSource();
			String selection = (String) box.getSelectedItem();
			if (selection != null) {
				selection = selection.trim();
			}
			if (!accountTree.getAccounts().containsKey(selection)) {
				box.getEditor().getEditorComponent().setForeground(AccountingGUI.errorColor);
			} else {
				box.getEditor().getEditorComponent().setForeground(AccountingGUI.fontColor);
			}
			
		}
		
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
}
