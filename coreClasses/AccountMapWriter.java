package coreClasses;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import dataStructures.SortedList;

public class AccountMapWriter {
	
	private String fileName;
	private AccountMap accountMap;
	private ArrayList<String> errors;
	private int errorCount;
	

	public AccountMapWriter(String fileName, AccountMap map) {
		this.fileName = fileName;
		this.accountMap = map;
		errors = new ArrayList<String>();
	}
	
	public void writeMap() {
		try {
            java.io.FileWriter fstream = new java.io.FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            writeAccounts(out);
            writeNotes(out);
            out.close();
        } catch (IOException e) {
            setError("Can't save the file" + fileName + ".\n");
        }
	}
	
    private void writeAccounts(BufferedWriter out) throws IOException {
    	out.write("#Accounts {\n");
		for(Account account : accountMap.getAccounts().values()) {
			String parent = null;
			if (account.getParent() == null) {
				parent = "";
			} else {
				parent = account.getParent().getName();
			}
			
			out.write(String.format("%s;%s;%s\n", account.getName(),parent,account.getDescription()));
		}
		out.write("}\n\n");
	}
    
    private void writeNotes(BufferedWriter out) throws IOException {
    	out.write("#Notes {\n");
    	LinkedList<Note> notes = getNotes(accountMap.getRoot());
    	for(Note note : notes) {
    		out.write(String.format("%s;%s;%s;%s;%s\n", note.getDescription(), note.getDate().getTime(), note.getDebet().getName(), note.getCredit().getName(), note.getValue()));
    	}
    	out.write("}\n\n");
    }

	public int getErrorCount() {
        return errorCount;
    }
    
    private void setError(String error) {
        errors.add(error);
        errorCount++;
    }
    
    public String printsErrors() {
        StringBuilder errorString = new StringBuilder();
        for(int i= 0; i< errors.size(); i++) {
            errorString.append(errors.get(i));
        }
        return errorString.toString();
    }
    
    public LinkedList<Note> getNotes(Account root) {
    	LinkedList<Note> notes = new LinkedList<Note>();
    	for(Note note : root.getNotes()) {
    		if(note.getDebet().equals(root)) {
    			notes.add(note);
    		}
    	}
    	for(Account child : root.getChildren()) {
    		notes.addAll(getNotes(child));
    	}
    	
    	return notes;
    }
}
