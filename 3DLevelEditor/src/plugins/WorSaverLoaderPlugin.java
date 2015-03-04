package plugins;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.LightBase;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;
import common.Global.TLEType;
import common.TLEData;
import common.TLEPlugin;

public class WorSaverLoaderPlugin extends TLEPlugin {
	
	public WorSaverLoaderPlugin(){
		pluginName = "WorSaverLoaderPlugin";
	}
	
	/**
	 * Adds new buttons under File. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> saveWorldObj(), "File", "Save as", "World Save file");
		mainPlMan.getMWin().addMenuBarItem(event -> loadWorldObj(), "File", "Load", "World Save file");
	}
	
	
	/**
	 * Saves World as .wsf into chosen location.
	 */
	public void saveWorldObj(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser saveLoc = new FileChooser();
		saveLoc.setTitle("Save World File");
		saveLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Save File", "*.wsf"));
		File file = saveLoc.showSaveDialog(stage);
		if(file == null){
			return;
		}
		
		WorldSave savEnc = new WorldSave(mainPlMan.getWorld().getData());
		try{
			ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
			oos.writeObject(savEnc);
			oos.close();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Loads chosen .wsf save into World.
	 */
	public void loadWorldObj(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser loadLoc = new FileChooser();
		loadLoc.setTitle("Load World Save");
		loadLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Save File", "*.wsf"));
		File file = loadLoc.showOpenDialog(stage);
		if(file == null){
			return;
		}
		WorldSave data = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()));
			try{
				data = (WorldSave) ois.readObject();
				ois.close();
			}
			catch(ClassNotFoundException ce){
				System.out.println(ce.getMessage());
			}	
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		
		HashMap<String, ArrayList<TLEData>> uEData = decryptWorldSave(data);
		
		if(uEData != null){
			mainPlMan.getWorld().setData(uEData);
		}
		else{
			System.out.println("uEData is null");
		}
		
		mainPlMan.getWorld().runResetWindow();
	}
	
	/**
	 * Turns WorldSave back into World.
	 * @param worSav WorldSave to be decrypted.
	 * @return Decrypted World.
	 */
	public HashMap<String, ArrayList<TLEData>> decryptWorldSave(WorldSave worSav){
		if(worSav == null){
			System.out.println("worSav is null");
			return null;
		}
		
		HashMap<String, ArrayList<TLEData>> data = new HashMap<String, ArrayList<TLEData>>();
		HashMap<String, ArrayList<NodeSave>> nodSav = worSav.getWorldSave();
		
		String[] keys = new String[0];
		keys = nodSav.keySet().toArray(keys);
		
		for(int i = 0; i < keys.length; i++){
			ArrayList<TLEData> curTLE = new ArrayList<TLEData>();
			for(int j = 0; j < nodSav.get(keys[i]).size(); j++){
				TLEData add = decryptNodeSave(nodSav.get(keys[i]).get(j));
				if(add != null){
					curTLE.add(add);
				}				
			}
			data.put(keys[i], curTLE);
			System.out.println(keys[i] + " added to data");
		}	
		
		return data;
	}
	
	/**
	 * Turns NodeSave back into TLEData.
	 * @param nS NodeSave to decrypt.
	 * @return Decrypted TLEData.
	 */
	public TLEData decryptNodeSave(NodeSave nS){
		if(nS == null){
			System.out.println("nS is null");
		}
		TLEType t = TLEType.valueOf(nS.getType());
		TLEData newTLE = new TLEData(nS.getName(), nS.getSaveId(), t);
		System.out.println(nS.getType());
		System.out.println(t);
		if(t == TLEType.LIGHT){
			System.out.println("t is Light");
			if(nS.getObjColour() == null){
				System.out.println("colour is null");
			}
		}

		if(!nS.getFilePath().equals(null) && t == TLEType.MESH){
			newTLE = mainPlMan.getWorld().runModelLoader(nS.getFilePath());
			if(newTLE == null){
				return null;
			}
			newTLE.setName(nS.getName());
			newTLE.setID(nS.getSaveId());
			newTLE.getMesh().getTransforms().add(makeTransforms(nS.getMeshTransforms()));
		} else if(t == TLEType.LIGHT && nS.getObjColour() != null){
			System.out.println("Light found");
	        Box lightMesh = new Box(1,1,1);
	        lightMesh.setMaterial(new PhongMaterial(Color.YELLOW));
	        lightMesh.setDrawMode(DrawMode.LINE);
	        newTLE.setLight(makeLight(nS.getObjColour()));
	        newTLE.setMesh(lightMesh);
	        newTLE.getMesh().getTransforms().add(makeTransforms(nS.getMeshTransforms()));
	        Affine a = makeTransforms(nS.getMeshTransforms());
	        a.appendScale(0,0,0);
	        newTLE.getLight().getTransforms().add(a);
		}
		else if(t == TLEType.CUBE && nS.getObjColour() != null){
			Box cubeMesh = new Box(1,1,1);
			Color c = new Color(
					nS.getObjColour().get("red"),
					nS.getObjColour().get("green"),
					nS.getObjColour().get("blue"),
					nS.getObjColour().get("alpha"));
			cubeMesh.setMaterial(new PhongMaterial(c));
			newTLE.setColour(c);
			newTLE.setMesh(cubeMesh);
	        newTLE.getMesh().getTransforms().add(makeTransforms(nS.getMeshTransforms()));
		}
		else if(t == TLEType.ACTIVATOR){
			Box actiMesh = new Box(1,1,1);
			actiMesh.setMaterial(new PhongMaterial(Color.RED));
        	actiMesh.setDrawMode(DrawMode.LINE);
        	newTLE.setMesh(actiMesh);
        	newTLE.setActivator(nS.getActivator());
        	newTLE.getMesh().getTransforms().add(makeTransforms(nS.getMeshTransforms()));
		}
		else if(t == TLEType.SOUND){
			Box sounMesh = new Box(1,1,1);
			sounMesh.setMaterial(new PhongMaterial(Color.AQUAMARINE));
			sounMesh.setDrawMode(DrawMode.LINE);
			newTLE.setMesh(sounMesh);
			newTLE.getMesh().getTransforms().add(makeTransforms(nS.getMeshTransforms()));
		}
		
		return newTLE;
	}
	
	/**
	 * Takes saved transform and reimplements it.
	 * @param tra Saved transform
	 * @return Loaded transform
	 */
	public Affine makeTransforms(HashMap<String, Double> tra){
		Affine neTrans = new Affine();
		
		neTrans.setMxx(tra.get("Mxx"));
		neTrans.setMxy(tra.get("Mxy"));
		neTrans.setMxz(tra.get("Mxz"));
		neTrans.setMyx(tra.get("Myx"));
		neTrans.setMyy(tra.get("Myy"));
		neTrans.setMyz(tra.get("Myz"));
		neTrans.setMzx(tra.get("Mzx"));
		neTrans.setMzy(tra.get("Mzy"));
		neTrans.setMzz(tra.get("Mzz"));
		neTrans.setTx(tra.get("Tx"));
		neTrans.setTy(tra.get("Ty"));
		neTrans.setTz(tra.get("Tz"));
		
		return neTrans;
	}
	
	public LightBase makeLight(HashMap<String, Double> col){
		PointLight l = new PointLight();
		l.setColor(new Color(col.get("red"),col.get("green"), col.get("blue"), col.get("alpha")));
		return l;
	}
	
}