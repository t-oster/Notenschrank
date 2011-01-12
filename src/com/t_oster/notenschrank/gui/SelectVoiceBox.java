package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class SelectVoiceBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2217011651055295158L;

	private boolean reloadImportant=false;
	
	public SelectVoiceBox(){
		super(Archive.getInstance().getAvailableVoices());
		this.setEditable(true);
		//AutoCompletion.enable(this);
	}
	
	public Voice getSelectedVoice(){
		Object o = this.getSelectedItem();
		if (o instanceof Voice){
			return (Voice) o;
		}
		else if(o != null && o.toString() != ""){//not in the List yet
			reloadImportant = true;
			return new Voice(o.toString());
		}
		else{
			return null;
		}
	}
	
	public void reload(){
		if (reloadImportant){
			this.removeAllItems();
			for (Voice v:Archive.getInstance().getAvailableVoices()){
				this.addItem(v);
			}
			reloadImportant=false;
		}
		else{
			System.out.println("reload dropped");
		}
	}
}
