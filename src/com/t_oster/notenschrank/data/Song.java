package com.t_oster.notenschrank.data;

public class Song implements Comparable{
	private String name;
	
	public Song(String name){
		this.name = name;
	}
	
  @Override
	public String toString(){
		return name;
	}

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }
	
  @Override
	public boolean equals(Object o){
		if (o instanceof Song){
			return (this.name.equals(((Song) o).name));
		}
		else{
			return super.equals(o);
		}
	}

  @Override
  public int compareTo(Object t)
  {
    if (t instanceof Song)
    {
      return this.name.compareToIgnoreCase(((Song) t).name);
    }
    return 0;
  }
}
