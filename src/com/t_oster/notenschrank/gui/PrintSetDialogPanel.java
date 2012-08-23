package com.t_oster.notenschrank.gui;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Voice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.table.AbstractTableModel;

public class PrintSetDialogPanel extends JPanel implements ActionListener{
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
	private JButton bSetAll;
	private JSpinner tfAllNumber;
	
	
	public PrintSetDialogPanel(){
		bSong = new SelectSongBox(true,false);
		availableVoices = Archive.getInstance().getAvailableVoices(bSong.getSelectedSong());
		selectedNumbers = new int[availableVoices.length];
		Map<String,Integer> m= SettingsManager.getInstance().getPredefinedNumbers();
		if (m!=null && m.size()>0){
			for (int i=0;i<availableVoices.length;i++){
				selectedNumbers[i]=m.containsKey(availableVoices[i].toString())?m.get(availableVoices[i].toString()):1;
			}
		}
		
		bSong.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				availableVoices = Archive.getInstance().getAvailableVoices(bSong.getSelectedSong());
				selectedNumbers = new int[availableVoices.length];
				Map<String,Integer> m= SettingsManager.getInstance().getPredefinedNumbers();
				if (m!=null && m.size()>0){
					for (int i=0;i<availableVoices.length;i++){
						selectedNumbers[i]=m.containsKey(availableVoices[i].toString())?m.get(availableVoices[i].toString()):1;
					}
				}
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
		tVoices.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2){
					if (tVoices.columnAtPoint(e.getPoint())==0){
						int index = tVoices.rowAtPoint(e.getPoint());
						try{
							Sheet preview = Archive.getInstance().getSheet(bSong.getSelectedSong(), availableVoices[index]);
							JOptionPane.showMessageDialog(tVoices.getParent(),
									new PreviewPanel(tVoices.getParent(), preview));
						}
						catch(Exception ex){
							
						}
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		cbPreview = new JCheckBox("Druckvorschau");
		bSetAll = new JButton("Anzahl alle");
		bSetAll.addActionListener(this);
		tfAllNumber = new JSpinner(
				new SpinnerListModel(new Integer[]{0,1,2,3,4,5})
				);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("Stück"));
		tmp.add(bSong);
		add(tmp);
		add(new JScrollPane(tVoices));
		Box box = Box.createHorizontalBox();
		box.add(tfAllNumber);
		box.add(bSetAll);
		box.add(cbPreview);
		add(box);
	
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bSetAll)){
			int i = (Integer) tfAllNumber.getValue();
			for (int k=0;k<selectedNumbers.length;k++){
				selectedNumbers[k]=i;
			}
			tVoicesModel.fireTableDataChanged();
		}
	}
	
}
