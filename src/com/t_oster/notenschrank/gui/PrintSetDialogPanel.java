package com.t_oster.notenschrank.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Voice;

public class PrintSetDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectSongBox bSong;
	private JTable tVoices;
	private AbstractTableModel tVoicesModel;
	private JCheckBox cbPreview;
	private Voice[] availableVoices;
	private int[] selectedNumbers;
	
	
	public PrintSetDialogPanel(){
		bSong = new SelectSongBox(false);
		availableVoices = Archive.getInstance().getAvailableVoices(bSong.getSelectedSong());
		selectedNumbers = new int[availableVoices.length];
		bSong.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				availableVoices = Archive.getInstance().getAvailableVoices(bSong.getSelectedSong());
				selectedNumbers = new int[availableVoices.length];
				tVoicesModel.fireTableDataChanged();
			}
			
		});
		tVoicesModel = new AbstractTableModel(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 5881114279927060254L;

			public String getColumnName(int column){
				return column==0?"Stimme":"Anzahl";
			}
			
			public boolean isCellEditable(int r, int c){
				return (c==1);
			}
			
			 public void setValueAt(Object value, int row, int col) {
				 	if (col==1){
				        selectedNumbers[row]=Integer.parseInt((String) value);
				        fireTableCellUpdated(row, col);
				 	}
			    }
			
			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public int getRowCount() {
				return availableVoices==null?0:availableVoices.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex==0){
					return availableVoices[rowIndex];
				}
				else{
					return selectedNumbers[rowIndex];
				}
			}
			
		};
		tVoices = new JTable(tVoicesModel);
		tVoices.setFillsViewportHeight(true);
		cbPreview = new JCheckBox("Druckvorschau");
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("Stück"));
		tmp.add(bSong);
		add(tmp);
		add(new JScrollPane(tVoices));
		add(cbPreview);
	
		
	}
	
	public boolean isPreviewSelected(){
		return cbPreview.isSelected();
	}
	
	public Sheet[] getSelectedSheets() throws IOException{
		return Archive.getInstance().getSheets(bSong.getSelectedSong());
	}
	
	public int[] getSelectedNumbers(){
		return selectedNumbers;
	}
	
}
