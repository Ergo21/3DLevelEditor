package plugins;

import java.util.ArrayList;

import common.Global.TLEType;
import common.TLEData;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.shape.Box;
import javafx.scene.transform.Transform;


public class DGPhysics{
	
	private ArrayList<TLEType> antiGravity;
	
	public DGPhysics(){
		antiGravity = new ArrayList<TLEType>();
		antiGravity.add(TLEType.ACTIVATOR);
		antiGravity.add(TLEType.CUBE);
		antiGravity.add(TLEType.LIGHT);
		antiGravity.add(TLEType.SOUND);
	}
	
	public boolean thiCollideAny(Node tar, ArrayList<TLEData> lis){
		
		for(int i = 0; i < lis.size(); i++){
			if(lis.get(i) != tar){
				if(theColliding(tar, lis.get(i))){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public TLEData thiCollideType(Node tar, ArrayList<TLEData> lis, TLEType typ){
		for(int i = 0; i < lis.size(); i++){
			if(lis.get(i) != tar){
				if(theColliding(tar, lis.get(i))){
					if(lis.get(i).getType() == typ){
						return lis.get(i);
					}
				}
			}
		}
		
		return null;
	}
	
	public boolean theColliding(Node mover, Node test){
		double mMinX = mover.boundsInParentProperty().get().getMinX(),
				mMaxX = mover.boundsInParentProperty().get().getMaxX(),
				mMinY = mover.boundsInParentProperty().get().getMinY(),
				mMaxY = mover.boundsInParentProperty().get().getMaxY(),
				mMinZ = mover.boundsInParentProperty().get().getMinZ(),
				mMaxZ = mover.boundsInParentProperty().get().getMaxZ();
		double tMinX = test.boundsInParentProperty().get().getMinX(),
				tMaxX = test.boundsInParentProperty().get().getMaxX(),
				tMinY = test.boundsInParentProperty().get().getMinY(),
				tMaxY = test.boundsInParentProperty().get().getMaxY(),
				tMinZ = test.boundsInParentProperty().get().getMinZ(),
				tMaxZ = test.boundsInParentProperty().get().getMaxZ();

		if((mMinX >= tMinX && mMinX <= tMaxX) || (mMaxX >= tMinX && mMaxX <= tMaxX)){
			if((mMinY >= tMinY && mMinY <= tMaxY) || (mMaxY >= tMinY && mMaxY <= tMaxY)){
				if((mMinZ >= tMinZ && mMinZ <= tMaxZ) || (mMaxZ >= tMinZ && mMaxZ <= tMaxZ)){
					System.out.println("Collision detected");
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void applyPhysics(ArrayList<TLEData> lis, PerspectiveCamera player, Box aB){
		System.out.println("Physics applied");
		player.setTranslateY(player.getTranslateY() + 1);
		aB.setTranslateY(aB.getTranslateY() + 1);
		boolean coll = thiCollideAny(player, lis);
		if(coll){
			player.setTranslateY(player.getTranslateY() - 1);
			aB.setTranslateY(aB.getTranslateY() - 1);
		}
		
		for(int i = 0; i < lis.size(); i++){
			TLEType temT = lis.get(i).getType();
			if(!antiGravity.contains(temT)){
				lis.get(i).setTranslateY(lis.get(i).getTranslateY() + 1);
				for(int j = 0; j < lis.size(); j++){
					if(i != j){
						if(theColliding(lis.get(i), lis.get(j))){
							lis.get(i).setTranslateY(lis.get(i).getTranslateY() - 1);
							break;
						}
					}
				}
			}
		}
	}
	
	public void gravity(){
		
	}
	
	public ArrayList<Transform> createInverse(ObservableList<Transform> lis){
		ArrayList<Transform> inv = new ArrayList<Transform>();
		
		for(int i = 0; i < lis.size(); i++){
			try{
				Transform t = lis.get(i).createInverse();
				inv.add(t);
			}
			catch(Exception e){};
		}
		
		return inv;
	}
}