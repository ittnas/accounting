package userInterface;

import java.text.DateFormat;

import javax.swing.table.AbstractTableModel;

import coreClasses.Note;

import dataStructures.SortedList;

public class NoteTableModel extends AbstractTableModel {

	private SortedList<Note> data;
	private String[] columnNames = {"Päivämäärä", "Tapahtuma", "Määrä", "Debet-tili", "Credit-tili"};
	private DateFormat dateFormat;
	
	private static final long serialVersionUID = 1L;
	
	public NoteTableModel(SortedList<Note> data, DateFormat format) {
		this.data = data;
		this.dateFormat = format;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public Note getNoteAt(int row) {
		return data.get(row);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}
	
	public String getColumnName(int col) {
		return columnNames[col].toString();
	}
	
	public boolean isCellEditable(int row, int col)
    { return false; }


	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0: return dateFormat.format(data.get(row).getDate());
		case 1: return data.get(row).getDescription();
		case 2: return String.format("%.2f EUR ", data.get(row).getValue());
		case 3: return data.get(row).getDebet().getName();
		case 4: return data.get(row).getCredit().getName();
		default: return null;
		}
		
		
	}

}
