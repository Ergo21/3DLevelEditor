package plugins;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.fxml.FxmlModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;

import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import common.NodeSave;
import common.TLEData;
import common.WorldSave;
import common.Global.TLEType;

public class DemoGameLoader{
	
	public DemoGameLoader(){
		
	}
	

	/**
	 * Loads chosen .wsf save into World.
	 */
	public HashMap<String, ArrayList<TLEData>> loadWorldObj(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser loadLoc = new FileChooser();
		loadLoc.setTitle("Load World Save");
		loadLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Save File", "*.wsf"));
		File file = loadLoc.showOpenDialog(stage);
		if(file == null){
			return null;
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
			return uEData;
		}
		else{
			System.out.println("uEData is null");
		}
		
		return null;
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
			newTLE = readFile(new File(nS.getFilePath()));
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
	
	private TLEData readFile(File f){
		
		if(f.getName().endsWith(".obj")){
			ObjModelImporter mI = new ObjModelImporter();
			mI.read(f);
		 	
		 	MeshView[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		TLEData t = new TLEData(f.getName(), "obj1", TLEType.MESH);
		 		t.setFilePath(f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 		
		 	}
		 	
		}
		else if(f.getName().endsWith(".3ds")){
			TdsModelImporter mI = new TdsModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "3ds1", TLEType.MESH);
		 		t.setFilePath(f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		 	
		}
		else if(f.getName().endsWith(".dae") || f.getName().endsWith(".zae")){
			ColModelImporter mI = new ColModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "d/zae1", TLEType.MESH);
		 		t.setFilePath(f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		}
		else if(f.getName().endsWith(".fxml")){
			FxmlModelImporter mI = new FxmlModelImporter();
			mI.read(f);
		 	
		 	Node models = mI.getImport();
		 	mI.close();
		 	
		 	TLEData t = new TLEData(f.getName(), "fxml1", TLEType.MESH);
		 	t.setFilePath(f.getAbsolutePath());
	 		t.setMesh(models);
	 		if(!checkIfLoaded(t)){
	 			return t;
	 		}
	 		else{
	 			System.out.println("File already loaded");
	 		}
		}
		else if(f.getName().endsWith(".stl")){
			StlMeshImporter mI = new StlMeshImporter();
			mI.read(f);
		 	
		 	TriangleMesh models = mI.getImport();
		 	mI.close();
		 	MeshView mV = new MeshView(models);
		 	
		 	TLEData t = new TLEData(f.getName(), "stl1", TLEType.MESH);
		 	t.setFilePath(f.getAbsolutePath());
	 		t.setMesh(mV);
	 		if(!checkIfLoaded(t)){
	 			return t;
	 		}
	 		else{
	 			System.out.println("File already loaded");
	 		}
		}
		else if(f.getName().endsWith(".x3d") || f.getName().endsWith(".x3dz")){
			X3dModelImporter mI = new X3dModelImporter();
			mI.read(f);
		 	
		 	Node[] models = mI.getImport();
		 	mI.close();
		 	
		 	if(models != null && models.length > 0){ 
		 		Group gNode = new Group();
		 		gNode.getChildren().addAll(models);
		 		
		 		TLEData t = new TLEData(f.getName(), "x3d/z1", TLEType.MESH);
		 		t.setFilePath(f.getAbsolutePath());
		 		t.setMesh(gNode);
		 		if(!checkIfLoaded(t)){
		 			return t;
		 		}
		 		else{
		 			System.out.println("File already loaded");
		 		}
		 	}
		}
	 	
		return null;
	}
	
	/**
	 * Currently not implemented as Meshes are not either cloned or inherited, so is reloaded.
	 * @param t Model to be checked.
	 * @return Is the model t already loaded?
	 */
	public boolean checkIfLoaded(TLEData t){
		/*ArrayList<TLEData> m = mainPlMan.getWorld().getData().get("Meshes");
		for(int i = 0; i < m.size(); i++){
			if(m.get(i).getName().equals(t.getName())){
				return true;
			}
		}*/
		return false;
	}
}