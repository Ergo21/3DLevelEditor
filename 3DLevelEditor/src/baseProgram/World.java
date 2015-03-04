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
	
	public World(MainWindow mW){
		rootWindow = mW;
		worldData = new HashMap<String, ArrayList<TLEData>>();
		resetWindows = new HashMap<String, Runnable>();

        Box c2 = new Box(1, 1,1);
        c2.setMaterial(new PhongMaterial(Color.BLUE));

        Box c3 = new Box(1, 1,1);
        c3.getTransforms().add(new Scale(5,5,5));
        c3.setMaterial(new PhongMaterial(Color.RED));
        
        ArrayList<TLEData> cC = new ArrayList<TLEData>();
        TLEData t2 = new TLEData("Cube 1", "1", TLEType.CUBE);
        t2.setTranslateX(2);
        t2.setMesh(c2);
        t2.setColour(Color.BLUE);
        TLEData t3 = new TLEData("Cube 2", "2", TLEType.CUBE);
        t3.setMesh(c3);
        t3.setColour(Color.RED);
        t3.setTranslateX(-10);
        cC.add(t2);
        cC.add(t3);
        
        PointLight light = new PointLight();
        light.setColor(Color.WHITE);
        //light.setTranslateY(-3);
        Box lightMesh = new Box(1,1,1);
        lightMesh.setMaterial(new PhongMaterial(Color.YELLOW));
        lightMesh.setDrawMode(DrawMode.LINE);
        lightMesh.getTransforms().add(new Scale(2,2,2));
        TLEData l1 = new TLEData("Light 1", "1", TLEType.LIGHT);
        l1.setLight(light);
        l1.setColour(Color.YELLOW);
        l1.setMesh(lightMesh);
        cC.add(l1);
      
        worldData.put("Level1", cC);
        worldData.put("CurrentLevel", cC);
        worldData.put("Meshes", new ArrayList<TLEData>());
        rootWindow.addMenuBarItem(event -> clearLevel(), "File", "Delete Current Level");
	}
	
	public void setData(HashMap<String, ArrayList<TLEData>> d){
		if(d != null){
			worldData = d;
		}
	}
	
	public HashMap<String, ArrayList<TLEData>> getData(){
		return worldData; 
	}
	
	public void clearLevel(){
		worldData.get("CurrentLevel").clear();
		Box c = new Box(1, 1, 1);
		c.setMaterial(new PhongMaterial(Color.ORANGE));
		c.setTranslateX(-10);
        TLEData t = new TLEData("Cube 1", "1", TLEType.CUBE);
        t.setMesh(c);
        worldData.get("CurrentLevel").add(t);
        runResetWindow();
	}
	
	public void addResetWindow(String rN, Runnable rW){
		resetWindows.put(rN, rW);
	}
	
	public void runResetWindow(){
		Object[] s = resetWindows.keySet().toArray();
		for(int i = 0; i < s.length; i++){
			resetWindows.get(s[i]).run();
		}
	}
	
	public void setModelLoader(Function<File, TLEData> f){
		modLoad = f;
	}
	
	public TLEData runModelLoader(String f){
		TLEData t = null;
		
		if(modLoad != null){
			t = modLoad.apply(new File(f));
		}
		
		return t;
	}
	
	/*class Cube extends Box implements Serializable {
		private static final long serialVersionUID = 7245809316554124934L;
		final Rotate rx = new Rotate(0, Rotate.X_AXIS);
        final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
        final Rotate rz = new Rotate(0, Rotate.Z_AXIS);

        public Cube(double size, Color color) {
            super(size, size, size);
            setMaterial(new PhongMaterial(color));
            getTransforms().addAll(rz, ry, rx);
        }
    }*/
}