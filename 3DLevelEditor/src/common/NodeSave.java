package common;

import java.io.Serializable;
import java.util.HashMap;

import javafx.scene.transform.Transform;


/**
 * NodeSave is the version of TLEData to be saved.
 * @author Ergo21
 *
 */
public class NodeSave implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6720706129414196456L;
	private String name;
	private String id;
	private String filePath;
	private String type;
	private String activator;
	
	private HashMap<String, Double> meshTrans;
	private HashMap<String, Double> objColour;
	
	/**
	 * Saves TLEData into a Serializable form.
	 * @param n The TLEData to be saved.
	 */
	public NodeSave(TLEData n){
			tleBuilder(n);		
	}
	
	private void tleBuilder(TLEData data){
		name = data.getName();
		id = data.getID();
		filePath = data.getFilePath();
		type = data.getType().toString();
		activator = "";
		meshTrans = null;
		if(data.getMesh() != null){
			Transform lis = data.getMesh().getLocalToSceneTransform();
			if(lis != null){
				HashMap<String, Double> curMap = new HashMap<String, Double>();
				curMap.put("Mxx", lis.getMxx());
				curMap.put("Mxy", lis.getMxy());
				curMap.put("Mxz", lis.getMxz());
				curMap.put("Myx", lis.getMyx());
				curMap.put("Myy", lis.getMyy());
				curMap.put("Myz", lis.getMyz());
				curMap.put("Mzx", lis.getMzx());
				curMap.put("Mzy", lis.getMzy());
				curMap.put("Mzz", lis.getMzz());
				curMap.put("Tx",  lis.getTx());
				curMap.put("Ty",  lis.getTy());
				curMap.put("Tz",  lis.getTz());
				meshTrans = curMap;
				
			}
		}

		objColour = null;
		if(data.getType() == Global.TLEType.LIGHT && data.getColour() != null){
			objColour = new HashMap<String, Double>();
			objColour.put("red", data.getLight().getColor().getRed());
			objColour.put("green", data.getLight().getColor().getGreen());
			objColour.put("blue", data.getLight().getColor().getBlue());
			objColour.put("alpha", data.getLight().getColor().getOpacity());
		}
		else if(data.getType() == Global.TLEType.CUBE && data.getColour() != null){
			objColour = new HashMap<String, Double>();
			objColour.put("red", data.getColour().getRed());
			objColour.put("green", data.getColour().getGreen());
			objColour.put("blue", data.getColour().getBlue());
			objColour.put("alpha", data.getColour().getOpacity());
		}
		else if(data.getType() == Global.TLEType.ACTIVATOR){
			activator = data.getActivator();
		}

	}
	
	public String getName(){
		return name;
	}
	
	public String getSaveId(){
		return id;
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	public String getType(){
		return type;
	}
	
	public String getActivator(){
		return activator;
	}
	
	public HashMap<String, Double> getMeshTransforms(){
		return meshTrans;
	}
	
	public HashMap<String, Double> getObjColour(){
		return objColour;
	}
}