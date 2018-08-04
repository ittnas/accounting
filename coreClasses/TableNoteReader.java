package coreClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import userInterface.AccountingGUI;

public class TableNoteReader {
	
	private File file;
	private ArrayList<String> errorReport;
	private ArrayList<String> warningReport;
	private Account dummy;

	public TableNoteReader(File file) {
		this.file = file;
		errorReport = new ArrayList<String>();
		warningReport = new ArrayList<String>();
		dummy = new Account("dummy", "dummy");
	}
	
	public ArrayList<NoteHolder> readNotes() {
		ArrayList<NoteHolder> output = new ArrayList<NoteHolder>();
		
		
		
		String line = null;
		
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					continue;
				}
				String[] noteStrings = line.split("\\t");
				if(noteStrings.length != 5) {
					setWarning(String.format("Merkintä %d on viallinen ja se ohitetaan.",output.size()+1));
					continue;
				}
				Date date;
				try {

					date = AccountingGUI.dateFormat.parse(noteStrings[0].trim());
				} catch (ParseException e) {
					setWarning(String.format("Merkinnän %d päivämäärä on viallinen.",output.size()+1));
					date = new Date();
				}
				double value;
				try {
					DecimalFormat df = new DecimalFormat("+#,#;-#,#");
					//DecimalFormat df = new DecimalFormat();
					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setDecimalSeparator(',');
					symbols.setGroupingSeparator(' ');
					df.setDecimalFormatSymbols(symbols);
					value = df.parse(noteStrings[1].trim()).doubleValue();
				} catch (ParseException e) {
					setWarning(String.format("Merkinnän %d arvo on viallinen.",
							output.size()+1));
					value = 0;
				}
				String description = noteStrings[3].trim() + " " + noteStrings[4].trim();
				
				output.add(new NoteHolder(value, date, description, dummy, dummy));
			}
		} catch (IOException e) {
			setError("Tiedostoa ei voida avata tai se ei ole olemassa.");
			return null;
		}
		return output;
	}
	
	public int getErrorCount() {
		return errorReport.size() + warningReport.size();
	}
	
	private void setError(String errorText) {
		errorReport.add(errorText);
	}

	private void setWarning(String warningText) {
		warningReport.add(warningText);
	}

	public String printErrors() {
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