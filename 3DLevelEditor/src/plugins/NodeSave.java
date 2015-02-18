package plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import common.TLEData;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

public class NodeSave implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6720706129414196456L;
	private String name;
	private String id;
	private String meshPath;
	
	private ArrayList<NodeSave> children = null;
	private ArrayList<HashMap<String, Double>> transforms;
	
	public NodeSave(Node n){
		transforms = new ArrayList<HashMap<String, Double>>();
		
		if(TLEData.class.isAssignableFrom(n.getClass())){
			tleBuilder((TLEData) n);
		}
		else if(Group.class.isAssignableFrom(n.getClass())){
			groBuilder((Group)n);
		}
		else{
			nodBuilder(n);
		}
	}
	
	private void tleBuilder(TLEData data){
		name = data.getName();
		id = data.getID();
		meshPath = data.getMeshPath();
		data.setMesh(null);
		groBuilder(data);
	}
	
	private void groBuilder(Group data){
		ObservableList<Node> nodes = data.getChildren();
		
		if(nodes != null){
			children = new ArrayList<NodeSave>();
			for(int i = 0; i < nodes.size(); i++){
				children.add(new NodeSave(nodes.get(i)));
			}
		}	
		
		nodBuilder(data);
	}

	private void nodBuilder(Node data){
		ObservableList<Transform> lis = data.getTransforms();
		if(lis != null){
			for(int i = 0; i < lis.size(); i++){
				HashMap<String, Double> curMap = new HashMap<String, Double>();
				curMap.put("Mxx", lis.get(i).getMxx());
				curMap.put("Mxy", lis.get(i).getMxy());
				curMap.put("Mxz", lis.get(i).getMxz());
				curMap.put("Myx", lis.get(i).getMyx());
				curMap.put("Myy", lis.get(i).getMyy());
				curMap.put("Myz", lis.get(i).getMyz());
				curMap.put("Mzx", lis.get(i).getMzx());
				curMap.put("Mzy", lis.get(i).getMzy());
				curMap.put("Mzz", lis.get(i).getMzz());
				curMap.put("Tx", lis.get(i).getTx());
				curMap.put("Ty", lis.get(i).getTy());
				curMap.put("Tz", lis.get(i).getTz());
				transforms.add(curMap);
			}
		}
		
	}
}