package coreClasses;

import dataStructures.SortedList;

public class CurrencyTradingAccount {
	private String currency1;
	private String currency2;
	private String name;
	private Account account1;
	private Account account2;
	
	private SortedList<Note> notes1;
	private SortedList<Note> notes2;
	
	public CurrencyTradingAccount(String currency1, String currency2) {
		this.currency1 = currency1;
		this.currency2 = currency2;
		this.name = currency1 + '-' + currency2;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCurrency1() {
		return currency1;
	}
	public String getCurrency2() {
		return currency2;
	}
	
	public SortedList<Note> getNotes1() {
		return notes1;
	}
	
	public SortedList<Note> getNotes2() {
		return notes2;
	}
	/**
	 * Add a DualCurrencyNote to the account. Creates two single currency notes that are added to both the target accounts.
	 * @param note DualCurrencyNote, which currencies have to match the currencies of the trading account.
	 * @return true if successful, false otherwise.
	 */
	boolean addNote(DualCurrencyNote note) {
		String currency1 = note.getCredit().getCurrency();
		String currency2 = note.getDebet().getCurrency();
		
		// Cannot add a note which currencies do not match the currencies of the trading account.
		if((!currency1.equals(this.currency1) && !currency1.equals(this.currency2)) || (!currency2.equals(this.currency1) && !currency2.equals(this.currency2))) {
			return false;
		}
		
		if(currency1.equals(this.currency1)) {
			// Creating a new note automatically adds it the credit and debet accounts.
			new Note(note.getValueCredit(),note.getDate(),note.getDescription(),account1,note.getCredit());
			new Note(note.getValueDebet(),note.getDate(),note.getDescription(),note.getDebet(),account2);
			return true;
		} else {
			new Note(note.getValueCredit(),note.getDate(),note.getDescription(),account2,note.getCredit());
			new Note(note.getValueDebet(),note.getDate(),note.getDescription(),note.getDebet(),account1);
			return true;
		}
	}
	
	/**
	 * Add a single currency note to the currency trading account. This can be used to account for losses/gains due to currency trading.
	 * @param note A single currency note.
	 * @return true if successful, false otherwise.
	 */
	boolean addNote(Note note) {
		//TODO
		String currency1 = note.getCredit().getCurrency();
		String currency2 = note.getDebet().getCurrency();
		
		if(!currency1.equals(currency2)) {
			return false;
		}
		
		if(currency1.equals(this.currency1)) {
			return notes1.add(note);
		} else if(currency2.equals(this.currency2)) {
			return notes2.add(note);
		} else {
			return false;
		}
	}
}
