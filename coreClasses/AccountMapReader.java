package coreClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AccountMapReader {

	private String fileName;
	private ArrayList<String> errorReport;
	private ArrayList<String> warningReport;
	private AccountMap map;

	public AccountMapReader(String fileName) {
		this.fileName = fileName;
		errorReport = new ArrayList<String>();
		warningReport = new ArrayList<String>();
	}

	public AccountMap readMap() {
		String line = null;
		boolean allRead = false;
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				if (line.contains("#Accounts")) {
					readAccounts(br, line);
					allRead = true;
				} else	if (line.contains("#Notes")) {
					readNotes(br, line);
				}
			}
			in.close();
		} catch (IOException e) {
			setError("Tiedostoa ei voida avata tai se ei ole olemassa.");
			return null;
		} catch (BadFileException e) {
			setError("Tiedosto on korruptoitunut");
		}
		if(allRead) {
			return map;
		} else {
			setError("#Accounts-osio puuttuu kokonaan.");
			return null;
		}
		
	}

	private void readNotes(BufferedReader br, String line) throws IOException {
		if (map == null) {
			setError("#Accounts-osion on sijoittava #Notes-osiota ennen.");
		}
		try {
			line = cutTheCurve(br, line);
		} catch (NoBracketException e) {
			return;
		}
		boolean end = false;

		do {
			int index = line.indexOf('}');
			if (index != -1) {
				line = line.substring(0, index);
				end = true;
			}
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			String[] words = line.split(";");
			String description = null;
			String dateStr = null;
			String debetStr = null;
			String creditStr = null;
			String valueStr = null;

			if (words.length != 5 && words.length > 0) {
				setError(String.format("Virheellinen merkintä %s.", words[0]));
			} else if (words.length != 5) {
				setError("Virheellinen merkintä.");
			} else {
				description = words[0];
				dateStr = words[1];
				debetStr = words[2];
				creditStr = words[3];
				valueStr = words[4];
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
		} while (((line = br.readLine()) != null) && !end);
	}

	private void readAccounts(BufferedReader br, String line)
			throws IOException, BadFileException {
		try {
			line = cutTheCurve(br, line);
		} catch (NoBracketException e) {
			return;
		}
		HashMap<String, Account> accounts = new HashMap<String, Account>();
		boolean end = false;
		Account root = null;
		do {
			int index = line.indexOf('}');
			if (index != -1) {
				line = line.substring(0, index);
				end = true;
			}
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			String[] words = line.split(";");
			String name = null;
			String parent = null;
			String description = null;

			if (words.length < 2) {
				setError(String.format("Virheellinen tili %s.", words[0]));
			} else {
				if (words.length == 2) {
					setWarning(String.format("Tilin %s kuvaus puuttuu",
							words[0]));
					name = words[0];
					parent = words[1];
					description = "";
				}
				if (words.length > 3) {
					setWarning(String.format(
							"Tilillä %s on liikaa argumentteja", words[0]));
					name = words[0];
					parent = words[1];
					description = words[2];
				}
				if (words.length == 3) {
					name = words[0];
					parent = words[1];
					description = words[2];
				}
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
		} while (((line = br.readLine()) != null) && !end);
		map = new AccountMap(accounts);
		if(root == null) {
			setWarning("Juuri-tiliä ei löytynyt");
			throw new BadFileException();
		}
		map.setRoot(root);
	}

	private String cutTheCurve(BufferedReader br, String line)
			throws IOException, NoBracketException {

		do {

			int index = line.indexOf('{');
			if (index != -1) {
				if (line.length() > index) {
					line = line.substring(index + 1);
					break;
				} else {
					line = br.readLine();
					break;
				}
			}
		} while ((line = br.readLine()) != null);
		if (line == null) {
			setError("Aloitussulkua ei löytynyt");
			throw new NoBracketException();
		}
		return line;
	}

	private void setError(String errorText) {
		errorReport.add(errorText);
	}

	private void setWarning(String warningText) {
		warningReport.add(warningText);
	}

	public String printErrors() {
		if (errorReport.size() == 0 && warningReport.size() == 0) {
			return "Reading the structure finished without warnings or errors.\n";
		}
		String report = "Reading the structure finished with "
				+ errorReport.size() + " errors and " + warningReport.size()
				+ " warnings:";
		for (int i = 0; i < errorReport.size(); i++) {
			report += "\n" + errorReport.get(i);
		}
		for (int i = 0; i < warningReport.size(); i++) {
			report += "\n" + warningReport.get(i);
		}
		return report;
	}
}
