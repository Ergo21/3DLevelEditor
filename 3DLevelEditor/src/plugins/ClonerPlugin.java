package plugins;

import java.util.function.Function;

import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import baseProgram.PluginManager;
import common.*;

/**
 * This plugin creates a simple game to test the current World.
 * @author Ergo21
 *
 */

public class ClonerPlugin extends TLEPlugin {
	
	public ClonerPlugin(){
		pluginName = "ClonerPlugin";
	}
	
	/**
	 * Adds new function to baseProgram.
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		
		Function<TLEData,TLEData> cloneTLEFunc = t -> {
			TLEData cT = cloneTLEData(t);
			return cT;
		};
		mainPlMan.getWorld().setCloneTLE(cloneTLEFunc);
	}
	
	
	public TLEData cloneTLEData(TLEData t){
		TLEData clone = new TLEData(t.getName(), t.getID(), t.getType());
		
		clone.setFilePath(t.getFilePath());
		
		switch(t.getType())
		{
			case ACTIVATOR: {
				Box actiMesh = new Box(1,1,1);
				actiMesh.setMaterial(new PhongMaterial(Color.RED));
	        	actiMesh.setDrawMode(DrawMode.LINE);
	        	clone.setMesh(actiMesh);
				clone.setActivator(t.getActivator());
				
				Transform lis = t.getMesh().getLocalToSceneTransform();
				clone.getMesh().getTransforms().add(lis.clone());
			}
			break;
			case CUBE: {
				Color colour = t.getColour();
				colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getOpacity());
				clone.setColour(colour);
				
				Box cubeMesh = new Box(1,1,1);
				cubeMesh.setMaterial(new PhongMaterial(colour));
				clone.setColour(colour);
				clone.setMesh(cubeMesh);
				
				Transform lis = t.getMesh().getLocalToSceneTransform();
				clone.getMesh().getTransforms().add(lis.clone());
			}
			break;
			case LIGHT: {
				Color colour = t.getLight().getColor();
				clone.setColour(new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getOpacity()));
				
				Box lightMesh = new Box(1,1,1);
		        lightMesh.setMaterial(new PhongMaterial(Color.YELLOW));
		        lightMesh.setDrawMode(DrawMode.LINE);
		        PointLight p = new PointLight();
		        p.setColor(colour);
		        clone.setLight(p);
		        clone.setMesh(lightMesh);
		        Affine lis = new Affine(t.getMesh().getLocalToSceneTransform().clone());
		        /*lis = new Affine(t.getMesh().getLocalToSceneTransform().clone());
		        lis.appendScale(0.5,0.5,0.5);
		        clone.getLight().getTransforms().add(lis);*/
		        
		        
				clone.getTransforms().add(lis);
				
			}
			break;
			case MESH: {
					clone = mainPlMan.getWorld().runModelLoader(t.getFilePath());
					if(clone == null){
						return null;
					}
					clone.setName(t.getName());
					clone.setID(t.getID());

					Transform lis = t.getMesh().getLocalToSceneTransform();
					clone.getMesh().getTransforms().add(lis.clone());
			}
			break;
			case SOUND: {
				Box sounMesh = new Box(1,1,1);
				sounMesh.setMaterial(new PhongMaterial(Color.AQUAMARINE));
				sounMesh.setDrawMode(DrawMode.LINE);
				clone.setMesh(sounMesh);
				Transform lis = t.getMesh().getLocalToSceneTransform();
				clone.getMesh().getTransforms().add(lis.clone());
			}
			break;
			default: {
			}
		}
		
		return clone;
	}
}