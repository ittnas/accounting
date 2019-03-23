package coreClasses;

import java.util.ArrayList;

import dataStructures.SortedList;

public class Account {
	
	private String description;
	private String name;
	private ArrayList<Account> children;
	private SortedList<Note> notes;
	private Account parent = null;
	private String currency = null;
	final private static String defaultCurrency = "EUR";
	
	
	public Account(String name, String description) {
		this(name,description,defaultCurrency);
	}
	
	public Account(String name, String description, String currency) {
		this.description = description;
		this.name = name;
		this.currency = currency;
		children = new ArrayList<Account>();
		notes = new SortedList<Note>();
	}

	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		//TODO Should also return list of children
		return description;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	boolean addNote(Note note) {
		return notes.add(note);
	}
	
	public void addChild(Account child) {
		children.add(child);
		if(child.parent != null) {
			child.getParent().children.remove(child);
		}
		child.setParent(this);
	}
	
	public ArrayList<Account> getChildren() {
		return children;
	}
	public ArrayList<Account> getChildren(int level) {
			if(children == null || children.size() == 0) {
				ArrayList<Account> acc = new ArrayList<Account>();
				acc.add(this);
				return acc;
			} else if(level < 1) {
				return children;
			} else {
				ArrayList<Account> children = new ArrayList<Account>();
				for(Account child : this.children) {
					children.addAll(child.getChildren(level-1));
				}
				return children;
			}
	}
	
	void setParent(Account parent) {
		this.parent = parent;
	}
	
	public Account getParent() {
		return parent;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public void setDescription(String newDescr) {
		this.description = newDescr;
	}
	/*
	public ArrayList<Account> getChildrenWithNoChilds() {
		ArrayList<Account> children = new ArrayList<Account>();
		for(Account child : this.children) {
			if(child.getChildren().size() == 0) {
			children.addAll(child.getChildrenWithNoChilds());
			}
		}
		return children;
	}
	*/
	
	/**
	 * Returns the list of notes.
	 * @return the notes.
	 */
	
	public SortedList<Note> getNotes() {
		return notes;
	}
	
	boolean removeNote(Note note) {
		if(note.getDebet() == this) {
			note.voidDebet();
		}
		if(note.getCredit() == this) {
			note.voidCredit();
		}
		return notes.remove(note);
	}
	
	public double getValue() {
		double value = 0;
		for(Note note : notes) {
			value += note.getSignedValue(this);
		}
		for(Account child : children) {
			value += child.getValue();
		}
		return value;
	}
	
	public boolean isDescendant(Account ancestor) {
		if(ancestor.equals(this)) {
			return true;
		}
		if(parent == null) {
			return false;
		} else return parent.isDescendant(ancestor);
	}
	
	public String toString() {
		return String.format(name + "  %.2f %s", getValue(),getCurrency());
	}
	
	/**
	 * Test the Account class.
	 * @param args
	 */
	public static void main(String[] args) {
		Account A = new Account("A", "","USD");
		System.out.println(A.toString());
	}
}