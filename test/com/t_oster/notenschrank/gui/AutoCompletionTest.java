package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.junit.Test;


public class AutoCompletionTest {
	
	@Test
	public void editableTest() {
		// the combo box (add/modify items if you like to)
		final JComboBox comboBox = new JComboBox(new Object[] { "Ester",
				"Jordi", "Jordina", "Jorge", "Sergi" });
		AutoCompletion.enable(comboBox, true);

		// create and show a window containing the combo box
		JOptionPane.showConfirmDialog(null, comboBox, "EditableTest", JOptionPane.YES_NO_OPTION);
	}
	
	@Test
	public void uneditableTest() {
		// the combo box (add/modify items if you like to)
		final JComboBox comboBox = new JComboBox(new Object[] { "Ester",
				"Jordi", "Jordina", "Jorge", "Sergi" });
		AutoCompletion.enable(comboBox, false);

		// create and show a window containing the combo box
		JOptionPane.showConfirmDialog(null, comboBox, "UneditableTest", JOptionPane.YES_NO_OPTION);
	}
}
