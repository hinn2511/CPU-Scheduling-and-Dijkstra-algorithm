package ltm.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {
	JFileChooser fileChooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");

	StringBuilder sb = new StringBuilder();
	String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	// Chon file va lay duong dan file
	public void pickingFile() throws FileNotFoundException {
		fileChooser.setFileFilter(filter);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			Scanner input = new Scanner(file);

			setPath(file.getAbsolutePath());

			input.close();
		}
	}

	//Doc file tim duong di ngan nhat
	public String readShortesPathFile(String path) {
		String result = "";
		
		try {
			Scanner read = new Scanner(new File(path));
			result = read.nextLine() + "|" + read.nextLine() + "|" + read.nextLine() + "|" + read.nextLine() + "|";
			boolean first = true;
			while (read.hasNextLine()) {
				if(first) {
					result += read.nextLine();
					first = false;
				}
				else
					result = result + "," + read.nextLine();
			}
			read.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
