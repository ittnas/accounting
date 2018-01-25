package coreClasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphState {
	
	public enum StepType {
		DAY() {
			@Override
			public Date firstDay(Date start){
				cal.setTime(start);
				cal.clear(Calendar.HOUR_OF_DAY);
				cal.clear(Calendar.MINUTE);
				cal.clear(Calendar.SECOND);
				cal.clear(Calendar.MILLISECOND);
				return cal.getTime();
			}
			@Override
			public Date lastDay(Date end) {
				cal.setTime(end);
				cal.clear(Calendar.HOUR_OF_DAY);
				cal.clear(Calendar.MINUTE);
				cal.clear(Calendar.SECOND);
				cal.clear(Calendar.MILLISECOND);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				return cal.getTime();
			}
			@Override
			public Date advance(Date date, int interval) {
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_MONTH, interval);
				return cal.getTime();
			}
			@Override
			public String getFormat() {
				return "dd.MM.yy";
			}
			@Override
			public long getPeriodLength() {
				cal.setTime(new Date(0));
				cal.add(Calendar.DAY_OF_MONTH, 1);
				return cal.getTimeInMillis();
			}
		},MONTH() {
			@Override
			public Date firstDay(Date start) {
				cal.setTime(start);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				return cal.getTime();
		}
			@Override
			public Date lastDay(Date end) {
				cal.setTime(end);
				cal.set(Calendar.DAY_OF_MONTH, 2);
				return cal.getTime();
		}
			@Override
			public Date advance(Date date, int interval) {
				cal.setTime(date);
				cal.add(Calendar.MONTH, interval);
				return cal.getTime();
			}
			@Override
			public String getFormat() {
				return "MM.yyyy";
			}
			@Override
			public long getPeriodLength() {
				cal.setTime(new Date(0));
				cal.add(Calendar.MONTH, 1);
				return cal.getTimeInMillis();
			}
			},YEAR() {
				@Override
				public Date firstDay(Date start) {
					cal.setTime(start);
					cal.set(Calendar.MONTH, 1);
					return cal.getTime();
				}
				@Override
				public Date lastDay(Date end) {
					cal.setTime(end);
					cal.set(Calendar.MONTH, 2);
					return cal.getTime();
				}
				@Override
				public Date advance(Date date, int interval) {
					cal.setTime(date);
					cal.add(Calendar.YEAR, interval);
					return cal.getTime();
				}
				@Override
				public String getFormat() {
					
					return "yyyy";
				}
				@Override
				public long getPeriodLength() {
					cal.setTime(new Date(0));
					cal.add(Calendar.YEAR, 1);
					return cal.getTimeInMillis();
				}
			};
	private static Calendar cal = Calendar.getInstance();		

	public abstract Date firstDay(Date start);

	public abstract Date lastDay(Date endDate);

	public abstract Date advance(Date date,int stepInverval);
	
	public abstract String getFormat();
	
	public abstract long getPeriodLength();
	}
	
	private Date startDate;
	private Date endDate;
	private int interval;
	private ArrayList<Trace> traces;
	private StepType stepType;
	
	public GraphState(Date startDate, Date endDate, int interval, StepType stepType, ArrayList<Trace> traces) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.interval = interval;
		this.traces = traces;
		this.stepType = stepType;
	}
	
	public void addTrace(int index, Trace trace) {
		traces.add(index, trace);
	}
	
	public ArrayList<Trace> getTraceList() {
		return traces;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public int getStepInverval() {
		return interval;
	}
	
	public StepType getStepType() {
		return stepType;
	}
	
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat(stepType.getFormat());
		String sd = null;
		String ed = null;
		if(startDate != null) {
			sd = dateFormat.format(startDate);
		} else {
			sd = "null";
		}
		if(endDate != null) {
			ed = dateFormat.format(endDate);
		} else {
			ed = "null";
		}
		
		String output = "Time from " + sd + " to " + ed + "."
		+ " Step type is " + this.getStepType().toString() + " and step interval " + this.getStepInverval();
		return output;
	}
	
}
