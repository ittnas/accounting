package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import coreClasses.Account;
import coreClasses.AccountMap;
import coreClasses.AccountTree;

public class MoveAccountPanel extends JPanel implements ActionListener {
	
	private GridBagConstraints constraints;
	private AccountTree accountTree;
	private Account sourceAccount;
	private AccountMap accountMap;
	private Java2sAutoComboBox parentAccountComboBox;
	private JButton okButton;
	private JButton cancelButton;
	private AccountingGUI GUI;

	public MoveAccountPanel(Account sourceAccount,AccountTree accountTree, AccountMap accountMap,AccountingGUI GUI) {
		super();
		this.sourceAccount = sourceAccount;
		this.accountTree = accountTree;
		this.accountMap = accountMap;
		this.GUI = GUI;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 4, 2, 4), 0, 0);
		
FocusListener comboboxFL = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(e.getSource() == parentAccountComboBox.getEditor().getEditorComponent()) {
					parentAccountComboBox.getEditor().selectAll();
					//JTextField editor = (JTextField)parentAccountComboBox.getEditor().getEditorComponent();
					//editor.setCaretPosition(0);
				}
			}
		};
		
		JLabel parentAccountLabel = new JLabel("Uusi isäntätili");
		parentAccountLabel.setFont(AccountingGUI.font);
		parentAccountLabel.setOpaque(false);
		parentAccountLabel.setForeground(AccountingGUI.fontColor);
		
		ArrayList<String> accountNames = accountMap.getAccountNames(false);
		parentAccountComboBox = new Java2sAutoComboBox(accountNames);
		parentAccountComboBox.setEditable(true);
		//parentAccountComboBox.addActionListener(this);
		parentAccountComboBox.setFont(AccountingGUI.font);
		parentAccountComboBox.setForeground(AccountingGUI.fontColor);
		parentAccountComboBox.getEditor().getEditorComponent().addFocusListener(comboboxFL);
		
		okButton = new JButton("OK");
		okButton.setForeground(AccountingGUI.fontColor);
		okButton.setFont(AccountingGUI.font);
		okButton.addActionListener(this);
		
		cancelButton = new JButton("Peru");
		cancelButton.setForeground(AccountingGUI.fontColor);
		cancelButton.setFont(AccountingGUI.font);
		cancelButton.addActionListener(this);
		
		add(parentAccountLabel,constraints);
		constraints.gridx++;
		add(cancelButton,constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		add(parentAccountComboBox,constraints);
		constraints.gridx++;
		add(okButton,constraints);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			Account target = accountMap.getAccount(parentAccountComboBox.getSelectedItem()
					.toString().trim());
			if (target == null || sourceAccount == null || sourceAccount.equals(target) || accountMap.getRoot() == sourceAccount) {
				String message = "Tiliä ei voida siirtää.";
				GUI.updateStatus(message);
			} else {
				target.addChild(sourceAccount);
				String message = "Tili " + sourceAccount.getName() + " siirreettiin tilin " + target.getName() + " alle.";
				GUI.updateStatus(message);
				accountTree.expandAll();
				accountTree.updateUI();
				
				
				SwingUtilities.getWindowAncestor(this).dispose();
			}
		}
		
		if(e.getSource() == cancelButton) {
			SwingUtilities.getWindowAncestor(this).dispose();
			String message = "Tilin siirtäminen peruttu.";
			GUI.updateStatus(message);
		}
	}
}
