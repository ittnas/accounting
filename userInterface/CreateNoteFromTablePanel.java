package userInterface;

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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

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
	
	private AccountMap accountTree;
	private ArrayList<NoteHolder> notes;
	private GridBagConstraints constraints;
	private JButton addNoteButton;
	private JButton skipNoteButton;
	private JButton skipAllButton;

	public CreateNoteFromTablePanel(AccountMap accountTree, ArrayList<NoteHolder> notes) {
		super();
		this.accountTree = accountTree;
		this.notes = notes;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		
		JLabel numberLabel = new JLabel("");
		numberLabel.setFont(AccountingGUI.font);
		numberLabel.setOpaque(false);
		numberLabel.setForeground(AccountingGUI.fontColor);
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
		JLabel sourceAccountLabel = new JLabel("Lähdetili");
		sourceAccountLabel.setFont(AccountingGUI.font);
		sourceAccountLabel.setOpaque(false);
		sourceAccountLabel.setForeground(AccountingGUI.fontColor);
		ArrayList<String> accountNames = accountTree.getAccountNames(false);
		sourceAccountComboBox = new Java2sAutoComboBox(accountNames);
		sourceAccountComboBox.setEditable(true);
		sourceAccountComboBox.addActionListener(this);
		sourceAccountComboBox.setFont(AccountingGUI.font);
		sourceAccountComboBox.setForeground(AccountingGUI.fontColor);
		sourceAccountComboBox.addFocusListener(this);
		
		JLabel targetAccountLabel = new JLabel("Kohdetili");
		targetAccountLabel.setFont(AccountingGUI.font);
		targetAccountLabel.setOpaque(false);
		targetAccountLabel.setForeground(AccountingGUI.fontColor);
		
		targetAccountComboBox = new Java2sAutoComboBox(accountNames);
		targetAccountComboBox.setEditable(true);
		targetAccountComboBox.addActionListener(this);
		targetAccountComboBox.setFont(AccountingGUI.font);
		targetAccountComboBox.setForeground(AccountingGUI.fontColor);
		targetAccountComboBox.addFocusListener(this);
		
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
		
		addNoteButton = new JButton("Lisää");
		addNoteButton.setForeground(AccountingGUI.fontColor);
		addNoteButton.setFont(AccountingGUI.font);
		addNoteButton.addActionListener(this);
		
		// Enables activating the button by pressing enter when it is focused
		@SuppressWarnings("serial")
		Action accept = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		       ((JButton)e.getSource()).doClick();
		    }
		};

		addNoteButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		addNoteButton.getActionMap().put("pressed",
                accept);
		
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
		
		add(numberLabel,constraints);
		
		constraints.gridx = 4;
		add(skipAllButton,constraints);
		constraints.gridy++;
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
			descriptionField.setText(notes.get(0).getDescription());
			sumField.setText(new Double(notes.get(0).getValue()).toString());
			dateField.setValue(AccountingGUI.dateFormat.format(notes.get(0).getDate()));
		}
	}
	
	void setNoteNumber(JLabel targetLabel, int currentNote, int totalNumberOfNotes) {
		String labelText = String.format("Käsitellään merkintää %d / %d",currentNote, totalNumberOfNotes);
		targetLabel.setText(labelText);
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
