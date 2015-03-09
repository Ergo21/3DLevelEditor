package common;

import common.Global.TLEType;

import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class TLEData extends Group {
	private String name;
	private String id;
	private String filePath;
	private Node mesh;
	private LightBase light;
	private Color colour;
	private String activator;
	private TLEType type;
	
	/**
	 * Creates TLEData the editor uses.
	 * @param n Data name
	 * @param i Data ID
	 * @param t Type of Data
	 */
	public TLEData(String n, String i, TLEType t){
		super();
		name = n;
		id = i;
		mesh = null;
		type = t;
		if(type != TLEType.MESH && type != TLEType.SOUND){
			filePath = "NA";
		}
		else{
			filePath = "";
		}
		light = null;
		colour = null;
		activator = "";
	}
	
	public void setName(String n){
		name = n;
	}
	
	public void setID(String i){
		id = i;
	}
	
	public void setLight(LightBase l){
		super.getChildren().remove(light);
		light = l;
		super.getChildren().add(light);
	}
	
	public void setFilePath(String f){
		filePath = f;
	}
	
	public void setMesh(Node n){
		super.getChildren().remove(mesh);
		mesh = n;
		super.getChildren().add(mesh);
	}
	
	public void setColour(Color c){
		colour = c;
	}
	
	public void setActivator(String a){
		activator = a;
	}
	
	public String getName(){
		return name;
	}
	
	public String getID(){
		return id;
	}
	
	public LightBase getLight(){
		return light;
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	public Node getMesh(){
		return mesh;
	}
	
	public Color getColour(){
		return colour;
	}
	
	public String getActivator(){
		return activator;
	}
	
	public TLEType getType(){
		return type;
	}
	
}