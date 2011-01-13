package com.t_oster.notenschrank.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Sheet;

public class PrintOneDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectSongBox bSong;
	private SelectVoiceBox bVoice;
	private JSpinner sNumber;
	private SpinnerListModel lm;
	
	
	public PrintOneDialogPanel(){
		this.setLayout(new GridLayout(0,2));
		bSong = new SelectSongBox(false);
		bSong.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				bVoice.setSong(bSong.getSelectedSong());
			}
			
		});
		bVoice = new SelectVoiceBox(bSong.getSelectedSong());
		lm=new SpinnerListModel(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		sNumber = new JSpinner(lm);
		add(new JLabel("Stück"));
		add(bSong);
		add(new JLabel("Stimme"));
		add(bVoice);
		add(new JLabel("Anzahl"));
		add(sNumber);
	}
	
	/*
	 * Returns the selected Sheet, if it exists, null otherwise
	 */
	public Sheet getSelectedSheet(){
		try {
			return Archive.getInstance().getSheet(bSong.getSelectedSong(), bVoice.getSelectedVoice());
		} catch (IOException e) {
			return null;
		}
	}
	
	/*
	 * Returns the number specified in the Spinner
	 */
	public int getSelectedNumber(){
		return Integer.parseInt((String) lm.getValue());
	}
}
