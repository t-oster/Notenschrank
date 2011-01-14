package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.Test;

import com.t_oster.notenschrank.data.Archive;


public class SettingsDialogTest {
	
	@Test
	public void testSettingsDialog(){
		SettingsDialog.showDialog(null, "Test");
	}
}
