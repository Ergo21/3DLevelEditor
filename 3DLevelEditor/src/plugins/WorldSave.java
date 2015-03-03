package plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import common.TLEData;

/**
 * WorldSave is the version of World to be saved.
 * @author Ergo21
 *
 */
public class WorldSave implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9065313159572910346L;
	private HashMap<String, ArrayList<NodeSave>> wSave;
	
	/**
	 * Saves data into serializable form.
	 * @param data The data to be saved.
	 */
	public WorldSave(HashMap<String, ArrayList<TLEData>> data){
		wSave = new HashMap<String, ArrayList<NodeSave>>();
		String[] keys = new String[0];
		keys = data.keySet().toArray(keys);
		
		for(int i = 0; i < keys.length; i++){
			ArrayList<NodeSave> curList = new ArrayList<NodeSave>();		
			
			for(int j = 0; j < data.get(keys[i]).size(); j++){
				NodeSave nS = new NodeSave(data.get(keys[i]).get(j));
				if(nS != null && !nS.getMeshPath().equals(null) && !nS.getMeshPath().equals("NA")){
					curList.add(nS);
				}	
			}
			wSave.put(keys[i], curList);
		}
	}
	
	/**
	 * 
	 * @return Saved World for loading.
	 */
	public HashMap<String, ArrayList<NodeSave>> getWorldSave(){
		return wSave;
	}
	
}