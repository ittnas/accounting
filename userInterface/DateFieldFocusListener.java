package userInterface;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;

public class DateFieldFocusListener implements FocusListener {

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!e.isTemporary()) {

			if (e.getSource() instanceof JFormattedTextField) {
				JFormattedTextField source = (JFormattedTextField) e
						.getSource();
				String newValue = source.getText().trim();
				try {
					source.setValue(AccountingGUI.dateFormat
							.format(AccountingGUI.parseDate(newValue)));
					source.setForeground(AccountingGUI.fontColor);
				} catch (NullPointerException ex) {

				} catch (IllegalDateException ex) {
					source.setForeground(AccountingGUI.errorColor);
					// TODO tee jotain ilke‰‰
				}
			}

		}
	}
}
