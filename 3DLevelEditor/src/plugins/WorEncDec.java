package plugins;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.scene.transform.Transform;
import common.TLEData;

/**
 * Currently unimplemented. Would be used to save the world in a format readable by multiple engines, e.g. XML.
 * @author Ergo21
 *
 */
public class WorEncDec{
	
	public ArrayList<String> encryptText(HashMap<String, ArrayList<TLEData>> d){
		ArrayList<String> strings = new ArrayList<String>();
		String[] keys = new String[0];	
		keys = d.keySet().toArray(keys);
		
		for(int i = 0; i < keys.length; i++){
			String curStr = "<" + keys[i] + "> { ";
			ArrayList<TLEData> curDat = d.get(keys[i]);
			
			for(int j = 0; j < curDat.size(); j++){
				TLEData curTle = curDat.get(j);
				curStr += "<<Name=" + curTle.getName() + "> ";
				curStr += "<ID=" + curTle.getID() + "> ";
				curStr += "<Path=" + curTle.getFilePath() + ">> {";
				
				ObservableList<Transform> curTrans = curTle.getTransforms();
				for(int k = 0; k < curTrans.size(); k++){
					curStr += "<Transform " + k + "> { ";
					curStr += "<Mxx=" + curTrans.get(k).getMxx() + "> ";
					curStr += "<Mxy=" + curTrans.get(k).getMxy() + "> ";
					curStr += "<Mxz=" + curTrans.get(k).getMxz() + "> ";
					curStr += "<Myx=" + curTrans.get(k).getMyx() + "> ";
					curStr += "<Myy=" + curTrans.get(k).getMyy() + "> ";
					curStr += "<Myz=" + curTrans.get(k).getMyz() + "> ";
					curStr += "<Mzx=" + curTrans.get(k).getMzx() + "> ";
					curStr += "<Mzy=" + curTrans.get(k).getMzy() + "> ";
					curStr += "<Mzz=" + curTrans.get(k).getMzz() + "> ";
					curStr += "<Tx=" + curTrans.get(k).getTx() + "> ";
					curStr += "<Ty=" + curTrans.get(k).getTy() + "> ";
					curStr += "<Tz=" + curTrans.get(k).getTz() + "> ";
					
					curStr += "} ";
				}
				
				curStr += "} ";
			}
			
			curStr += "}";
			strings.add(curStr);
		}
		
		
		return strings;
	}
	
	public HashMap<String, ArrayList<TLEData>> decryptText(ArrayList<String> s){
		HashMap<String, ArrayList<TLEData>> data = new HashMap<String, ArrayList<TLEData>>();
		
		return data;
	}
}