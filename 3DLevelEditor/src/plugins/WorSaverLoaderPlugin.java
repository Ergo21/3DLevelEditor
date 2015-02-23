package plugins;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.fxml.FxmlModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;
import common.TLEData;
import common.TLEPlugin;

public class WorSaverLoaderPlugin extends TLEPlugin {
	private File currentFile;
	
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
		mainPlMan.getMWin().addMenuBarItem(event -> loadModel(), "Plugins", "3D Model Loader", "Load New Model");
		mainPlMan.getMWin().addMenuBarItem(event -> saveWorldObj(), "File", "Save as", "World Save file");
		mainPlMan.getMWin().addMenuBarItem(event -> loadWorldObj(), "File", "Load", "World Save file");
		
		Function<File,TLEData> readFileFunc = f -> {
			TLEData t = readFile(f);
			return t;
		};
		mainPlMan.getWorld().setModelLoader(readFileFunc);
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
		TLEData newTLE = new TLEData(nS.getName(), nS.getSaveId(), nS.getMeshPath());

		if(!nS.getMeshPath().equals(null) && !nS.getMeshPath().equals("NA")){
			newTLE = readFile(new File(nS.getMeshPath()));
			if(newTLE == null){
				return null;
			}
			newTLE.setName(nS.getName());
			newTLE.setID(nS.getSaveId());
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
	
	/**
	 * Opens a window to load a valid model. Activated from "Plugins/3D Model Loader/Load Model" button. 
	 */
	public void loadModel(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
		        new FileChooser.ExtensionFilter("OBJ Model Files", "*.obj"),
		        new FileChooser.ExtensionFilter("3DS Model Files", "*.3ds"),
		        new FileChooser.ExtensionFilter("COLLADA Model Files", "*.dae", "*.zae"),
		        new FileChooser.ExtensionFilter("FXML Model Files", "*.fxml"),
		        new FileChooser.ExtensionFilter("STL Model Files", "*.stl"),
		        new FileChooser.ExtensionFilter("X3D Model Files", "*.x3d", "*.x3dz"));
		 
		if(currentFile != null){
			fileChooser.setInitialDirectory(currentFile.getParentFile());
		}
		 
		currentFile = fileChooser.showOpenDialog(stage);
		 
		if(currentFile != null){
			TLEData t = readFile(currentFile);
			if(t != null){
				mainPlMan.getWorld().getData().get("Meshes").add(t);
			}
		}
		mainPlMan.getWorld().runResetWindow();
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
		 		TLEData t = new TLEData(f.getName(), "obj1", f.getAbsolutePath());
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
		 		
		 		TLEData t = new TLEData(f.getName(), "3ds1", f.getAbsolutePath());
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
		 		
		 		TLEData t = new TLEData(f.getName(), "d/zae1", f.getAbsolutePath());
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
		 	
		 	TLEData t = new TLEData(f.getName(), "fxml1", f.getAbsolutePath());
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
		 	
		 	TLEData t = new TLEData(f.getName(), "stl1", f.getAbsolutePath());
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
		 		
		 		TLEData t = new TLEData(f.getName(), "x3d/z1", f.getAbsolutePath());
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