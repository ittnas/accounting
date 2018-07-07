package userInterface;

import java.awt.Component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import coreClasses.Account;

public class OptionPane extends JDialog implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField accountNameField;
	private JTextField accountDescrField;
	private JButton okButton;
	private JButton cancelButton;

	public OptionPane(Component location) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 4, 2, 4), 0, 0);
		
		JLabel nameLabel = new JLabel("Tilin nimi");
		nameLabel.setOpaque(false);
		nameLabel.setForeground(AccountingGUI.fontColor);
		nameLabel.setFont(AccountingGUI.font);
		
		JLabel descrLabel = new JLabel("Tilin kuvaus");
		descrLabel.setOpaque(false);
		descrLabel.setForeground(AccountingGUI.fontColor);
		descrLabel.setFont(AccountingGUI.font);
		
		accountNameField = new JTextField();
		accountNameField.setOpaque(false);
		accountNameField.setForeground(AccountingGUI.fontColor);
		accountNameField.setFont(AccountingGUI.font);
		
		accountDescrField = new JTextField();
		accountDescrField.setOpaque(false);
		accountDescrField.setForeground(AccountingGUI.fontColor);
		accountDescrField.setFont(AccountingGUI.font);
		
		okButton = new JButton("OK");
		okButton.setForeground(AccountingGUI.fontColor);
		okButton.setFont(AccountingGUI.font);
		okButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setForeground(AccountingGUI.fontColor);
		cancelButton.setFont(AccountingGUI.font);
		cancelButton.addActionListener(this);
		
		panel.add(nameLabel, constraints);
		constraints.gridx++;
		panel.add(descrLabel, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		panel.add(accountNameField, constraints);
		constraints.gridx++;
		panel.add(accountDescrField, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		panel.add(okButton, constraints);
		constraints.gridx++;
		panel.add(cancelButton, constraints);
		
		setContentPane(panel);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(location);
		pack();
		
	}
	
	public Account getAccount() {
		return new Account(accountNameField.getText(), accountDescrField.getText());
	}
	
	public static Account showAddAccountDialog(Component location) {
		OptionPane pane = new OptionPane(location);
		pane.setVisible(true);
		return pane.getAccount();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cancelButton) {
			dispose();
		}
		
	}
}
