package ltm.client.gui.pathFinder;

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
	
	private static final long serialVersionUID = 1L;
	
	public mxGraph getGraph() {
		return mxGraph;
	}

	public void setGraph(mxGraph mxGraph) {
		this.mxGraph = mxGraph;
	}
	
	public GraphDrawer(int totalPos, String resultGraph, String shortestPath) {
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
	    
	    try
		{
			
			List<Object> placeList = new ArrayList<>();
			
			for(int i = 0; i < totalPos; i++)
				placeList.add(mxGraph.insertVertex(parent, null, "Position " + (i+1), i, i, 70, 50));
			
			StringTokenizer st = new StringTokenizer(resultGraph, " ");
			
			/////
			List<Integer> shortestPathList = new ArrayList<>();
			StringTokenizer sp = new StringTokenizer(shortestPath, " ");
			while(sp.hasMoreTokens())
			{
				shortestPathList.add(Integer.valueOf(sp.nextToken())-1);
			}
			/////
			
			
			for (int i = 0; i < totalPos; i++) {
				for (int j = 0; j < totalPos; j++) {
					String weight = st.nextToken();
					if (!weight.equals("0")) {
						if(shortestPathList.indexOf(i) >-1 && shortestPathList.indexOf(j) > -1)
							mxGraph.insertEdge(parent, null, weight, placeList.get(i), placeList.get(j), "strokeColor=red");
						else
							mxGraph.insertEdge(parent, null, weight, placeList.get(i), placeList.get(j), "endArrow=none");
					}
				}
			}
			
		}
		finally
		{
			mxCircleLayout layout = new mxCircleLayout(mxGraph);
			layout.execute(parent);
			mxGraph.getModel().endUpdate();
		}
	
		mxGraphComponent mxGraphComponent = new mxGraphComponent(mxGraph);
		mxGraphComponent.setBorder(BorderFactory.createEmptyBorder());
		this.setBorder(new EmptyBorder(20, 20, 10, 10));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.add(mxGraphComponent);
	}

	
	
	
}
