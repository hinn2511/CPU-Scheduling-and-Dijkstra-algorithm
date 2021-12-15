package ltm.client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler_CPU {
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
			String temp="";
			int i=0;
			while (read.hasNextLine()){
				temp=read.nextLine();
				if(i==0 )
					result=temp;
				else
					result=result+","+temp;
				i++;
			}
			read.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return result;
	}

//	public static void main(String[] args) {
//		FileHandler f=new FileHandler();
//		f.setPath("C:\\Users\\quang\\Downloads\\fcfs.txt");
//		f.readShortesPathFile(f.getPath());
//		System.out.println("hai");
//	}
}
