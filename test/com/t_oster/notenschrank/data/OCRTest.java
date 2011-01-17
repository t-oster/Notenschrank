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
import org.junit.Test;

import com.t_oster.notenschrank.gui.PreviewPanel;

public class OCRTest {
	
	
	
	@Test
	public void testMatch(){
		assertEquals(100,OCR.match("helloworld", "hellhelloworld"));
		assertEquals(100,OCR.match("world hello","##'234hellohelloworld341234",true));
		assertEquals(0,OCR.match("helloworld",""));
		assertEquals(100,OCR.match("a","#';4234nsdfsfalhsdfsf"));
		int i= OCR.match("He is a Pirate", "HE’S A PIRATE ",true);
		System.out.println("I:"+i);
		assertTrue(i>50);
		i= OCR.match("The Time Warp", "HE’S A PIRATE ",true);
		System.out.println("I:"+i);
		assertTrue(i<50);
	}
	
	@Test
	public void testOCRwithGUI() throws IOException{
		Box testPanel = Box.createVerticalBox();
		testPanel.setPreferredSize(new Dimension(300,400));
		final PreviewPanel pw = new PreviewPanel(testPanel);
		Sheet s = Archive.getInstance().getSheets(Archive.getInstance().getAvailableSongs()[0])[0];
		System.out.println("Guessed name: "+OCR.getStringsInImage(s.getPreview(new Dimension(25,0), new Dimension(50,20), new Dimension(500,200))));
		
		pw.showSheet(s);
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
