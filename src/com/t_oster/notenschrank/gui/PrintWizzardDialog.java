package com.t_oster.notenschrank.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.SettingsManager;

public class PrintWizzardDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2581319034184146056L;
	private JButton bPrintOne;
	private JButton bPrintSong;
	private JButton bPrintVoice;
	
	public PrintWizzardDialog(JFrame parent){
		super(parent, "Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		bPrintOne = new JButton("Eine Stimme für ein Stück drucken");
		bPrintSong = new JButton("Einen kompletten Satz drucken");
		bPrintVoice = new JButton("Eine komplette Mappe drucken");
		
		JPanel b = new JPanel();
		b.setLayout(new GridLayout(0,1));
		b.add(bPrintOne);
		b.add(bPrintSong);
		b.add(bPrintVoice);
		this.setContentPane(b);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void showDialog(){
		this.setModal(true);
		this.setVisible(true);
	}
}
