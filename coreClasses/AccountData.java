package coreClasses;

import java.util.ArrayList;

public class AccountData {
	
	public enum ValueType {
		ACCUMULATE,DIFFERENCE
	}
	public enum RelationType {
		MULT { @Override
			public String toString() {
			return "MULT";
		}
			
		} ,SUM,SUB,DIV,NONE
	}
	
	private int level;
	private Account relative;
	private RelationType relationType;
	private ValueType valueType;
	private Account targetAccount;
	
	public AccountData(Account targetAccount, ValueType valueType, int level, Account relative, RelationType relationType) {
		this.level = level;
		this.relative = relative;
		this.relationType = relationType;
		this.valueType = valueType;
		this.targetAccount = targetAccount;
	}
	
	public boolean isMainAccount() {
		if (relative == null) {
			return true;
		} else return false;
	}
	public ArrayList<Account> getAssociatedAccounts() {
		if(level == 0) {
			ArrayList<Account> output = new ArrayList<Account>();
			output.add(targetAccount);
			return output;
		} else {
			return targetAccount.getChildren(level-1);
		}
	}
	public Account getTargetAccount() {
		return targetAccount;
	}
	
	public Account getRelative() {
		return relative;
	}

	public void setRelative(Account data) {
		relative = data;
	}
	
	public ValueType getValueType() {
		return valueType;
	}
	
	public RelationType getRelationType() {
		return relationType;
	}
	
	public int getLevel() {
		return level;
	}
}
