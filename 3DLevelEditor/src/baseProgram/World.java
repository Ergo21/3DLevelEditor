package baseProgram;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import common.*;

/**
 * The Level Editor's Data structure, all plugins read and edit this data.
 * @author Ergo21
 *
 */
public class World {
	MainWindow rootWindow;
	HashMap<String, ArrayList<TLEData>> worldData;
	HashMap<String, Runnable> resetWindows;
	
	public World(MainWindow mW){
		rootWindow = mW;
		worldData = new HashMap<String, ArrayList<TLEData>>();
		resetWindows = new HashMap<String, Runnable>();

        Cube c2 = new Cube(1, Color.BLUE);
        c2.setTranslateX(2);
        c2.rx.setAngle(45);
        c2.ry.setAngle(45);

        Cube c3 = new Cube(5, Color.RED);
        c3.setTranslateX(-10);
        c3.rx.setAngle(45);
        c3.ry.setAngle(45);
        
        ArrayList<TLEData> cC = new ArrayList<TLEData>();
        TLEData t2 = new TLEData("Cube 1", "1");
        t2.setMesh(c2);
        TLEData t3 = new TLEData("Cube 2", "2");
        t3.setMesh(c3);
        cC.add(t2);
        cC.add(t3);
        
        worldData.put("CurrentLevel", cC);
        worldData.put("Meshes", new ArrayList<TLEData>());
        rootWindow.addMenuBarItem(event -> clearLevel(), "File", "Delete Current Level");
	}
	
	public HashMap<String, ArrayList<TLEData>> getData(){
		return worldData; 
	}
	
	public void clearLevel(){
		worldData.get("CurrentLevel").clear();
		Cube c = new Cube(2, Color.AZURE);
		c.setTranslateX(-10);
        c.rx.setAngle(45);
        c.ry.setAngle(45);
        TLEData t = new TLEData("Cube 3", "3");
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
	
	class Cube extends Box {

        final Rotate rx = new Rotate(0, Rotate.X_AXIS);
        final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
        final Rotate rz = new Rotate(0, Rotate.Z_AXIS);

        public Cube(double size, Color color) {
            super(size, size, size);
            setMaterial(new PhongMaterial(color));
            getTransforms().addAll(rz, ry, rx);
        }
    }
}