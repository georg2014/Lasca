//editor: Georg Stahn
package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.Serializable;

public class LascaField implements Serializable {
	private static final long serialVersionUID = 2467695635164175201L;
	
	private String field = null;
	
	//constructor
	public LascaField(String field){
		if(field == "")
			this.field = null;
		else
			this.field = field;
	}
	
	
	//delete
	public void delete() {
		this.field = null;
	}
	//set
	public void setField(String stones){
		if(stones == "")
			this.field = null;
		this.field = (stones);
	}
	//addStone
	public void addStone(String stone){
		if(field != null)
			setField(field.concat(stone));
		else
			this.setField(stone);
	}
	//setFirst
	public void setFirst(String stone){
		if(field.length()<2)
			setField(stone);
		else
			setField(stone.concat(field.substring(1)));
	}
	
	
	//catch
	public String doCatch(){
		String stone = getFirst();
		if(field.length()>1)
			field = field.substring(1, field.length());
		else
			field = null;
		return stone;
	}
	//getField
	public String getField(){
		return this.field;
	}
	//getFirst
	public String getFirst(){
//		System.out.println("field(in lascafield): "+field);
		if(field != null)
			return getField().substring(0, 1);
		return null;
	}
	
	
	//isOfficer
	public boolean isOfficer(){
		return "W".equals(getFirst()) || "B".equals(getFirst());
	}
}
