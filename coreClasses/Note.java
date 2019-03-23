package coreClasses;

import java.util.Date;

public class Note implements Comparable<Note> {
	
	double value;
	Date date;
	String description;
	private Account debet;
	private Account credit;
	
	public Note(double value, Date date, String description, Account debet, Account credit) {
		this.debet = debet;
		this.credit = credit;
		this.value = value;
		this.date = date;
		this.description = description;
		if(this.debet != null && !credit.equals(debet)) {
			this.debet.addNote(this);
		}
		if(this.credit != null && !credit.equals(debet)) {
			this.credit.addNote(this);
		}
	}
	
	public double getValue() {
		return value;
	}
	
	public double getSignedValue(Account enquirer) {
		double value = 0.0;
		if(credit.isDescendant(enquirer)) {
			value -= this.value;
		}
		if(debet.isDescendant(enquirer)) {
			 value += this.value;
		}
		//If any other account than owner enquires the value;
		return value;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Account getDebet() {
		return debet;
	}
	
	public void voidCredit() {
		credit = null;
	}
	
	public void voidDebet() {
		debet = null;
	}
	
	public Account getCredit() {
		return credit;
	}
	
	public void remove() {
		if (credit != null) {
			credit.removeNote(this);
		}
		if(debet != null) {
			debet.removeNote(this);
		}
	}

	@Override
	public int compareTo(Note o) {
		return -1*this.date.compareTo(o.getDate());
	}
	
	public String toString() {
		String output = "";
		if(debet == null) {
			output = "null:";
		} else {
			output = debet.getName() + ":";
		}
		if(credit == null) {
			output += "null";
		} else {
			output += credit.getName();
		}
		return output; 
	}
	
}
