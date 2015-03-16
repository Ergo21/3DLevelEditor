package plugins;

import java.util.ArrayList;

import common.TLEData;

import javafx.scene.Node;


public class DGPhysics{
	
	public DGPhysics(){
		
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
}