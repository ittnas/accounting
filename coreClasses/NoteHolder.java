package coreClasses;

import java.util.Date;

public class NoteHolder {
	
	private double value;
	private Date date;
	private String description;
	private Account debet;
	private Account credit;
	
	public NoteHolder(double value, Date date, String description, Account debet, Account credit) {
		this.setDebet(debet);
		this.setCredit(credit);
		this.setValue(value);
		this.setDate(date);
		this.setDescription(description);
	}

	/**
	 * @param debet the debet to set
	 */
	public void setDebet(Account debet) {
		this.debet = debet;
	}

	/**
	 * @return the debet
	 */
	public Account getDebet() {
		return debet;
	}

	/**
	 * @param credit the credit to set
	 */
	public void setCredit(Account credit) {
		this.credit = credit;
	}

	/**
	 * @return the credit
	 */
	public Account getCredit() {
		return credit;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

}
