package coreClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import userInterface.AccountingGUI;

public class ExcelNoteReader {

	private String fileName;
	private ArrayList<String> errorReport;
	private ArrayList<String> warningReport;
	private AccountMap map;

	public ExcelNoteReader(String fileName, AccountMap map) {
		this.fileName = fileName;
		errorReport = new ArrayList<String>();
		warningReport = new ArrayList<String>();
		this.map = map;

	}

	public ArrayList<NoteHolder> readNotes() {
		ArrayList<NoteHolder> output = new ArrayList<NoteHolder>();

		String line = null;

		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					continue;
				}
				String[] noteStrings = line.split("\\t");
				if (noteStrings.length == 5) {
					String[] tmp = noteStrings;
					noteStrings = new String[6];
					for (int i = 0; i < tmp.length; i++) {
						noteStrings[i] = tmp[i];
					}
					// Longer description is not in use
					noteStrings[5] = "";
				} else if (noteStrings.length != 6) {
					setError(String.format(
							"Merkintä %s on viallinen ja se ohitetaan.",
							noteStrings[0]));
					continue;
				}
				Account debet = map.getAccount(noteStrings[3]);
				if (debet == null) {
					setWarning(String.format(
							"Merkinnän %s debet-tiliä ei löytynyt. Merkinnän tili oli %s",
							noteStrings[1], noteStrings[3]));
					continue;
				}
				Account credit = map.getAccount(noteStrings[4]);
				if (credit == null) {
					setWarning(String.format(
							"Merkinnän %s credit-tiliä ei löytynyt. Merkinnän tili oli %s",
							noteStrings[1], noteStrings[4]));
					continue;
				}
				Date date = null;
				try {
					date = AccountingGUI.dateFormat.parse(noteStrings[0]);
				} catch (ParseException e) {
					setWarning(String.format(String.format(
							"Merkinnän %s päiväys on virheellinen.",
							noteStrings[1])));
					continue;
				}
				double value = 0;
				try {
					value = Double.parseDouble(noteStrings[2]);
				} catch (NumberFormatException e) {
					setWarning(String.format(String.format(
							"Merkinnän %s arvo on virheellinen.",
							noteStrings[1])));
				}
				output.add(new NoteHolder(value, date, noteStrings[1], debet,
						credit));

			}
			in.close();
		} catch (IOException e) {
			setError("Tiedostoa ei voida avata tai se ei ole olemassa.");
			return null;
		}
		return output;
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

	public int getErrorCount() {
		return errorReport.size() + warningReport.size();
	}

}
