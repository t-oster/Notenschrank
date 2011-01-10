package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.t_oster.notenschrank.data.SettingsManager;


public class MainFrame extends JFrame implements ActionListener, WindowListener{


	public enum Action{
		actionScanClicked,
		actionPrintClicked,
		actionCloseClicked
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5200598004775043020L;
	private JButton btScan;
	private JButton btPrint;
	private List<ActionListener> lListeners;
	
	public MainFrame(){
		super("Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		
		//initialize Behavior
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		//initialize Attributes
		btScan = new JButton("Noten einscannen");
		btPrint = new JButton("Noten ausdrucken");
		lListeners = new LinkedList<ActionListener>();
		
		//initialize Listeners
		btScan.addActionListener(this);
		btPrint.addActionListener(this);
		this.addWindowListener(this);
		
		//layout & look and Feel
		Box b = Box.createVerticalBox();
		b.add(btScan);
		b.add(btPrint);
		this.setContentPane(b);
		this.setMinimumSize(new Dimension(200,90));
		this.setVisible(true);
	}

	private void throwActionEvent(Action action){
		for (ActionListener l:lListeners){
			l.actionPerformed(new ActionEvent(this, action.ordinal(), null));
		}
	}
	
	public void addActionListener(ActionListener l){
		lListeners.add(l);
	}
	public void removeActionListener(ActionListener l){
		lListeners.remove(l);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(this.btScan)){
			this.throwActionEvent(Action.actionScanClicked);
		}
		else if (arg0.getSource().equals(this.btPrint)){
			this.throwActionEvent(Action.actionPrintClicked);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// nothing
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// nothing
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.throwActionEvent(Action.actionCloseClicked);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// nothing
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// nothing
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// nothing
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// nothing
	}
	
}
