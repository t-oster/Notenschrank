package com.t_oster.notenschrank.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;

public class PrintWizzardDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2581319034184146056L;
	private JButton bPrintOne;
	private JButton bPrintSong;
	private JButton bPrintVoice;
	private JFrame parent;
	
	public PrintWizzardDialog(JFrame parent){
		super(parent, "Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		this.parent=parent;
		bPrintOne = new JButton("Eine Stimme für ein Stück drucken");
		bPrintSong = new JButton("Einen kompletten Satz drucken");
		bPrintVoice = new JButton("Eine komplette Mappe drucken");
		
		bPrintOne.addActionListener(this);
		
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
		if (Archive.getInstance().getAvailableSongs().length==0){
			JOptionPane.showMessageDialog(parent, "Es wurden noch keine Noten eingescannt.\nEs können also auch keine gedruckt werden.");
			return;
		}
		this.setModal(true);
		this.setVisible(true);
	}

	private void printOneClicked(){
		PrintOneDialogPanel choose = new PrintOneDialogPanel();
		if (JOptionPane.showConfirmDialog(this, choose, this.getTitle(), JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
			try {
				Sheet s = choose.getSelectedSheet();
				int times = choose.getSelectedNumber();
				if (s==null){
					throw new IOException();
				}
				//TODO: get number and invoke Print...
				s.print();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Diese Stimme konnte für diesen Song nicht gefunden werden.");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bPrintOne)){
			this.printOneClicked();
		}
	}
}
