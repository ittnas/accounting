package coreClasses;

import java.util.Date;

public class DualCurrencyNote  implements Comparable<DualCurrencyNote> {
	private double value_debet;
	private double value_credit;
	private Date date;
	private Account debet;
	private Account credit;
	private String description;
	
	public DualCurrencyNote(double value_debet, double value_credit, Date date, String description, Account debet, Account credit) {
		this.value_debet = value_debet;
		this.value_credit = value_credit;
		this.date = date;
		this.description = description;
		this.debet = debet;
		this.credit = credit;
	}

	@Override
	public int compareTo(DualCurrencyNote o) {
		return -1*this.date.compareTo(o.getDate());
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
	
	public double getValueDebet() {
		return value_debet;
	}
	
	public double getValueCredit() {
		return value_credit;
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
