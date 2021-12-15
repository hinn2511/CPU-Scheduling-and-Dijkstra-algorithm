package ltm.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class Utility {
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

	// Doc file tim duong di ngan nhat
	public String readShortesPathFile(String path) {
		String result = "";

		try {
			Scanner read = new Scanner(new File(path));
			result = read.nextLine() + "|" + read.nextLine() + "|" + read.nextLine() + "|";
			boolean first = true;
			while (read.hasNextLine()) {
				if (first) {
					result += read.nextLine();
					first = false;
				} else
					result = result + "," + read.nextLine();
			}
			read.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	// Xuat do thi ra file anh jpg
	public void exportGraph(mxGraph graph) {
		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 2, Color.WHITE, true, null);
		try {
			String userHomeFolder = System.getProperty("user.home");
			ImageIO.write(image, "JPG", new File(userHomeFolder + "\\Downloads" + "\\graph.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
