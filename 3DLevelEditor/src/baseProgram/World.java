package baseProgram;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import common.*;
import common.Global.TLEType;

/**
 * The Level Editor's Data structure, all plugins read and edit this data.
 * @author Ergo21
 *
 */
public class World {
	private MainWindow rootWindow;
	private HashMap<String, ArrayList<TLEData>> worldData;
	private HashMap<String, Runnable> resetWindows;
	private Function<File, TLEData> modLoad;
	private Function<TLEData, TLEData> cloneTLE;
	
	public World(MainWindow mW){
		rootWindow = mW;
		worldData = new HashMap<String, ArrayList<TLEData>>();
		resetWindows = new HashMap<String, Runnable>();
		ArrayList<TLEData> cC = new ArrayList<TLEData>();

		worldData.put("Level1", cC);
        worldData.put("CurrentLevel", cC);
        worldData.put("Meshes", new ArrayList<TLEData>());

        Box c2 = new Box(1, 1,1);
        c2.setMaterial(new PhongMaterial(Color.BLUE));

        Box c3 = new Box(1, 1,1);
        c3.getTransforms().add(new Scale(5,5,5));
        c3.setMaterial(new PhongMaterial(Color.RED));
        
        
        TLEData t2 = new TLEData("Cube 1", getNewID(), TLEType.CUBE);
        t2.setTranslateX(2);
        t2.setMesh(c2);
        t2.setColour(Color.BLUE);
        cC.add(t2);
        
        TLEData t3 = new TLEData("Cube 2", getNewID(), TLEType.CUBE);
        t3.setMesh(c3);
        t3.setColour(Color.RED);
        t3.setTranslateX(-10);       
        cC.add(t3);
        
        PointLight light = new PointLight();
        light.setColor(Color.WHITE);
        //light.setTranslateY(-3);
        Box lightMesh = new Box(1,1,1);
        lightMesh.setMaterial(new PhongMaterial(Color.YELLOW));
        lightMesh.setDrawMode(DrawMode.LINE);
        lightMesh.getTransforms().add(new Scale(2,2,2));
        TLEData l1 = new TLEData("Light 1", getNewID(), TLEType.LIGHT);
        l1.setLight(light);
        l1.setColour(Color.YELLOW);
        l1.setMesh(lightMesh);
        cC.add(l1);
      
        
        Box c = new Box(1, 1, 1);
		c.setMaterial(new PhongMaterial(Color.WHITE));
		c.setTranslateX(-10);
		c.setTranslateY(10);
		c.setScaleX(100);
		c.setScaleZ(100);
        TLEData t = new TLEData("Floor", getNewID(), TLEType.CUBE);
        t.setMesh(c);
        t.setColour(Color.WHITE);
        cC.add(t);
        
        rootWindow.addMenuBarItem(event -> clearLevel(), "File", "Delete Current Level");
	}
	
	public void setData(HashMap<String, ArrayList<TLEData>> d){
		if(d != null){
			worldData = d;
		}
	}
	
	/**
	 * Gets the loaded world data.
	 * @return World data
	 */
	public HashMap<String, ArrayList<TLEData>> getData(){
		return worldData; 
	}
	
	/**
	 * Returns unique id for the current level.
	 * @return Unique ID
	 */
	public String getNewID(){
		ArrayList<TLEData> curLev = worldData.get("CurrentLevel");
		
		int id = 0;
		boolean nIDFon = false;
		
		while(!nIDFon){
			id++;
			nIDFon = true;
			for(int i = 0; i < curLev.size(); i++){
				if(curLev.get(i).getID().equals(Integer.toString(id))){
					nIDFon = false;
					break;
				}
			}
		}
		
		return Integer.toString(id);
	}
	
	/**
	 * Deletes everything in the level.
	 */
	public void clearLevel(){
		worldData.get("CurrentLevel").clear();
		Box c = new Box(1, 1, 1);
		c.setMaterial(new PhongMaterial(Color.WHITE));
		c.setTranslateX(-10);
		c.getTransforms().add(new Translate(0, 10, 0));
		c.setTranslateY(10);
		c.setScaleX(100);
		c.setScaleZ(100);
        TLEData t = new TLEData("Floor", getNewID(), TLEType.CUBE);
        t.setMesh(c);
        t.setColour(Color.WHITE);
        worldData.get("CurrentLevel").add(t);

        runResetWindow();
	}
	
	/**
	 * Adds method to update window.
	 * @param rN Window name.
	 * @param rW Window reset method.
	 */
	public void addResetWindow(String rN, Runnable rW){
		resetWindows.put(rN, rW);
	}
	
	/**
	 * Updates the other windows if the Data gets changed.
	 */
	public void runResetWindow(){
		Object[] s = resetWindows.keySet().toArray();
		for(int i = 0; i < s.length; i++){
			resetWindows.get(s[i]).run();
		}
	}
	
	/**
	 * Sets the model loader function the editor will use to read models.
	 * @param f Function
	 */
	public void setModelLoader(Function<File, TLEData> f){
		modLoad = f;
	}
	
	/**
	 * This runs the current model loader, finding the model in directory f and returns the model as TLEData.
	 * @param f Model directory
	 * @return The model found in f, returned as TLEData.
	 */
	public TLEData runModelLoader(String f){
		TLEData t = null;
		
		if(modLoad != null){
			t = modLoad.apply(new File(f));
		}
		
		return t;
	}
	
	/**
	 * Sets the TLEData cloner.
	 * @param f The Clone function.
	 */
	public void setCloneTLE(Function<TLEData, TLEData> f){
		cloneTLE = f;
	}
	
	/**
	 * Clones and returns clone of the given TLEData.
	 * @param oT Original TLEData
	 * @return Clone of TLEData
	 */
	public TLEData runCloneTLE(TLEData oT){
		TLEData t = null;
		
		if(cloneTLE != null){
			t = cloneTLE.apply(oT);
		}
		
		return t;
	}
}