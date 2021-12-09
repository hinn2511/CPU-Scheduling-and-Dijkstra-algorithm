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

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class Utility {
	
	//Xuat do thi ra file anh jpg
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
