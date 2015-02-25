package plugins;

import java.io.Serializable;
import java.util.HashMap;
import javafx.scene.transform.Transform;

import common.TLEData;


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
	private String meshPath;
	
	private HashMap<String, Double> meshTrans;
	
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
		meshPath = data.getMeshPath();
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
	}
	
	public String getName(){
		return name;
	}
	
	public String getSaveId(){
		return id;
	}
	
	public String getMeshPath(){
		return meshPath;
	}
	
	public HashMap<String, Double> getMeshTransforms(){
		return meshTrans;
	}
}