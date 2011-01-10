package com.t_oster.notenschrank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.t_oster.notenschrank.gui.MainFrame;

public class Notenschrank implements ActionListener{
	
	private MainFrame mainFrame;
	private Notenschrank(){
		this.mainFrame = new MainFrame();
		this.mainFrame.addActionListener(this);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
		new Notenschrank();
	}
	
	private void showScanWizzard(){
		
	}
	
	private void showPrintWizzard(){
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.mainFrame)){
			MainFrame.Action a = MainFrame.Action.values()[e.getID()];
			switch (a){
				case actionPrintClicked:{
					mainFrame.setVisible(false);
					this.showPrintWizzard();
					mainFrame.setVisible(true);
					break;
				}
				case actionScanClicked:{
					mainFrame.setVisible(false);
					this.showScanWizzard();
					mainFrame.setVisible(true);
					break;
				}
				case actionCloseClicked:{
					System.exit(0);
					break;
				}
			}
		}
	}

}
