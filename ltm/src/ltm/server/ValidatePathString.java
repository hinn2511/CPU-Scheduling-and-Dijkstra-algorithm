package ltm.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class ValidatePathString {
	private String input;
	private String placeList;
	private boolean directed;
	private String from;
	private String to;
	private String distance;
	private String error;

	private static String fileFormatError = "Incorrect file format!";
	private static String missingError = "is missing!";
	private static String notExistError = "is not exist in places list!";
	private static String duplicateError = "is duplicate!";
	private static String numberError = "is not number!";
	private static String valueError = "is not correct value(value must greater than 0)!";


	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPlaceList() {
		return placeList;
	}

	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public ValidatePathString(String input) {
		setInput(input);
		validate();
	}
	
	public void validate() {
		setError("noerror");

		StringTokenizer st = new StringTokenizer(getInput(), "|");

		// Dem so luong token
		if (st.countTokens() != 4) {
			setError(fileFormatError);
			return;
		}

		// Kiem tra dinh dang danh sach dia diem
		String placeList = st.nextToken();
		if (!placeList.contains("Places:")) {
			setError(fileFormatError);
			return;
		} else
			setPlaceList(placeList.replace("Places:", ""));

		// Kiem tra dinh dang diem bat dau
		String from = st.nextToken();
		if (!from.contains("From:")) {
			setError(fileFormatError);
			return;
		} else
			setFrom(from.replace("From:", ""));

		// Kiem tra diem bat dau co ton tai hay khong
		if (getFrom().equals("")) {
			setError("Starting point " + missingError);
			return;
		}

		// Kiem tra dinh dang diem ket thuc
		String to = st.nextToken();
		if (!to.contains("To:")) {
			setError(fileFormatError);
			return;
		} else
			setTo(to.replace("To:", ""));

		// Kiem tra diem den co ton tai hay khong
		if (getTo().equals("")) {
			setError("End point " + missingError);
			return;
		}
		
		// Kiem tra diem den co trung voi diem dich khong
		if(getFrom().equals(getTo())) {
			setError("Start point must different from end point");
			return;
		}

		StringTokenizer pl = new StringTokenizer(getPlaceList(), ",");
		List<String> places = new ArrayList();
		LinkedHashMap<String, Integer> frequencyMap = new LinkedHashMap<>();
		while (pl.hasMoreTokens()) {
			String place = pl.nextToken();
			// Kiem tra danh sach dia diem co bi trung khong
			if(places.indexOf(place) > -1) {
				setError("Some place in places list is " + duplicateError);
				return;
			}
			else {
				places.add(place);
				frequencyMap.putIfAbsent(place, 0);
			}
		}

		// Kiem tra diem bat dau co ton tai trong ds dia diem
		if (places.indexOf(getFrom()) < 0) {
			setError("Starting point " + notExistError);
			return;
		}

		// Kiem tra diem bat dau co ton tai trong ds dia diem
		if (places.indexOf(getTo()) < 0) {
			setError("End point " + notExistError);
			return;
		}
		
		setDistance(st.nextToken());
		String temp = getDistance().replace("->", "|").replace(":", "|").replace(",", "|");
		setDistance(temp);
		List<String> existedDistance = new ArrayList();
		st = new StringTokenizer(temp, "|");
		
		// Kiem tra khoang cach co thieu thong so khong
		int check = st.countTokens()%3;
		if(check > 0) {
			setError(fileFormatError);
			return;
		}
		
		while (st.hasMoreTokens()) {
			
			String firstPlace = st.nextToken();
			String secondPlace = st.nextToken();
			String dist = st.nextToken();
			
			// Kiem tra diem dau co nam trong ds dia diem
			if(places.indexOf(firstPlace) < 0) {
				setError(firstPlace  + " " + notExistError);
				return;
			}
			
			// Kiem tra diem dich co nam trong ds dia diem
			if(places.indexOf(secondPlace) < 0) {
				setError(secondPlace  + " " + notExistError);
				return;
			}
			
			// Kiem tra diem dau -> diem dich co lap lai khong
			if(existedDistance.indexOf(firstPlace + secondPlace) < 0)
				existedDistance.add(firstPlace + secondPlace);
			else {
				setError(firstPlace  + "->" + secondPlace + " " + duplicateError);
				return;
			}
			
			// Kiem tra diem dau trung diem cuoi
			if(firstPlace.equals(secondPlace)) {
				setError(firstPlace  + " " + duplicateError);
				return;
			}
			
			// Kiem tra gia tri cua khoang cach la chu
			int d = -1;
			try {
				d = Integer.valueOf(dist);
			} catch (NumberFormatException e) {
				setError(dist + " " + numberError);
				return;
			}
			
			// Kiem tra gia tri cua khoang cach la nho hon 1
			if(d < 1) {
				setError(d + " " + valueError);
				return;
			}
			
			frequencyMap.put(firstPlace, frequencyMap.get(firstPlace) + 1);
			frequencyMap.put(secondPlace, frequencyMap.get(secondPlace) + 1);
			
		}
		
		// Kiem tra diem ngoai do thi
		for(Entry<String, Integer> e: frequencyMap.entrySet()){
			if (e.getValue() == 0) {
				setError(e.getKey() + " is not connected to another place.");
				return;
			}
		}

	}

}
