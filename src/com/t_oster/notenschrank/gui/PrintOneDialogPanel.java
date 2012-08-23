package com.t_oster.notenschrank.gui;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Sheet;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class PrintOneDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectSongBox bSong;
	private SelectVoiceBox bVoice;
	private JSpinner sNumber;
	private SpinnerListModel lm;
	private JCheckBox cbPreview;
	
	public PrintOneDialogPanel(){
		this.setLayout(new GridLayout(0,2));
		bSong = new SelectSongBox(true,false);
		bSong.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				bVoice.setSong(bSong.getSelectedSong());
			}
			
		});
		bVoice = new SelectVoiceBox(bSong.getSelectedSong());
		lm=new SpinnerListModel(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		
		cbPreview = new JCheckBox("Druckvorschau anzeigen");
		cbPreview.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				sNumber.setEnabled(!cbPreview.isSelected());
			}
			
		});
		sNumber = new JSpinner(lm);
		add(new JLabel("Stück"));
		add(bSong);
		add(new JLabel("Stimme"));
		add(bVoice);
		add(new JLabel("Anzahl"));
		add(sNumber);
		add(cbPreview);
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
	 * Should only be used if isPreviewSelected is false
	 * because otherwise the user should use the
	 * print-dialog from his PDF Viewer
	 */
	public int getSelectedNumber(){
		return Integer.parseInt((String) lm.getValue());
	}
	
	public boolean isPreviewSelected(){
		return cbPreview.isSelected();
	}
}
