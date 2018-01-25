package userInterface;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import javax.swing.JPanel;
import javax.swing.JTextField;

import coreClasses.Note;

public class NoteContainer extends JPanel {

	private static final long serialVersionUID = 1L;

	private Note note;
	JFormattedTextField dateField;
	JTextField descriptionField;
	JComboBox debet;
	JComboBox credit;
	JTextField value;

	public NoteContainer(Note note, JFormattedTextField dateField,
			JTextField description, JComboBox debet, JComboBox credit,
			JTextField value) {
		super();
		this.dateField = dateField;
		descriptionField = description;
		descriptionField.setColumns(20);
		this.debet = debet;
		this.credit = credit;
		this.value = value;
		this.value.setColumns(8);
		this.note = note;

		this.setLayout(new GridBagLayout());
		GridBagConstraints contraints = new GridBagConstraints(0, 0, 1, 1, 0,
				1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(3, 4, 3, 4), 0, 0);

		// this.setOpaque(false);

		this.add(dateField, contraints);
		contraints.gridx++;
		contraints.weightx = 0;
		this.add(descriptionField, contraints);
		contraints.weightx = 0;
		contraints.gridx++;
		this.add(value, contraints);
		contraints.weightx = 0;
		contraints.gridx++;
		this.add(debet, contraints);
		contraints.gridx++;
		contraints.weightx = 1;
		this.add(credit, contraints);
		
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public void activate() {

	}
}