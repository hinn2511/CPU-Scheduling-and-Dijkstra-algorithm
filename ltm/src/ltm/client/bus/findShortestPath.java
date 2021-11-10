package ltm.client.bus;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class findShortestPath {
	
	//Xuat do thi ra file anh jpg
	public void exportGraph(mxGraph graph) {
		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 2, Color.WHITE, true, null);
		try {
			ImageIO.write(image, "JPG", new File("C:\\Users\\\\Administrator\\graph.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
