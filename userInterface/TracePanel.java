package userInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import coreClasses.AccountData;
import coreClasses.AccountDataExtractor;
import coreClasses.Trace;
import coreClasses.AccountData.RelationType;
import coreClasses.AccountData.ValueType;
import coreClasses.Trace.MarkerType;
import coreClasses.Trace.TraceType;

public class TracePanel extends JPanel implements ActionListener, ItemListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int addingPosition = 1;
	private int nbrAccounts = 0;
	private AccountingGUI main;
	private JButton addAccountButton;
	private JLabel traceNumLabel;
	private JButton removeTraceButton;
	public static ImageIcon removeIcon = new ImageIcon("images/removeIcon.png");
	public static ImageIcon addIcon = new ImageIcon("images/plus.png");
	private GraphPanel actionPanel;
	private HashMap<String,AccountDataExtractor> accountExtractors;
	
	private int traceNbr;
	private JComboBox traceTypeSelecter;
	private AccountData defData;
	
	TracePanel(AccountingGUI listHolder,int traceNbr,GraphPanel actionListenerPanel,Trace trace) {
		super();
		
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);
		this.main = listHolder;
		this.actionPanel = actionListenerPanel;
		this.traceNbr = traceNbr;
		accountExtractors = new HashMap<String,AccountDataExtractor>();
		defData = new AccountData(main.getAccountMap().getRoot(), ValueType.ACCUMULATE, 0, null, RelationType.NONE);
		GridBagConstraints traceConst = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		removeTraceButton = new JButton(removeIcon);
		removeTraceButton.addActionListener(this);
		removeTraceButton.setForeground(null);
		removeTraceButton.setOpaque(false);
		removeTraceButton.setPreferredSize(new Dimension(removeIcon.getIconHeight(), removeIcon.getIconWidth()));
		add(removeTraceButton,traceConst);
		traceConst.gridx = 1;
		String traceNumber = "Ura " + traceNbr;
		traceNumLabel = new JLabel(traceNumber);
		traceNumLabel.setForeground(AccountingGUI.fontColor);
		traceNumLabel.setFont(AccountingGUI.font);
		traceNumLabel.setOpaque(false);
		add(traceNumLabel,traceConst);
		traceConst.gridx++;
		traceTypeSelecter = new JComboBox(TraceType.values());
		traceTypeSelecter.setForeground(AccountingGUI.fontColor);
		traceTypeSelecter.setFont(AccountingGUI.font);
		add(traceTypeSelecter,traceConst);
		traceConst.gridx++;
		addAccountButton = new JButton("Lisää tili");
		addAccountButton.setForeground(AccountingGUI.fontColor);
		addAccountButton.setFont(AccountingGUI.font);
		addAccountButton.addActionListener(this);
		add(addAccountButton,traceConst);
		
		
		traceTypeSelecter.setSelectedItem(trace.getTraceType().toString());
		for(AccountData acc :trace.getAccountData()) {
			addAccount(acc);
		}
		
	}
	private void addAccount(AccountData data) {
GridBagConstraints accConst = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		Java2sAutoComboBox accountSelector = new Java2sAutoComboBox(main.getAccountMap().getAccountNames(false));
		main.addComboboxToUpdateList(accountSelector);
		if(data.getTargetAccount() != null) {
		accountSelector.setSelectedItem(data.getTargetAccount().getName());
		}
		ArrayList<String> relativeList = new ArrayList<String>();
		for(AccountDataExtractor iterator : accountExtractors.values()) {
			relativeList.add(iterator.toString());
		}
		
		Collections.sort(relativeList);
		Java2sAutoComboBox relativeSelector = new Java2sAutoComboBox(relativeList);
		RelationType[] relArr = RelationType.values();
		List<RelationType> relList = Arrays.asList(relArr);
		Java2sAutoComboBox relationTypeSelector = new Java2sAutoComboBox(relList);
		//relationTypeSelector.setSelectedItem(RelationType.NONE);
		if(data.getRelationType() != null) {
			relationTypeSelector.setSelectedItem(data.getRelationType().toString());
		}
		SpinnerModel levelModel = new SpinnerNumberModel(0, 0, 100, 1);
		JSpinner levelSelector = new JSpinner(levelModel);
		levelSelector.setForeground(AccountingGUI.fontColor);
		levelSelector.setFont(AccountingGUI.font);
		if(data.getLevel() >= 0 && data.getLevel() <= 100) {
			levelSelector.setValue(data.getLevel());
		}
		ValueType[] valArr = ValueType.values();
		List<ValueType> valList = Arrays.asList(valArr);
		Java2sAutoComboBox valueTypeSelector = new Java2sAutoComboBox(valList);
		if(data.getValueType() != null && valueTypeSelector.getDataList().contains(data.getValueType().toString())) {
			valueTypeSelector.setSelectedItem(data.getValueType().toString());
		}
		nbrAccounts++;
		accConst.gridy = addingPosition + nbrAccounts;
		accConst.gridwidth = 1;
		accConst.gridx = 0;
		JLabel accountNumberLabel = new JLabel("Tili " + (nbrAccounts));
		accountNumberLabel.setForeground(AccountingGUI.fontColor);
		accountNumberLabel.setFont(AccountingGUI.font);
		accountNumberLabel.setOpaque(false);
		
		JButton removeButton = new JButton(removeIcon);
		removeButton.setPreferredSize(new Dimension(removeIcon.getIconHeight(),removeIcon.getIconWidth()));
		add(removeButton,accConst);
		accConst.gridx++;
		add(accountNumberLabel,accConst);
		accConst.gridx++;
		accConst.gridwidth = 2;
		add(accountSelector,accConst);
		accConst.gridwidth = 1;
		accConst.gridx++;
		accConst.gridx++;
		add(relativeSelector,accConst);
		accConst.gridx++;
		add(levelSelector,accConst);
		accConst.gridx++;
		add(relationTypeSelector,accConst);
		accConst.gridx++;
		add(valueTypeSelector,accConst);
		
		AccountDataExtractor extr = new AccountDataExtractor(this, accountNumberLabel, nbrAccounts, accountSelector, relativeSelector, relationTypeSelector, main.getAccountMap(), valueTypeSelector, removeButton, levelSelector,accountExtractors);
		addExtractor(extr);
		
		//revalidate();
		updateUI();
	}
	
	private void addAccount() {
		addAccount(defData);
	}
	
	public void reduceTraceNumber() {
		traceNbr--;
		traceNumLabel.setText("Ura" + new Integer(traceNbr).toString());
	}
	
	public int getNumber() {
		return traceNbr;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == removeTraceButton) {
			actionPanel.removeTrace(this);
		}
		if(e.getSource() == addAccountButton) {
			addAccount();
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}
	
	/**
	 * Removes the extractor from accExtractros map and also removes the component in the TracePanel. 
	 * The traceNmbr of all extractors above the one to be removed is modified. 
	 * The values in other extractors relative account boxes are also updated.
	 * @param extr Extractor to be removed
	 */
	public void removeExtractor(AccountDataExtractor extr) {
		//The GridbagLayout doesn't destroy unused positions
		addingPosition++;
		nbrAccounts--;
		ArrayList<AccountDataExtractor> extrList = new ArrayList<AccountDataExtractor>(accountExtractors.values());
		Collections.sort(extrList);
		//Index of extractor to be removed
		int index = extrList.indexOf(extr);
		
		if(index == 0) {
			accountExtractors.remove(extr.toString());
		} else {
			if(index == extrList.size() -1) {
				accountExtractors.remove(extr.toString());
			}
		}
		// Reduce the account number of all extractors after the specified one.
		//This actually also replaces the extractor with the old account number
		for(int i = index + 1; i < extrList.size(); i++) {
			extrList.get(i).reduceNumber();
		}
		//Also the last extractor has to be destroyed (reduceNumber() is not called)
		
		//accountExtractors.remove(extr.toString());
		extrList = new ArrayList<AccountDataExtractor>(accountExtractors.values());
		Collections.sort(extrList);
		//accountExtractors.remove(extrList.get(extrList.size()-1).toString());
		
		for(AccountDataExtractor iterator : extrList) {
			iterator.updateRelativeValues(accountExtractors);
		}
	}
	
	public void addExtractor(AccountDataExtractor extr) {
		accountExtractors.put(extr.toString(), extr);
		for(AccountDataExtractor iterator : accountExtractors.values()) {
			iterator.updateRelativeValues(accountExtractors);
		}
	}
	
	public Trace getTrace() {
		
		ArrayList<AccountData> data = new ArrayList<AccountData>();
		for(AccountDataExtractor iterator :accountExtractors.values()) {
			data.addAll(iterator.getAccountData());
		}
		String traceTypeString = traceTypeSelecter.getSelectedItem().toString();
		TraceType traceType = TraceType.valueOf(traceTypeString);
		Trace trace = new Trace(data, traceType, MarkerType.NONE);
		return trace;
	}

}
