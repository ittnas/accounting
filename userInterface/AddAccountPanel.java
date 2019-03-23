package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import coreClasses.Account;

public class AddAccountPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField accountNameField;
	private JTextField accountDescrField;
	private GridBagConstraints constraints;
	private boolean addAsChild;
	//private JTextField accountCurrencyField;
		
	public AddAccountPanel(boolean addAsChild) {
		super();
		this.addAsChild = addAsChild;
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 4, 2, 4), 0, 0);
		
		JLabel nameLabel = new JLabel("Tilin nimi");
		nameLabel.setOpaque(false);
		nameLabel.setForeground(AccountingGUI.fontColor);
		nameLabel.setFont(AccountingGUI.font);
		
		JLabel descrLabel = new JLabel("Tilin kuvaus");
		descrLabel.setOpaque(false);
		descrLabel.setForeground(AccountingGUI.fontColor);
		descrLabel.setFont(AccountingGUI.font);
		
		/**
		JLabel currencyLabel = new JLabel("Tilin valuutta");
		currencyLabel.setOpaque(false);
		currencyLabel.setForeground(AccountingGUI.fontColor);
		currencyLabel.setFont(AccountingGUI.font);
		**/
		
		accountNameField = new JTextField();
		accountNameField.setOpaque(true);
		accountNameField.setForeground(AccountingGUI.fontColor);
		accountNameField.setFont(AccountingGUI.font);
		
		accountDescrField = new JTextField();
		accountDescrField.setOpaque(true);
		accountDescrField.setForeground(AccountingGUI.fontColor);
		accountDescrField.setFont(AccountingGUI.font);
		
		/**
		accountCurrencyField = new JTextField();
		accountCurrencyField.setOpaque(true);
		accountCurrencyField.setForeground(AccountingGUI.fontColor);
		accountCurrencyField.setFont(AccountingGUI.font);
		accountCurrencyField.setText(AccountingGUI.proposedCurrency);
		**/
		add(nameLabel, constraints);
		constraints.gridx++;
		add(descrLabel, constraints);
		//constraints.gridx++;
		//add(currencyLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		add(accountNameField, constraints);
		constraints.gridx++;
		add(accountDescrField, constraints);
		constraints.gridx++;
		//add(accountCurrencyField, constraints);
		//constraints.gridx++;
	}
	
	public void addButton(JButton button) {
		button.setForeground(AccountingGUI.fontColor);
		button.setFont(AccountingGUI.font);
		add(button, constraints);
		constraints.gridx++;
	}
	
	public boolean addAsChild() {
		return addAsChild;
	}
	
	public Account getAccount() {
		return new Account(accountNameField.getText(), accountDescrField.getText());
	}
}
