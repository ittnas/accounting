package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCurrencyPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints constraints;
	private JTextField currencyNameField;

	public AddCurrencyPanel() {
		super();
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 4, 2, 4), 0, 0);
		
		JLabel nameLabel = new JLabel("Valuutta");
		nameLabel.setOpaque(false);
		nameLabel.setForeground(AccountingGUI.fontColor);
		nameLabel.setFont(AccountingGUI.font);
		
		currencyNameField = new JTextField();
		currencyNameField.setOpaque(true);
		currencyNameField.setForeground(AccountingGUI.fontColor);
		currencyNameField.setFont(AccountingGUI.font);
		
		add(nameLabel, constraints);
		constraints.gridx++;
		constraints.gridx = 0;
		constraints.gridy++;
		add(currencyNameField, constraints);
		constraints.gridx++;
	}
	
	public void addButton(JButton button) {
		button.setForeground(AccountingGUI.fontColor);
		button.setFont(AccountingGUI.font);
		add(button, constraints);
		constraints.gridx++;
	}
	
	public String getCurrency() {
		return currencyNameField.getText().trim();
	}
	
	public void setInvalidCurrency() {
		currencyNameField.setForeground(AccountingGUI.errorColor);
	}
}
