package ltm.client.gui.pathFinder;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import ltm.server.pathFinder.Edge;

public class GraphDrawer extends JPanel {
	mxGraph mxGraph;
	int count;

	private static final long serialVersionUID = 1L;

	public mxGraph getGraph() {
		return mxGraph;
	}

	public void setGraph(mxGraph mxGraph) {
		this.mxGraph = mxGraph;
	}

	public GraphDrawer(String placeList, String startPlace, String endPlace, String pathGraph,
			String shortestPathGraph) {
		mxGraph = new mxGraph();
		Object parent = mxGraph.getDefaultParent();

		mxGraph.getModel().beginUpdate();

		mxStylesheet stylesheet = mxGraph.getStylesheet();
		Hashtable<String, Object> style = new Hashtable<>();
		stylesheet.putCellStyle("ROUNDED", style);

		Map<String, Object> vertexStyle = stylesheet.getDefaultVertexStyle();
		vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
		vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		vertexStyle.put(mxConstants.STYLE_AUTOSIZE, 1);
		vertexStyle.put(mxConstants.STYLE_SPACING, "10");
		vertexStyle.put(mxConstants.STYLE_ORTHOGONAL, "true");
		vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);

		try {

			List<Object> places = new ArrayList<>();
			StringTokenizer placeTokenizer = new StringTokenizer(placeList, ",");
			count = placeTokenizer.countTokens();
			for (int i = 0; i < count; i++) {
				String place = placeTokenizer.nextToken();
				if (place.equals(startPlace) || place.equals(endPlace))
					places.add(mxGraph.insertVertex(parent, null, place, i, i, 70, 50,
							"strokeColor=red"));
				else
					places.add(mxGraph.insertVertex(parent, null, place, i, i, 70, 50));
			}

			StringTokenizer pathGraphTokenizer = new StringTokenizer(pathGraph, " ");
			StringTokenizer shortestPathGraphTokenizer = new StringTokenizer(shortestPathGraph, " ");

			for (int i = 0; i < count; i++) {
				for (int j = 0; j < count; j++) {
					String weight = pathGraphTokenizer.nextToken();
					String shortest = shortestPathGraphTokenizer.nextToken();
					if (!weight.equals("0")) {
						if (shortest.equals("x"))
							mxGraph.insertEdge(parent, null, weight, places.get(i), places.get(j), "strokeColor=red");
						else
							mxGraph.insertEdge(parent, null, weight, places.get(i), places.get(j));
					}

				}
			}

		} finally {
			mxCircleLayout layout = new mxCircleLayout(mxGraph);
			layout.setRadius(count * 30);
			layout.execute(parent);
			mxParallelEdgeLayout layout2 = new mxParallelEdgeLayout(mxGraph);
			layout2.execute(parent);
			mxGraph.getModel().endUpdate();
		}

		mxGraphComponent mxGraphComponent = new mxGraphComponent(mxGraph);
		mxGraphComponent.setBorder(BorderFactory.createEmptyBorder());
		mxGraphComponent.setEnabled(false);
		this.setBorder(new EmptyBorder(20, 20, 10, 10));

		JPanel panel = new JPanel();
		add(panel);
		panel.add(mxGraphComponent);
	}

}
