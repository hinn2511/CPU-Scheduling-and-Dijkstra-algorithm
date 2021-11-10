package ltm.client.bus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class filePicker {
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	//Chon file va lay duong dan file
	public void pickingFile() throws FileNotFoundException {
		if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChooser.getSelectedFile();
			
			Scanner input = new Scanner(file);
			
//			while (input.hasNext()) {
//				sb.append(input.nextLine());
//				sb.append("\n");
//			}
			
			setPath(file.getAbsolutePath());
			
			input.close();
		}
		else 
		{
//			sb.append("No file was selected");
		}
	}
}
