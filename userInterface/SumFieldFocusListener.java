package userInterface;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class SumFieldFocusListener implements FocusListener {

	@Override
	public void focusGained(FocusEvent arg0) {

	}

	@Override
	public void focusLost(FocusEvent ev) {
		if (!ev.isTemporary()) {

			JTextField source = (JTextField) ev.getSource();
			String sumString = source.getText();
			try {
				double sum = Double.parseDouble(sumString);
				source.setForeground(AccountingGUI.fontColor);
				/* CreateNoteFromTable requires also negative values.
				if (sum < 0) {
					source.setText(new Double(-sum).toString());
				}
				*/
			} catch (NumberFormatException e) {
				source.setForeground(AccountingGUI.errorColor);
			}

		}

	}

}
