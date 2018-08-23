package coreClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import dataStructures.SortedList;

public class AccountMap {

	private HashMap<String, Account> accounts;
	private Account root;

	public AccountMap() {
		accounts = new HashMap<String, Account>();
	}
	
	public AccountMap(HashMap<String, Account> accounts) {
		this.accounts = accounts;
	}
	
	public void setRoot(Account root) {
		this.root = root;
		addAccount(root);
	}
	
	/**
	 * Returns the root account.
	 * @return the root account
	 */
	
	public Account getRoot() {
		return root;
	}

	public boolean addAccount(Account account) {
		if (accounts.containsKey(account.getName())) {
			return false;
		}
		accounts.put(account.getName(), account);
		return true;
	}

	public HashMap<String, Account> getAccounts() {
		return accounts;
	}
	
	public Account getAccount(String target) {
		return accounts.get(target);
	}
	
	public void updateAccountName(String oldName, Account account) {
		accounts.remove(oldName);
		accounts.put(account.getName(), account);
	}
	/*
	public String[] getAccountNames(boolean withNoChildren) {
	//TODO tuskin tarvitaan tätä
		String[] names = new String[accounts.size()];
		int i = 0;
		if (withNoChildren) {

			for (Account current : accounts.values()) {
				if (current.getChildren().size() == 0) {
					names[i] = current.getName();
					i++;
				}
			}
		} else {
			for (Account current : accounts.values()) {
				names[i] = current.getName();
				i++;

			}
		}
		return names;
	}
	*/
	public ArrayList<String> getAccountNames(boolean withNoChildren) {
		
		ArrayList<String> names = new ArrayList<String>();
		int i = 0;
		if (withNoChildren) {

			for (Account current : accounts.values()) {
				if (current.getChildren().size() == 0) {
					names.add(current.getName());
				}
			}
		} else {
			for (Account current : accounts.values()) {
				names.add(current.getName());
			}
		}
		Collections.sort(names);
		return names;
	}
	
	public boolean containsKey(String account) {
		return accounts.containsKey(account);
	}

	public boolean removeAccount(Account deletable, boolean recursiveRemove) {
		if(!recursiveRemove) {
		Account parent = deletable.getParent();
		if (parent != null) {
			
			while(deletable.getNotes().size() != 0) {
				Note note = deletable.getNotes().getFirst();
				Account debet = note.getDebet();
				Account credit = note.getCredit();
				if (deletable.equals(debet)) {
					debet = parent;
				}
				if (deletable.equals(note.getCredit())) {
					credit = parent;
				}
				new Note(note.getValue(), note.getDate(), note.getDescription(), debet, credit);
				note.remove();
			}
			
			while(deletable.getChildren().size() != 0) {
				parent.addChild(deletable.getChildren().get(0));
			}
			parent.getChildren().remove(deletable);
			accounts.remove(deletable.getName());
			return true;
		} else {
			return false;
		}
		} else {
			while(deletable.getChildren().size() != 0) {
				removeAccount(deletable.getChildren().get(0), true);
			}
			return removeAccount(deletable, false);
		}
	}
	
	public SortedList<Note> getNotesInRange(Account root, Date start, Date end) {
		
		Note startNote = new Note(0, start, "", null, null);
		Note endNote = new Note(0, end, "", null, null);
		SortedList<Note> notes = new SortedList<Note>();
		for(Account account : accounts.values()) {
			if(account.isDescendant(root)) {
				notes.addAll(account.getNotes().getRange(startNote, endNote));
			}
		}
		Collections.sort(notes);
		return notes;
	}
}
