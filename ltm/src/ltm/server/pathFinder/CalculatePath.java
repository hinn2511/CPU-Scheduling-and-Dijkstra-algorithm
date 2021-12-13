package ltm.server.pathFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class CalculatePath {
	private double cost;
	private String path;
	private int totalPos;
	private int startPos;
	private int endPos;
	private String graph;
	private String resutlGraph;
	
	public String getResutlGraph() {
		return resutlGraph;
	}

	public void setResutlGraph(String resutlGraph) {
		this.resutlGraph = resutlGraph;
	}


	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getTotalPos() {
		return totalPos;
	}

	public int getStartPos() {
		return startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public CalculatePath(int totalPos, int startPos, int endPos, String graph) {
		super();
		this.totalPos = totalPos;
		this.startPos = startPos;
		this.endPos = endPos;
		this.graph = graph;
		Calculate();
	}

	public void Calculate() {
		List<Vert> vertList = new ArrayList<Vert>();
		for (int i = 0; i < totalPos; i++) {
			vertList.add(new Vert(String.valueOf(i)));
		}

		StringTokenizer st = new StringTokenizer(graph, " ");
		String[][] graphArray = new String[totalPos][totalPos];
		String[][] resutlGraphArray = new String[totalPos][totalPos];

		for (int i = 0; i < graphArray.length; ++i) {
			for (int j = 0; j < graphArray.length; ++j) {
				graphArray[i][j] = st.nextToken();
			}
		}
		
		for (int i = 0; i < resutlGraphArray.length; ++i) {
			for (int j = 0; j < resutlGraphArray.length; ++j) {
				resutlGraphArray[i][j] = "0";
			}
		}
		
		setResutlGraph("");
		
		for (int i = 0; i < resutlGraphArray.length; ++i) {
			for (int j = 0; j < resutlGraphArray.length; ++j) {
				//if(resutlGraphArray[j][i].equals("0") && !graphArray[i][j].equals("i")) {
					resutlGraphArray[i][j] = graphArray[i][j];
				//}
				setResutlGraph(getResutlGraph() + resutlGraphArray[i][j] + " ");
			}
		}
		
		
		

		for (int i = 0; i < graphArray.length; ++i) {
			for (int j = 0; j < graphArray[i].length; ++j) {
				if (!graphArray[i][j].equals("0") && !graphArray[i][j].equals("i"))
					vertList.get(Integer.valueOf(i)).addNeighbour(new Edge(Integer.valueOf(graphArray[i][j]),
							vertList.get(Integer.valueOf(i)), vertList.get(Integer.valueOf(j))));
			}
		}

		PathFinder shortestPath = new PathFinder();
		shortestPath.ShortestP(vertList.get(startPos));

		setCost(vertList.get(endPos).getDist());

		List<Vert> shortestPathList = shortestPath.getShortestP(vertList.get(endPos));
		String shortestPathString = "";
		for (Vert vert : shortestPathList) {
			int vertName = Integer.valueOf(vert.getName()) + 1;
			shortestPathString += vertName + " ";
		}
		shortestPathString = shortestPathString.substring(0, shortestPathString.length() - 1);
		setPath(shortestPathString);
	}

}
