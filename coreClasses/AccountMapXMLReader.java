package coreClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AccountMapXMLReader extends AccountMapReader {

	public AccountMapXMLReader(String fileName) {
		super(fileName);
	}
	
	@Override
	public AccountMap readMap() {
		HashMap<String, Account> accounts = new HashMap<String, Account>();
		Account root = null;
		try {
			
			File inputFile = new File(fileName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        
	        // Read the accounts
	        NodeList nList = doc.getElementsByTagName("Account");
	        for (int ii=0; ii < nList.getLength(); ii++) {
	        	Node nNode = nList.item(ii);
	        	//System.out.println("\nCurrent Element :" + nNode.getNodeName());
	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        		Element eElement = (Element) nNode;
	        		String name = eElement.getElementsByTagName("AccountName").item(0).getTextContent(); 
	        		String parent = eElement.getElementsByTagName("Parent").item(0).getTextContent();
	        		String description = eElement.getElementsByTagName("Description").item(0).getTextContent();
	        		//System.out.println(accountName);
	        		//System.out.println(parent);
	        		//System.out.println(description);
	        		Account newAccount = null;
	        		if (accounts.containsKey(name)) {
	        			newAccount = accounts.get(name);
	        			newAccount.setDescription(description);
	        		} else {
	        			newAccount = new Account(name, description);
	        			accounts.put(name, newAccount);
	        		}
	        		if (accounts.containsKey(parent)) {
						accounts.get(parent).addChild(newAccount);
					} else {
						if(parent.equals("")) {
							if(root != null) {
								setWarning("Tilipuussa on useampia juuritilejä.");
							}
							root = newAccount;
						} else {
						Account parentAccount = new Account(parent, "");
						parentAccount.addChild(newAccount);
						accounts.put(parent, parentAccount);
						}
					}
	        	}
	        	
	        }
			map = new AccountMap(accounts);
			map.setRoot(root);
			// Read the notes
			nList = doc.getElementsByTagName("Note");
			System.out.println("Starting to read notes.");
			System.out.println(nList.getLength());
	        for (int ii=0; ii < nList.getLength(); ii++) {
	        	Node nNode = nList.item(ii);
	        	System.out.println("\nCurrent Element :" + nNode.getNodeName());
	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        		Element eElement = (Element) nNode; 
	        		String dateStr = eElement.getElementsByTagName("Date").item(0).getTextContent();
	        		String description = eElement.getElementsByTagName("Description").item(0).getTextContent();
	        		String debetStr = eElement.getElementsByTagName("Debet").item(0).getTextContent();
	        		String creditStr = eElement.getElementsByTagName("Credit").item(0).getTextContent();
	        		String valueStr = eElement.getElementsByTagName("Value").item(0).getTextContent();
	        		long dateValue = 0;
					try {
						dateValue = Long.parseLong(dateStr);
					} catch (NumberFormatException ex) {
						setError(String.format(
								"Merkinnän %s päivämäärä on virheellinen.",
								description));
						continue;
					}
					Date date = new Date(dateValue);
					Account debet = null;
					if (map.containsKey(debetStr)) {
						debet = map.getAccount(debetStr);
					} else {
						setError(String.format(
								"Merkinnän %s debet-tili on virheellinen",
								description));
						continue;
					}
					Account credit = null;
					if (map.containsKey(creditStr)) {
						credit = map.getAccount(creditStr);
					} else {
						setError(String.format(
								"Merkinnän %s credit-tili on virheellinen",
								description));
						continue;
					}

					double value = 0;
					try {
						value = Double.parseDouble(valueStr);
					} catch (NumberFormatException ex) {
						setError(String.format(
								"Merkinnän %s arvo on virheellinen.", description));
						continue;
					}
					new Note(value, date, description, debet, credit);
	        	}
	        }
			
		} catch (Exception e) {
			setError("Virhe luettaessa tiedostoa " + fileName + ".");
		}
		
	
		
		return map;

	}
	
	


}
