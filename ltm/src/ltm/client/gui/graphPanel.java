package ltm.client.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class graphPanel extends JPanel {
	mxGraph graph;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public graphPanel() {
		graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		
	    try
		{
			
			List<Object> placeList = new ArrayList<>();
			for(int i = 0; i < 13; i++)
			{
				placeList.add(graph.insertVertex(parent, null, "Position " + (i+1), i, i, 70,
					50));
			}
			graph.insertEdge(parent, null, "5", placeList.get(0), placeList.get(1), "strokeColor=red;");
			graph.insertEdge(parent, null, "8", placeList.get(0), placeList.get(3));
			graph.insertEdge(parent, null, "3", placeList.get(0), placeList.get(2));
			graph.insertEdge(parent, null, "8", placeList.get(2), placeList.get(3), "strokeColor=red;");
			graph.insertEdge(parent, null, "1", placeList.get(1), placeList.get(2), "strokeColor=red;");
			graph.insertEdge(parent, null, "8", placeList.get(1), placeList.get(3));
			graph.insertEdge(parent, null, "9", placeList.get(2), placeList.get(1));
			graph.insertEdge(parent, null, "4", placeList.get(2), placeList.get(0));
			graph.insertEdge(parent, null, "8", placeList.get(0), placeList.get(5));
			graph.insertEdge(parent, null, "3", placeList.get(0), placeList.get(8));
			graph.insertEdge(parent, null, "8", placeList.get(3), placeList.get(9));
			graph.insertEdge(parent, null, "3", placeList.get(1), placeList.get(10));
			graph.insertEdge(parent, null, "11", placeList.get(3), placeList.get(4), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(4), placeList.get(5), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(5), placeList.get(6), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(6), placeList.get(7), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(7), placeList.get(8), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(8), placeList.get(9), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(9), placeList.get(10), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(10), placeList.get(11), "strokeColor=red;");
			graph.insertEdge(parent, null, "4", placeList.get(11), placeList.get(12), "strokeColor=red;");
		}
		finally
		{
			mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
			layout.setOrientation(SwingConstants.WEST);
			layout.setIntraCellSpacing(20);
			layout.setParallelEdgeSpacing(100);
			layout.execute(parent);
			graph.getModel().endUpdate();
		}
	
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setBorder(BorderFactory.createEmptyBorder());
		graphComponent.setEnabled(false);
		this.setBorder(new EmptyBorder(20, 20, 10, 10));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.add(graphComponent);
	}

	public mxGraph getGraph() {
		return graph;
	}

	public void setGraph(mxGraph graph) {
		this.graph = graph;
	}
	
	
}
