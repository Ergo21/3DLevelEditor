package baseProgram;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
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

        Box c2 = new Box(1, 1,1);
        c2.setTranslateX(2);
        c2.setMaterial(new PhongMaterial(Color.BLUE));

        Box c3 = new Box(5, 5,5);
        c3.setTranslateX(-10);
        c3.setMaterial(new PhongMaterial(Color.RED));
        
        ArrayList<TLEData> cC = new ArrayList<TLEData>();
        TLEData t2 = new TLEData("Cube 1", "1", "NA");
        t2.setMesh(c2);
        TLEData t3 = new TLEData("Cube 2", "2", "NA");
        t3.setMesh(c3);
        cC.add(t2);
        cC.add(t3);
        
        ArrayList<TLEData> l2 = new ArrayList<TLEData>();
        TLEData t4 = new TLEData("Cube 4", "4", "NA");
        Box c4 = new Box(5, 5,5);
        c4.setMaterial(new PhongMaterial(Color.AZURE));
        t4.setMesh(c4);
        l2.add(t4);
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
		Box c = new Box(2, 2, 2);
		c.setMaterial(new PhongMaterial(Color.AZURE));
		c.setTranslateX(-10);
        TLEData t = new TLEData("Cube 3", "3", "NA");
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