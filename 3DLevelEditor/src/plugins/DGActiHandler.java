package plugins;


public class DGActiHandler{
	
	private DemoGameWin mainWin;
	
	public DGActiHandler(DemoGameWin mw){
		mainWin = mw;
	}
	
	public void runActivator(String acti){
		String newActi = acti.toLowerCase();
		newActi = newActi.replaceAll("\\s+", "");
		
		System.out.println(newActi);
		
		if(!newActi.contains("when(pressed){")){
			return;
		}
		
		int ind = newActi.indexOf("when(pressed){");
		newActi = newActi.substring(ind);
		ind = newActi.indexOf('{');
		newActi = newActi.substring(ind);
		int fro = 1;
		int bac = 0;
		ind = 0;
		
		while(fro != bac && ind < newActi.length()){
			ind++;
			if(newActi.charAt(ind) == '{'){
				fro++;
			}
			else if(newActi.charAt(ind) == '}'){
				bac++;
			}		
		}
		newActi = newActi.substring(1, ind);
		
		System.out.println(newActi);
		
		ind = newActi.indexOf(';');
		
		while(ind != -1){
			String thiActi = newActi.substring(0, ind);
			performAction(thiActi);
			newActi = newActi.substring(ind + 1, newActi.length());
			ind = newActi.indexOf(';');
		}
		
	}
	
	
	private void performAction(String action){
		String command = action.substring(0, action.indexOf('('));
		System.out.println(command);
		switch(command){
		case "setlevel":{
			String val = action.substring(action.indexOf('(') + 1, action.indexOf(')'));
			System.out.println(val);
			mainWin.changeLevel("Level" + val);
		}
		break;
		}
	}
}