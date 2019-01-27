package coreClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AccountMapXMLWriter extends AccountMapWriter {

	public AccountMapXMLWriter(String fileName, AccountMap map) {
		super(fileName, map);
	}
	
	@Override
	public void writeMap() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Root");
			doc.appendChild(root);
			writeXMLAccounts(root, doc);
			writeXMLNotes(root, doc);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File out_file = new File(fileName);
			StreamResult result = new StreamResult(out_file);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);
			
			/**
			java.io.FileWriter fstream = new java.io.FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            writeAccounts(out);
            writeNotes(out);
            out.close();
            **/
        } catch (ParserConfigurationException | TransformerException e) {
        	setError("Cannot create the xml file " + fileName + ".\n");
        }
	}
	
    private void writeXMLAccounts(Element root, Document doc) {
    	Element accountsElement = doc.createElement("Accounts");
    	root.appendChild(accountsElement);
    	for(Account account : accountMap.getAccounts().values()) {
			Element accountElement = doc.createElement("Account");
			accountsElement.appendChild(accountElement);
			
			Element accountNameElement = doc.createElement("AccountName");
			accountElement.appendChild(accountNameElement);
			accountNameElement.appendChild(doc.createTextNode(account.getName()));
			
    		String parent = null;
			if (account.getParent() == null) {
				parent = "";
			} else {
				parent = account.getParent().getName();
			}
			
			Element parentElement = doc.createElement("Parent");
			parentElement.appendChild(doc.createTextNode(parent));
			accountElement.appendChild(parentElement);
			
			Element descriptionElement = doc.createElement("Description");
			descriptionElement.appendChild(doc.createTextNode(account.getDescription()));
			accountElement.appendChild(descriptionElement);
    	}
    	/**
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
		**/
	}
    
    private void writeXMLNotes(Element root, Document doc) {
    	Element notesElement = doc.createElement("Notes");
    	root.appendChild(notesElement);
    	LinkedList<Note> notes = getNotes(accountMap.getRoot());
    	for(Note note : notes) {
    		Element noteElement = doc.createElement("Note");
    		notesElement.appendChild(noteElement);
    		
			Element descriptionElement = doc.createElement("Description");
			descriptionElement.appendChild(doc.createTextNode(note.getDescription()));
			noteElement.appendChild(descriptionElement);
			
			Element dateElement = doc.createElement("Date");
			dateElement.appendChild(doc.createTextNode(Long.toString(note.getDate().getTime())));
			noteElement.appendChild(dateElement);
			
			Element debetElement = doc.createElement("Debet");
			debetElement.appendChild(doc.createTextNode(note.getDebet().getName()));
			noteElement.appendChild(debetElement);
			
			Element creditElement = doc.createElement("Credit");
			creditElement.appendChild(doc.createTextNode(note.getCredit().getName()));
			noteElement.appendChild(creditElement);
			
			Element valueElement = doc.createElement("Value");
			valueElement.appendChild(doc.createTextNode(Double.toString(note.getValue())));
			noteElement.appendChild(valueElement);
    		//out.write(String.format("%s;%s;%s;%s;%s\n", note.getDescription(), note.getDate().getTime(), note.getDebet().getName(), note.getCredit().getName(), note.getValue()));
    	}
    	/**
    	out.write("#Notes {\n");
    	LinkedList<Note> notes = getNotes(accountMap.getRoot());
    	for(Note note : notes) {
    		out.write(String.format("%s;%s;%s;%s;%s\n", note.getDescription(), note.getDate().getTime(), note.getDebet().getName(), note.getCredit().getName(), note.getValue()));
    	}
    	out.write("}\n\n");
    	**/
    }
    
    /**
    private void writeNotes(BufferedWriter out) throws IOException {
    	out.write("#Notes {\n");
    	LinkedList<Note> notes = getNotes(accountMap.getRoot());
    	for(Note note : notes) {
    		out.write(String.format("%s;%s;%s;%s;%s\n", note.getDescription(), note.getDate().getTime(), note.getDebet().getName(), note.getCredit().getName(), note.getValue()));
    	}
    	out.write("}\n\n");
    }
	**/

}
