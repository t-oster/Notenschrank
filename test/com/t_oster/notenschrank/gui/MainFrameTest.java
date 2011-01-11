package com.t_oster.notenschrank.gui;

import org.junit.Test;

import com.t_oster.notenschrank.gui.MainFrame;

import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrameTest {
	@Test
	public void testActions(){
		MainFrame m = new MainFrame();
		m.setVisible(false);
		m.addActionListener(new ActionListener(){
			private int i=0;
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(i){
				case 0: assertEquals(e.getID(),MainFrame.Action.actionScanClicked.ordinal());
				i++;
				break;
				case 1: assertEquals(e.getID(),MainFrame.Action.actionPrintClicked.ordinal());
				i++;
				break;
				case 2: assertEquals(e.getID(),MainFrame.Action.actionCloseClicked.ordinal());
				i++;
				break;
				}
			}
		});
	}
}
