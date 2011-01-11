package com.t_oster.notenschrank.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.SettingsManager;

public class SortingFrame extends JDialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699371667100327736L;
	private PreviewPanel previewpanel;
	private JPanel mainPanel;
	private SelectSongBox cbSong;
	private SelectVoiceBox cbVoice;
	private JButton bOk;
	private JButton bAddPage;
	private JButton bRotatePage;
	
	public SortingFrame(JFrame parent){
		super(parent,"Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		this.previewpanel = new PreviewPanel();
		this.cbSong = new SelectSongBox();
		this.cbVoice = new SelectVoiceBox();
		this.bOk = new JButton("Speichern");
		this.bAddPage = new JButton("weitere Seite");
		this.bRotatePage = new JButton("Seite drehen");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		this.mainPanel.add(this.previewpanel);
		this.mainPanel.add(cbSong);
		this.mainPanel.add(cbVoice);
		Box box = Box.createHorizontalBox();
		box.add(bRotatePage);
		box.add(bAddPage);
		box.add(bOk);
		this.mainPanel.add(box);
		this.setContentPane(mainPanel);
		this.pack();
		this.setVisible(true);
	}
	
	public void waitForClose(){
		this.setVisible(false);
		this.setModal(true);
		//bocks invoking Thread until disposed
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
