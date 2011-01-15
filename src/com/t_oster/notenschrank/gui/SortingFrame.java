package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.itextpdf.text.DocumentException;
import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class SortingFrame extends JDialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699371667100327736L;
	private PreviewPanel previewpanel;
	private PreviewPanel current;
	private JPanel mainPanel;
	private SelectSongBox cbSong;
	private SelectVoiceBox cbVoice;
	private JButton bOk;
	private JButton bAddPage;
	private JButton bRotatePage;
	private List<Sheet> stackSheets;

	public SortingFrame(JFrame parent){
		super(parent,"Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		this.setPreferredSize(new Dimension(800,600));
		this.previewpanel = new PreviewPanel(this);
		this.current = new PreviewPanel(this);
		this.cbSong = new SelectSongBox();
		this.cbSong.setMaximumSize(new Dimension(500,35));
		this.cbVoice = new SelectVoiceBox();
		this.cbVoice.setMaximumSize(new Dimension(500,35));
		
		this.bOk = new JButton("Speichern");
		this.bOk.addActionListener(this);
		this.bAddPage = new JButton("nächste Seite gehört dazu");
		this.bAddPage.addActionListener(this);
		this.bRotatePage = new JButton("Seite drehen");
		this.bRotatePage.addActionListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		Box b = Box.createHorizontalBox();
		b.add(Box.createHorizontalGlue());
		b.add(new JLabel("nächstes Blatt"));
		b.add(Box.createHorizontalGlue());
		b.add(new JLabel("aktuelles Blatt"));
		b.add(Box.createHorizontalGlue());
		this.mainPanel.add(b);
		JPanel previewContainer = new JPanel();
		previewContainer.setLayout(new GridLayout(1,2));
		previewContainer.add(this.previewpanel);
		previewContainer.add(this.current);
		this.mainPanel.add(previewContainer);
		//TODO: Comboboxen übereinander, nicht versetzt
		b=Box.createHorizontalBox();
		b.add(Box.createHorizontalGlue());
		b.add(new JLabel("Lied:      "));
		b.add(cbSong);
		b.add(Box.createHorizontalGlue());
		this.mainPanel.add(b);
		b=Box.createHorizontalBox();
		b.add(Box.createHorizontalGlue());
		b.add(new JLabel("Stimme:"));
		b.add(cbVoice);
		b.add(Box.createHorizontalGlue());
		this.mainPanel.add(b);
		Box box = Box.createHorizontalBox();
		//TODO: Next Button deaktivieren wenn kein Blatt mehr da
		box.add(bAddPage);
		box.add(bRotatePage);
		box.add(bOk);
		this.mainPanel.add(box);
		this.setContentPane(mainPanel);
		this.pack();
	}
	
	public void showDialog(){
		
		try{
			this.stackSheets = Archive.getInstance().getUnsortedSheets();
			if (stackSheets.size()==0){
				JOptionPane.showMessageDialog(null, "Keine Noten zum sortieren vorhanden\nBitte scannen Sie zuerst welche ein", "Nix da.", JOptionPane.OK_OPTION);
				this.dispose();
				return;
			}
			Sheet cur = stackSheets.remove(0);
			this.current.showSheet(cur);
			if (stackSheets.size()>0){
				Sheet nxt = stackSheets.remove(0);
				this.previewpanel.showSheet(nxt);
			}
			else{
				bAddPage.setEnabled(false);
			}
			this.setModal(true);
			//bocks invoking Thread until disposed
			this.setVisible(true);
		}
		catch (Exception e){
			
		}
		
	}

	private void rotateClicked(){
		try {
			Sheet s = this.current.getSheet();
			s.rotatePage(s.numberOfPages(), 1);
			this.current.refresh();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void addClicked(){
		try {
			Sheet current = this.current.getSheet();
			Sheet preview = this.previewpanel.getSheet();
			if (preview != null){
				current.addPage(this.previewpanel.getSheet());
				this.current.refresh();
				preview.delete();
				if (this.stackSheets.size()>0){
					this.previewpanel.showSheet(this.stackSheets.remove(0));
				}
				else{
					this.previewpanel.showSheet(null);
					this.bAddPage.setEnabled(false);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void okClicked(){
		Sheet s = current.getSheet();
		Song song = cbSong.getSelectedSong();
		Voice v = cbVoice.getSelectedVoice();
		if (song==null || v==null){
			JOptionPane.showMessageDialog(this, "Sie müssen sowohl Titel als auch Stimme eingeben", "Fehler", JOptionPane.OK_OPTION);
			return;
		}
		try {
			Archive.getInstance().addToArchive(s, song, v);
			s.delete();
			Sheet p = previewpanel.getSheet();
			this.current.showSheet(p);
			if (this.stackSheets.size()>0){
				this.previewpanel.showSheet(this.stackSheets.remove(0));
			}
			else{
				this.previewpanel.showSheet(null);
				this.bAddPage.setEnabled(false);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.bRotatePage)){
			this.rotateClicked();
		}
		else if (e.getSource().equals(this.bAddPage)){
			this.addClicked();
		}
		else if (e.getSource().equals(this.bOk)){
			this.okClicked();	
		}
	}
}
