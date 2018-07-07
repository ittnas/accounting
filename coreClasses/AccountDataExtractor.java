package coreClasses;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import coreClasses.AccountData.RelationType;
import coreClasses.AccountData.ValueType;

import userInterface.Java2sAutoComboBox;

public class AccountDataExtractor implements ActionListener,Comparable<AccountDataExtractor> {
	
	private JLabel accountName;
	private int accountNumber;
	private Java2sAutoComboBox account;
	private Java2sAutoComboBox targetAccount;
	private Java2sAutoComboBox relationType;
	private AccountMap accountMap;
	private Java2sAutoComboBox valueTypeSelector;
	private JButton removeButton;
	private JSpinner levelField;
	private userInterface.TracePanel mainPanel;
	private HashMap<String, AccountDataExtractor> extractors;
	//private static int identifier = 1;
	//private String ident;
	
	public AccountDataExtractor(userInterface.TracePanel mainPanel, JLabel accountName, int accountNumber, Java2sAutoComboBox account,Java2sAutoComboBox targetAccount,
			Java2sAutoComboBox relationType,AccountMap accounts, Java2sAutoComboBox valueTypeSelector, JButton removeButton,JSpinner levelField, HashMap<String, AccountDataExtractor> extractors) {
		//ident = new Integer(identifier).toString();
		//identifier++;
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.account = account;
		this.targetAccount = targetAccount;
		this.relationType = relationType;
		this.accountMap = accounts;
		this.valueTypeSelector = valueTypeSelector;
		this.removeButton = removeButton;
		removeButton.addActionListener(this);
		this.levelField = levelField;
		this.mainPanel = mainPanel;
		this.extractors =extractors;
	}
	
	public ArrayList<AccountData> getAccountData() {
		String accountName = (String)account.getSelectedItem();
		Account pres = accountMap.getAccount(accountName);
		Account targetAcc = getRelativeAccount();
		String relationTypeString =relationType.getSelectedItem().toString();
		RelationType relationValue = RelationType.valueOf(relationTypeString);
		ArrayList<AccountData> dataList = new ArrayList<AccountData>();
		String valueTypeString = (valueTypeSelector.getSelectedItem()).toString();
		ValueType valueType = ValueType.valueOf(valueTypeString);
		int level = (Integer)(levelField.getValue());
		AccountData parentData = new AccountData(pres,valueType,level,targetAcc,relationValue);
		if(relationValue==RelationType.NONE) {
			ArrayList<Account> accountList = parentData.getAssociatedAccounts();
			for(Account child : accountList) {
				dataList.add(new AccountData(child, valueType, 0, null, RelationType.NONE));
			}
		} else {
			dataList.add(parentData);
		}
		return dataList;
	}
	
	private void remove() {
		Container parent = this.accountName.getParent();
		parent.remove(accountName);
		parent.remove(account);
		parent.remove(targetAccount);
		parent.remove(relationType);
		parent.remove(valueTypeSelector);
		parent.remove(removeButton);
		parent.remove(levelField);
	}
	
	public Account getMainAccount() {
		String accountName = (String)account.getSelectedItem();
		Account pres = accountMap.getAccount(accountName);
		return pres;
	}
	
	public Account getRelativeAccount() {
		String selection =  targetAccount.getSelectedItem().toString();
		AccountDataExtractor extr = extractors.get(selection);
		return extr.getMainAccount();
	}
	
	public void setRelationType(RelationType type) {
		relationType.setSelectedItem(type);
	}
	
	public void setRelativeAccount(AccountDataExtractor newExtractor) {
		targetAccount.setSelectedItem(newExtractor);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		remove();
		mainPanel.removeExtractor(this);
		mainPanel.updateUI();
	}
	
	public void reduceNumber() {
		extractors.remove(this.toString());
		accountNumber--;
		accountName.setText("Tili " + accountNumber);
		if(extractors.containsKey(this.toString())) {
			extractors.remove(this.toString());
		}
		extractors.put(this.toString(), this);
		
	}
	
	
	@Override
	public String toString() {
		return "Tili " + accountNumber;
	}

	public void updateRelativeValues(
			HashMap<String, AccountDataExtractor> extrMap) {
		//int index = targetAccount.getSelectedIndex();
		String temp = (String)(targetAccount.getSelectedItem());
		ArrayList<String> tempList = new ArrayList<String>();
		for(AccountDataExtractor iterator : extrMap.values()) {
			tempList.add(iterator.toString());
		}
		//ArrayList<String> tempList = new ArrayList<String>(extrMap.keySet());
		Collections.sort(tempList);
		targetAccount.setDataList(tempList);
		if(extrMap.containsKey(temp)) {
			targetAccount.setSelectedItem(temp);
		} else if(targetAccount.getDataList().size() != 0) {
			targetAccount.setSelectedIndex(0);
		}
	}

	@Override
	public int compareTo(AccountDataExtractor o) {
		return this.toString().compareTo(o.toString());
	}
}
