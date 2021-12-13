package ltm.server.pathFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class CalculatePath2 {
	private String placelist;
	private String startPlace;
	private String endPlace;
	private boolean directed;
	private String distance;
	private String shortestPath;
	private String pathGraph;
	private String shortestPathGraph;
	private double cost;

	public String getPathGraph() {
		return pathGraph;
	}

	public void setPathGraph(String pathGraph) {
		this.pathGraph = pathGraph;
	}

	public String getPlacelist() {
		return placelist;
	}

	public void setPlacelist(String placelist) {
		this.placelist = placelist;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(String shortestPath) {
		this.shortestPath = shortestPath;
	}

	public String getShortestPathGraph() {
		return shortestPathGraph;
	}

	public void setShortestPathGraph(String shortestPathGraph) {
		this.shortestPathGraph = shortestPathGraph;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public CalculatePath2(String placelist, boolean directed, String startPlace, String endPlace, String distance) {
		super();
		this.placelist = placelist;
		this.directed = directed;
		this.startPlace = startPlace;
		this.endPlace = endPlace;
		this.distance = distance;
		Calculate();
	}

	public void Calculate() {
		StringTokenizer st = null;

		// Tao danh sach dia diem
		List<String> places = new ArrayList();

		// Them tung dia diem vao danh sach
		st = new StringTokenizer(getPlacelist(), ",");
		while (st.hasMoreTokens()) {
			places.add(st.nextToken());
		}

		// Them cac dinh tuong ung voi tung` dia diem
		List<Vert> vertList = new ArrayList<Vert>();
		for (int i = 0; i < places.size(); i++) {
			vertList.add(new Vert(places.get(i)));
		}

		// Tao mang 2 chieu chua khoanh cach giua 2 diem
		String temp = getDistance().replace("->", "-").replace(":", "-").replace(",", "-");
		st = new StringTokenizer(temp, "-");

		// Khoi tao mang co do dai = so luong dia diem
		String[][] graphArray = new String[places.size()][places.size()];

		// Cho tat ca bang 0
		for (int i = 0; i < graphArray.length; ++i) {
			for (int j = 0; j < graphArray.length; ++j) {
				graphArray[i][j] = "0";
			}
		}

		// Neu co duong di, thay the "0" bang khoang cach giua 2 dinh
		while (st.hasMoreTokens()) {
			graphArray[places.indexOf(st.nextToken())][places.indexOf(st.nextToken())] = st.nextToken();
		}
		
		// Them cac canh giua cac dinh neu co duong di ( do dai giua 2 dinh != "0")
		for (int i = 0; i < graphArray.length; ++i) {
			for (int j = 0; j < graphArray[i].length; ++j) {
				if (!graphArray[i][j].equals("0"))
					vertList.get(i).addNeighbour(
							new Edge(Integer.valueOf(graphArray[i][j]), vertList.get(i), vertList.get(j)));
			}
		}

		// Lop tim duong di ngan nhat
		PathFinder shortestPath = new PathFinder();
		
		// Tim duong di ngan nhat tu vi tri startPlace
		shortestPath.ShortestP(vertList.get(places.indexOf(getStartPlace())));

		// Tinh chi phi duong di ngan nhat tu startPlace den endPlace
		setCost(vertList.get(places.indexOf(getEndPlace())).getDist());

		// Danh sach ca dinh trong duong di ngan nhat
		List<Vert> shortestPathList = shortestPath.getShortestP(vertList.get(places.indexOf(getEndPlace())));

		// Tra ve chuoi chua duong di ngan nhat
		String shortestPathString = "";

		for (Vert vert : shortestPathList) {
			shortestPathString += vert.getName() + " -> ";
		}
		shortestPathString = shortestPathString.substring(0, shortestPathString.length() - 4);
		setShortestPath(shortestPathString);

		// Tra ve mang do dai duong di giua cac dinh
		setPathGraph("");
		// Tra ve mang duong di ngan nhat ( "x" neu ton tai trong duong di ngan nhat, nguoc lai la "0")
		setShortestPathGraph("");

		// Xu ly chuoi duong di ngan nhat
		for (int i = 0; i < graphArray.length; ++i) {
			for (int j = 0; j < graphArray.length; ++j) {
				setPathGraph(getPathGraph() + " " + graphArray[i][j]);
				if (shortestPathList.indexOf(vertList.get(i)) > -1 && shortestPathList.indexOf(vertList.get(j)) > -1) {
					if (i < j && shortestPathList.indexOf(vertList.get(j))
							- shortestPathList.indexOf(vertList.get(i)) == 1)
						setShortestPathGraph(getShortestPathGraph() + " x");
					else {
						if (i > j && shortestPathList.indexOf(vertList.get(j))
								- shortestPathList.indexOf(vertList.get(i)) == 1)
							setShortestPathGraph(getShortestPathGraph() + " x");
						else
							setShortestPathGraph(getShortestPathGraph() + " 0");
					}
				} 
				else
					setShortestPathGraph(getShortestPathGraph() + " 0");
			}
		}
	}
}
