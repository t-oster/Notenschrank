package com.t_oster.notenschrank.data;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.After;
import org.junit.Test;

import com.t_oster.notenschrank.gui.PreviewPanel;

public class OCRTest {
	@Test
	public void testOCRwithGUI() throws IOException{
		Box testPanel = Box.createVerticalBox();
		testPanel.setPreferredSize(new Dimension(300,400));
		final PreviewPanel pw = new PreviewPanel(testPanel);
		pw.showSheet(Archive.getInstance().getSheets(Archive.getInstance().getAvailableSongs()[1])[0]);
		final JLabel found = new JLabel("making OCR");
		testPanel.add(pw);
		testPanel.add(found);
		testPanel.validate();
		pw.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(pw) && e.getID()==PreviewPanel.IMAGE_RELOADED){
					System.out.println("starting ocr...");
					Image i = pw.getShownImage();
					String t = OCR.getStringsInImage(i);
					System.out.println("Got: "+t);
					found.setText(t);
				}
			}
			
		});
		JOptionPane.showConfirmDialog(null, testPanel);
	}
}
