package com.t_oster.notenschrank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.gui.MainFrame;
import com.t_oster.notenschrank.gui.PrintWizzardDialog;
import com.t_oster.notenschrank.gui.SortingDialog;

public class Notenschrank implements ActionListener{
	
	private MainFrame mainFrame;
	private boolean running=true;
	public Notenschrank(){
		if (!SettingsManager.getInstance().getArchivePath().exists()){
			if (JOptionPane.showConfirmDialog(null, "Fehler: Der Archivordner '"+SettingsManager.getInstance().getArchivePath()+"' wurde nicht gefunden\n"
					+"Soll ein neuer angelegt werden?", "Fehler", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
				System.exit(0);
			}
			else{
				if (!SettingsManager.getInstance().getArchivePath().mkdirs()){
					JOptionPane.showMessageDialog(null, "Fehler: Konnte Archivordner nicht anlegen.");
					System.exit(1);
				}
			}
		}
		if (!SettingsManager.getInstance().getStackPath().exists()){
			if (JOptionPane.showConfirmDialog(null, "Fehler: Der Scannerordner '"+SettingsManager.getInstance().getStackPath()+"' wurde nicht gefunden\n"
					+"Soll ein neuer angelegt werden?", "Fehler", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
				System.exit(0);
			}
			else{
				if (!SettingsManager.getInstance().getStackPath().mkdirs()){
					JOptionPane.showMessageDialog(null, "Fehler: Konnte Scannerordner nicht anlegen.");
					System.exit(1);
				}
			}
		}
		this.mainFrame = new MainFrame();
		this.mainFrame.addActionListener(this);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't load system style. running in java style");
		}
		Notenschrank ns = new Notenschrank();
		while(ns.running){
			try{
				synchronized(ns.mainFrame){
					ns.mainFrame.wait();
				}
			}
			catch(InterruptedException e){
				//nothing
			}
		}
		//System.exit(0);
	}
	
	private void showScanWizzard(){
		new SortingDialog(mainFrame).showDialog();
		//sortingFrame.addActionListener(this);
	}
	
	private void showPrintWizzard(){
		new PrintWizzardDialog(mainFrame).showDialog();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.mainFrame)){
			MainFrame.Action a = MainFrame.Action.values()[e.getID()];
			switch (a){
				case actionPrintClicked:{
					//mainFrame.setVisible(false);
					this.showPrintWizzard();
					//mainFrame.setVisible(true);
					break;
				}
				case actionScanClicked:{
					//mainFrame.setVisible(false);
					this.showScanWizzard();
					//mainFrame.setVisible(true);
					break;
				}
				case actionCloseClicked:{
					this.running=false;
					this.mainFrame.dispose();
					synchronized(this.mainFrame){
						this.mainFrame.notify();
					}
					break;
				}
			}
		}
	}

}
