package coreClasses;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.management.relation.RelationType;

import org.jfree.chart.plot.Marker;

import coreClasses.GraphState.StepType;

import userInterface.AccountingGUI;

public class GraphReader {

	private String fileName;
	private ArrayList<String> errorReport;
	private ArrayList<String> warningReport;
	private AccountMap map;

	public GraphReader(String fileName,AccountMap map) {
		this.fileName = fileName;
		this.map = map;
		errorReport = new ArrayList<String>();
		warningReport = new ArrayList<String>();
	}
	
	public ArrayList<GraphState> readGraph() {
		String line = null;
		ArrayList<GraphState> state = new ArrayList<GraphState>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				if(line.contains("#GraphState")) {
					GraphState readState = readState(line,br);
					state.add(readState);
				}
			}
			in.close();
		} catch (IOException e) {
			setError("Tiedostoa ei voida avata tai se ei ole olemassa.");
			return null;
		} catch (BadFileException e) {
			setError("Tiedosto on korruptoitunut");
		}

		return state;
		
	}
	
	private GraphState readState(String line, BufferedReader br) throws BadFileException, IOException {
		GraphState readState = null;
		ArrayList<Trace> traces = null;
		boolean tracesRead = false;
		boolean attributesRead = false;
		Date startDate = null;
		Date endDate = null;
		int interval = 0;
		StepType stepType = null;
		ArrayList<String> parameterStrings = null;
		while ((line = br.readLine()) != null && !line.contains("#END")) {
			if (line.contains("#Traces")) {
				traces = readTraces(br, line);
				tracesRead = true;
			} else	if (line.contains("#Parameters")) {
				parameterStrings = readParameters(br, line);
				if(!parameterStrings.get(0).equals("null")) {
				startDate = new Date(Long.parseLong(parameterStrings.get(0)));
				}
				if(!parameterStrings.get(1).equals("null")) {
					endDate = new Date(Long.parseLong(parameterStrings.get(1)));
				}
				interval = Integer.parseInt(parameterStrings.get(2));
				stepType = StepType.valueOf(parameterStrings.get(3));
				attributesRead = true;
			}
		}
		if(tracesRead && attributesRead) {
			
			readState = new GraphState(startDate, endDate, interval, stepType, traces);
			return readState;
		} else {
			setError("Viallinen tiedosto.");
			throw new BadFileException();
		}
		
	}
	
	public ArrayList<String> readParameters(BufferedReader br, String line) throws IOException {
		//CHEATING
		int i = 0;
		ArrayList<String> parameters = new ArrayList<String>();
		while((line = br.readLine()) != null && !line.contains("}")) {
			if(!line.trim().equals("")) {
				parameters.add(line.trim());
			}
		}
		return parameters;
	}
	
	private ArrayList<Trace> readTraces(BufferedReader br, String line)
	throws IOException, BadFileException {
try {
	line = cutTheCurve(br, line);
} catch (NoBracketException e) {
	return null;
}
ArrayList<Trace> traces = new ArrayList<Trace>();
boolean end = false;
boolean accountStart = false;
do {
	int index = line.indexOf('}');
	if (index != -1) {
		line = line.substring(0, index);
		end = true;
	}
	line = line.trim();
	if (line.equals("")) {
		if(end) {
			break;
		} else {
		continue;
		}
	}
	index = line.indexOf('{');
	String[] words;
	if(index != -1) {
		words = line.substring(0,index).split(";");
		accountStart = true;
	} else {
		words = line.split(";");
	}
	String name = null;
	String traceTypeString = null;
	Trace.TraceType traceType = null;

	if (words.length != 1) {
		setError(String.format("Virheellinen ura %s.", words[0]));
	} else {
		traceTypeString = words[0];
		traceType = Trace.TraceType.valueOf(traceTypeString);
	}
	
	ArrayList<AccountData> accData;
	
	// If no account brackets exist this will malfunction.
		if(accountStart) {
			accData =readAccounts(br,line);
		} else {
			try {
			line = cutTheCurve(br, line);
			accData =readAccounts(br, line);
			} catch (NoBracketException e) {
				return null;
			}
		}
		Trace trace = new Trace(accData,traceType,Trace.MarkerType.NONE);
		traces.add(trace);
} while (((line = br.readLine()) != null) && !end);
return traces;
}
	
	private void setError(String errorText) {
		errorReport.add(errorText);
	}

	private void setWarning(String warningText) {
		warningReport.add(warningText);
	}
	
	private ArrayList<AccountData> readAccounts(BufferedReader br, String line) throws IOException, BadFileException {
		ArrayList<AccountData> accData = new ArrayList<AccountData>();
		boolean end = false;
		do {
			int index = line.indexOf('}');
			if (index != -1) {
				line = line.substring(0, index);
				end = true;
			}
			line = line.trim();
			if (line.equals("")) {
				if(end) {
					break;
				} else {
				continue;
				}
			}
			String[] words = line.split(";");
			String mainAccountName = null;
			String targetAccountName = null;
			String levelString = null;
			String relationTypeString = null;
			String valueTypeString = null;

			if (words.length != 5) {
				setError(String.format("Virheellinen tili %s.", words[0]));
			} else {
				mainAccountName = words[0];
				targetAccountName = words[1];
				levelString = words[2];
				relationTypeString = words[3];
				valueTypeString = words[4];
				Account mainAccount = null;
				Account targetAccount = null;
				int level = 0;
				AccountData.RelationType relType = null;
				AccountData.ValueType valType = null;
				if(map.containsKey(mainAccountName)) {
					mainAccount = map.getAccount(mainAccountName);
				}
				if(map.containsKey(targetAccountName)) {
					targetAccount = map.getAccount(targetAccountName);
				}
				try {
					level = Integer.parseInt(levelString);
				} catch (NumberFormatException e) {
					// Bad level
				}
				
				AccountData data = new AccountData(mainAccount, valType, level, targetAccount, relType);
				accData.add(data);
			}
		} while (((line = br.readLine()) != null) && !end);

		return accData;
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
}
