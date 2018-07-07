package coreClasses;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import userInterface.AccountingGUI;

public class GraphWriter {

	private String fileName;
	private ArrayList<GraphState> states;
	private ArrayList<String> errors;
	private int errorCount;
	
	public GraphWriter(String fileName, ArrayList<GraphState> states) {
		this.fileName = fileName;
		this.states = states;
		errors = new ArrayList<String>();
	}
	
	public void writeState() {
		try {
            java.io.FileWriter fstream = new java.io.FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            for(GraphState state : states) {
            	out.write("#GraphState\n");
            	writeParameters(out,state);
                writeTraces(out,state);
                out.write("#END\n");
            }
            out.close();
        } catch (IOException e) {
            setError("Can't save the file" + fileName + ".\n");
        }
	}
	
	   private void writeTraces(BufferedWriter out, GraphState state) throws IOException {
	    	out.write("#Traces {\n");
			for(Trace trace : state.getTraceList()) {
				String traceType = trace.getTraceType().toString();
				
				
				out.write(traceType);
				out.write("\n{\n");
				for(AccountData data : trace.getAccountData()) {
					String relativeAccountName = null;
					Account acc = data.getRelative();
					if(acc != null) {
						relativeAccountName = acc.getName();
					} else {
						relativeAccountName = "null";
					}
					out.write(String.format("%s;%s;%s;%s;%s\n", data.getTargetAccount().getName(),relativeAccountName,Integer.toString(data.getLevel()),data.getRelationType().toString(),data.getValueType().toString()));
				}
				out.write("}\n");
			}
			out.write("}\n\n");
		}
	   
	   private void writeParameters(BufferedWriter out, GraphState state) throws IOException {
		   out.write("#Parameters {\n");
		   String startString = null;
		   if(state.getStartDate() != null) {
		   //startString = AccountingGUI.dateFormat.format(state.getStartDate());
			   startString = Long.toString(state.getStartDate().getTime());
		   } else {
			   startString = "null";
		   }
		   String endString = null;
		   if(state.getEndDate() != null) {
			   //endString = AccountingGUI.dateFormat.format(state.getEndDate());
			   endString = Long.toString(state.getEndDate().getTime());
		   } else {
			   endString = "null";
		   }
		   
		   String stepValue = Integer.toString(state.getStepInverval());
		   String stepType = state.getStepType().toString();
		   out.write(String.format("%s\n%s\n%s\n%s\n}\n", startString,endString,stepValue,stepType));
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
}
