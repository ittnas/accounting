package coreClasses;

import java.util.ArrayList;

public class Trace {
	
	public enum TraceType {
		LINE(){
			@Override public boolean timeDep() {
				return true;
			}
		}
			,BAR(){@Override public boolean timeDep() {
				return true;
			}},PIE(){@Override public boolean timeDep() {
				return false;
			}},FILLED(){@Override public boolean timeDep() {
				return true;
			}},AREA(){@Override public boolean timeDep() {
				return true;
			}}
			/*,DIVBAR(){@Override public boolean availWithGraphType(GraphState.GraphType type) {
				switch (type) {
				case TIMEDEP: return true;
				case TIMEINDEP: return true;
				default: return false;
				}
			}}*/
			;
			public abstract boolean timeDep();
	}
	
	public enum MarkerType {
		CROSS,NONE;
	}
	
	private ArrayList<AccountData> accData;
	private TraceType type;
	private MarkerType markerType;
	
	public Trace(ArrayList<AccountData> accData, TraceType traceType, MarkerType markerType) {
		this.accData = accData;
		this.type = traceType;
		this.markerType = markerType;
	}
	
	public Trace() {
		this.accData = new ArrayList<AccountData>();
	}
	
	public void removeAccount(int index) {
		AccountData removable = accData.get(index);
		for(AccountData iterator : accData) {
			if(iterator.getRelative().equals(removable)) {
				iterator.setRelative(null);
			}
		}
		accData.remove(index);
	}
	public void addAccount(int index, AccountData accountD) {
		accData.add(index, accountD);
	}
	/**
	 * Confusing, probably this method should be preferred over getAccountData.
	 * Should be studied more
	 * @return
	 */
	public ArrayList<Account> getAccounts() {
		ArrayList<Account> accounts = new ArrayList<Account>();
		for(AccountData acc : accData) {
			accounts.addAll(acc.getAssociatedAccounts());
		}
		return accounts;
	}
	
	public ArrayList<AccountData> getAccountData() {
		return accData;
	}
	
	public TraceType getTraceType() {
		return type;
	}
}
