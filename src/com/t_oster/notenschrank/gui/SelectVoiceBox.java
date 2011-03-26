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
	
	public SelectVoiceBox(Song s){
		super(Archive.getInstance().getAvailableVoices(s));
		AutoCompletion.enable(this, false);
	}
	
	public SelectVoiceBox(){
		this(true);
	}
	
	public SelectVoiceBox(boolean editable){
		super(Archive.getInstance().getAvailableVoices());
		AutoCompletion.enable(this, editable);
	}
	
	/**
	 * Makes the Combobox to just contain
	 * Voices which are present for the specified
	 * Song.
	 * if s is null, all possible Voices are added.
	 * @param s
	 */
	public void setSong(Song s){
		Object o = this.getSelectedItem();
		this.removeAllItems();
		for (Voice v:Archive.getInstance().getAvailableVoices(s)){
			this.addItem(v);
		}
		this.setSelectedItem(o);
	}
	
	public Voice getSelectedVoice(){
		Object o = this.getSelectedItem();
		if (o instanceof Voice){
			return (Voice) o;
		}
		else if(o != null && o.toString() != ""){//not in the List yet
			Voice v = new Voice(o.toString());
			this.addItem(v);
			//TODO: sort
			this.setSelectedItem(v);
			return v;
		}
		else{
			return null;
		}
	}
	
}
