package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class SortingDialog extends JDialog implements ActionListener{
	
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

	public SortingDialog(JFrame parent){
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
		
		//ToolTips
		bOk.setToolTipText("Speichert das aktuelle Blatt im Archiv unter dem angegeben Namen und der angegebenen Stimme");
		bAddPage.setToolTipText("Fügt das nächste Blatt (linke Seite) zum aktuellen Blatt (rechte Seite) hinzu. Sinnvoll bei mehrseitigen Stücken");
		bRotatePage.setToolTipText("Dreht das aktuelle Blatt (Sinnvoll wenn falschherum eingescannt)");
		cbVoice.setToolTipText("Stimme unter der das aktuelle Blatt archiviert werden soll (zB. 1. Klarinette in Bb)");
		cbSong.setToolTipText("Name unter dem das aktuelle Blatt archiviert werden soll. (zB. Martini)");
		previewpanel.setToolTipText("Blatt welches als nächstes archiviert wird (Nur angezeigt damit man erkennt ob das aktuelle Blatt mehrseitig ist)");
		current.setToolTipText("Zeigt die erste Seite des aktuellen Stücks. Zoom mit linker Maustaste in eine der 4 Ecken. Herauszoomen mit rechter Maustaste. Scrollen mit Scrollrad.");
		
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
		box.add(bAddPage);
		box.add(bRotatePage);
		box.add(bOk);
		this.mainPanel.add(box);
		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationByPlatform(true);
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
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim rotieren.", "Fehler", JOptionPane.OK_OPTION);
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
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim hinzufügen der Seite");
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
			JPanel existingPreview = new JPanel();
			existingPreview.setSize(300,500);
			existingPreview.setLayout(new BoxLayout(existingPreview, BoxLayout.Y_AXIS));
			//TODO: Fix Layout
			//existingPreview.add(new PreviewPanel(existingPreview, Archive.getInstance().getSheet(song,v)));
			existingPreview.add(new JLabel( "Datei ist bereits im Archiv.\nÜberschreiben?"));
			existingPreview.validate();
			if (Archive.getInstance().contains(song,v) && JOptionPane.showConfirmDialog(this,existingPreview,this.getTitle(), JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
				return;
			}
			Archive.getInstance().addToArchive(s, song, v);
			s.delete();
			Sheet p = previewpanel.getSheet();
			if (p==null){
				this.dispose();
			}
			this.current.showSheet(p);
			if (this.stackSheets.size()>0){
				this.previewpanel.showSheet(this.stackSheets.remove(0));
			}
			else{
				this.previewpanel.showSheet(null);
				this.bAddPage.setEnabled(false);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim speichern der Seite");
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
