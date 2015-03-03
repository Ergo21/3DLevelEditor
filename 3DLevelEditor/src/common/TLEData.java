package common;

import common.Global.TLEType;

import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class TLEData extends Group {
	private String name;
	private String id;
	private String meshPath;
	private Node mesh;
	private LightBase light;
	private Color colour;
	private String activator;
	private TLEType type;
	
	public TLEData(String n, String i, TLEType t){
		super();
		name = n;
		id = i;
		mesh = null;
		type = t;
		if(type != TLEType.MESH){
			meshPath = "NA";
		}
		else{
			meshPath = "";
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
	
	public void setMeshPath(String m){
		meshPath = m;
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
	
	public String getMeshPath(){
		return meshPath;
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