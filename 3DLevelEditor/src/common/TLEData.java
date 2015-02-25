package common;

import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;

public class TLEData extends Group {
	private String name;
	private String id;
	private String meshPath;
	private Node mesh;
	private LightBase light;
	
	public TLEData(String n, String i, String m){
		super();
		name = n;
		id = i;
		mesh = null;
		meshPath = m;
		light = null;
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
	
}