package plugins;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import baseProgram.PluginManager;
import common.TLEData;
import common.TLEPlugin;

public class WorSaverLoaderPlugin extends TLEPlugin {
	private WorEncDec wEnDe;
	
	public WorSaverLoaderPlugin(){
		pluginName = "WorSaverLoaderPlugin";
		wEnDe = new WorEncDec();
	}
	
	/**
	 * Adds new buttons under File. 
	 * 
	 * @param pM	PluginManager for the Plugin to interact with the Base Program
	 */
	@Override
	public void install(PluginManager pM) {
		mainPlMan = pM;
		mainPlMan.getMWin().addMenuBarItem(event -> saveWorldTxt(), "File", "Save as", "Text file");
		mainPlMan.getMWin().addMenuBarItem(event -> loadWorldTxt(), "File", "Load", "Text file");
	}
	
	public void saveWorldTxt(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser saveLoc = new FileChooser();
		saveLoc.setTitle("Save World Text File");
		saveLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Text File", "*.txt"));
		File file = saveLoc.showSaveDialog(stage);
		if(file == null){
			return;
		}
		
		ArrayList<String> savEnc = wEnDe.encryptText(mainPlMan.getWorld().getData());
		try{
			ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
			oos.writeObject(savEnc);
			oos.close();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void loadWorldTxt(){
		Stage stage = new Stage();
		stage.setWidth(200);
		stage.setHeight(200);
		
		FileChooser loadLoc = new FileChooser();
		loadLoc.setTitle("Load World Text File");
		loadLoc.getExtensionFilters().add(new FileChooser.ExtensionFilter("World Text File", "*.txt"));
		File file = loadLoc.showOpenDialog(stage);
		if(file == null){
			return;
		}
		ArrayList<String> data = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file.toPath()));
			try{
				data = (ArrayList<String>) ois.readObject();
				ois.close();
			}
			catch(ClassNotFoundException ce){
				System.out.println(ce.getMessage());
			}	
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		
		HashMap<String, ArrayList<TLEData>> uEData = wEnDe.decryptText(data);
		
		mainPlMan.getWorld().setData(uEData);
		mainPlMan.getWorld().runResetWindow();
	}
}