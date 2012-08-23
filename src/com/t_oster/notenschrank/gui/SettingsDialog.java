package com.t_oster.notenschrank.gui;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class SettingsDialog extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1489049124905695755L;
	private JTextField tfArchPath = new JTextField(SettingsManager.getInstance().getArchivePath().getAbsolutePath());
	private JButton bBrowsAP = new JButton("browse");
	private JTextField tfStackPath = new JTextField(SettingsManager.getInstance().getStackPath().getAbsolutePath());
	private JButton bBrowsSP = new JButton("browse");
	//Use String instead of Voice, because HashMap has Problems
	private Map<String,JTextField> numberFields;
	private Map<String,JCheckBox> isDefaultFields;
	private JPanel thiss;
	
	private SettingsDialog(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0,3));
		mainPanel.add(new JLabel("Archiv:"));
		mainPanel.add(tfArchPath);
		mainPanel.add(bBrowsAP);
		mainPanel.add(new JLabel("Gescannte Dateien:"));
		mainPanel.add(tfStackPath);
		mainPanel.add(bBrowsSP);
		this.add(mainPanel);
		JPanel numbersPanel = new JPanel();
		numbersPanel.setLayout(new GridLayout(0,3));
		
		Map<String, Integer> map = SettingsManager.getInstance().getPredefinedNumbers();
		Voice[] voices = Archive.getInstance().getAvailableVoices();
		numberFields = new LinkedHashMap<String, JTextField>();
		if (map!=null && map.size()>0){
			for (Entry<String, Integer> e:map.entrySet()){
				numbersPanel.add(new JLabel("Standard Anzahl für "));
				numbersPanel.add(new JLabel(e.getKey()));
				JTextField tf = new JTextField();
				tf.setText(e.getValue().toString());
				numbersPanel.add(tf);
				numberFields.put(e.getKey().toString(),tf);
			}
		}
		
		if (voices != null && voices.length >0){
			for (Voice v:voices){
				if (!numberFields.containsKey(v.toString())){
					numbersPanel.add(new JLabel("Standard Anzahl für "));
					numbersPanel.add(new JLabel(v.toString()));
					JTextField tf = new JTextField();
					tf.setText("1");
					numbersPanel.add(tf);
					numberFields.put(v.toString(),tf);
				}
			}
		}
		this.add(new JLabel("Standard-Anzahlen für einzelne Stimmen"));
		JScrollPane tmp = new JScrollPane(numbersPanel);
		tmp.setPreferredSize(new Dimension(300,200));
		this.add(tmp);
		
		JPanel defaultSongsPanel = new JPanel();
		defaultSongsPanel.setLayout(new GridLayout(0,3));
		Set<String> defaultSongs = SettingsManager.getInstance().getDefaultSongs();
		Song[] songs = Archive.getInstance().getAvailableSongs();
		isDefaultFields = new LinkedHashMap<String,JCheckBox>();
		if (defaultSongs != null){
			for (String s:defaultSongs){
				defaultSongsPanel.add(new JLabel("In aktueller Mappe enthalten"));
				defaultSongsPanel.add(new JLabel(s));
				JCheckBox b = new JCheckBox();
				b.setSelected(true);
				defaultSongsPanel.add(b);
				isDefaultFields.put(s,b);
			}
		}
		for (Song s:songs){
			if (!isDefaultFields.containsKey(s.toString())){
				defaultSongsPanel.add(new JLabel("In aktueller Mappe enthalten"));
				defaultSongsPanel.add(new JLabel(s.toString()));
				JCheckBox b = new JCheckBox();
				b.setSelected(false);
				defaultSongsPanel.add(b);
				isDefaultFields.put(s.toString(),b);
			}
		}
		this.add(new JLabel("Stücke für Standard-Mappen"));
		tmp = new JScrollPane(defaultSongsPanel);
		tmp.setPreferredSize(new Dimension(300,200));
		this.add(tmp);
		this.thiss=this;
		
		bBrowsAP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Bitte wählen Sie den Archivordner aus");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(thiss) == JFileChooser.APPROVE_OPTION) { 
				      tfArchPath.setText(chooser.getSelectedFile().getAbsolutePath());
			      }
			    else {
			      }
			}
			
		});
		
		bBrowsSP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Bitte wählen Sie den Ordner mit den gescannten Daten aus");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(thiss) == JFileChooser.APPROVE_OPTION) { 
				      tfStackPath.setText(chooser.getSelectedFile().getAbsolutePath());
			      }
			    else {
			      }
			}
			
		});
		
	}
	
	/**
	 * Shows the settings dialog and applies changes
	 * if OK pressed
	 * 
	 * this method blocks the current Thread until the Dialog is closed
	 * @param parent
	 */
	public static void showDialog(JFrame parent, String title){
		SettingsDialog sd = new SettingsDialog();
		if (JOptionPane.showConfirmDialog(parent, sd, title, JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
			sd.applySettings();
		}
	}
	
	public void applySettings(){
		SettingsManager sm = SettingsManager.getInstance();
		sm.setArchivePath(tfArchPath.getText());
		sm.setStackPath(tfStackPath.getText());
		Map<String,Integer> nm = new LinkedHashMap<String,Integer>();
		for (Entry<String, JTextField> e:numberFields.entrySet()){
			try{
				nm.put(e.getKey(), Integer.parseInt(e.getValue().getText()));
			}
			catch(NumberFormatException ee){
				nm.put(e.getKey(), 1);
			}
			
		}
		sm.setPredefinedNumbers(nm);
		Set<String> defaultSongs = new LinkedHashSet<String>();
		for (Entry<String, JCheckBox> e:isDefaultFields.entrySet()){
			if (e.getValue().isSelected()){
				defaultSongs.add(e.getKey());
			}
		}
		sm.setDefaultSongs(defaultSongs);
		
		try {
			sm.save();
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Einstellungen\nDie Änderrungen sind nur bis zum Beenden des Programms wirksam.");
		}
 	}
}
