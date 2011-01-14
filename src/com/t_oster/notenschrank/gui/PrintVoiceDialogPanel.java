package com.t_oster.notenschrank.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.table.AbstractTableModel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class PrintVoiceDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectVoiceBox bVoice;
	private JTable tSongs;
	private AbstractTableModel tSongsModel;
	//private JSpinner sNumber;
	//private SpinnerListModel lm;
	private Song[] availableSongs;
	private boolean[] selectedNumbers;
	
	
	public PrintVoiceDialogPanel(){
		//this.setLayout(new GridLayout(0,2));
		bVoice = new SelectVoiceBox(false);
		bVoice.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				availableSongs = Archive.getInstance().getAvailableSongs(bVoice.getSelectedVoice());
				selectedNumbers = new boolean[availableSongs.length];
				tSongsModel.fireTableDataChanged();
			}
			
		});
		tSongsModel = new AbstractTableModel(){

			public String getColumnName(int column){
				return column==0?"Stimme":"auswählen";
			}
			
			public boolean isCellEditable(int r, int c){
				return (c==1);
			}
			
			 public void setValueAt(Object value, int row, int col) {
				 	if (col==1 && value instanceof Boolean){
				        selectedNumbers[row]=(Boolean) value;
				        fireTableCellUpdated(row, col);
				 	}
			    }
			
			@Override
			public int getColumnCount() {
				return 2;
			}

			public Class getColumnClass(int c) {
			        return getValueAt(0, c).getClass();
			}
			
			@Override
			public int getRowCount() {
				return availableSongs==null?0:availableSongs.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex==0){
					return availableSongs[rowIndex];
				}
				else{
					return selectedNumbers[rowIndex];
				}
			}
			
		};
		tSongs = new JTable(tSongsModel);
		//lm=new SpinnerListModel(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		//sNumber = new JSpinner(lm);
		add(new JLabel("Stück"));
		add(bVoice);
		tSongs.setFillsViewportHeight(true);
		add(new JScrollPane(tSongs));
	}
	
}
