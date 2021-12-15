package ltm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;

import ltm.server.cpuScheduling.*;
import ltm.server.pathFinder.CalculatePath;

public class Worker implements Runnable {
	Security security;
	BufferedReader in = null;
	BufferedWriter out = null;
	private Socket socket;

	public Worker(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void send(String message) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String encryptedMessage = security.AESEncrypt(message);
		out.write(encryptedMessage);
		out.newLine();
		out.flush();
	}

	public String receive() throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String returnMessage = in.readLine();
		if (returnMessage == null)
			return null;
		String decryptedMessage = security.AESDecrypt(returnMessage);
		return decryptedMessage;
	}

	@Override
	public void run() {
		System.out.println("Connection " + socket.toString() + " accepted");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			security = new Security();
			security.generateRSAKey();

			out.write(security.getPublicKey());
			out.newLine();
			out.flush();

			while (true) {
				String encryptedSessionKey = in.readLine();
				if (encryptedSessionKey != null) {
					String sessionKey = security.RSADecrypt(encryptedSessionKey);
					security.setSecretKey(sessionKey);
					send("Security enabled");
					break;
				}
			}

			while (true) {
				String request = receive();
				if (request != null) {
					if (request.equals("exit"))
						break;
					if (request.equals("findPath")) {
						String data = receive();

						ValidatePathString validate = new ValidatePathString(data);
						if (!validate.getError().equals("noerror")) {
							send("error");
							send(validate.getError());
						} 
						else {
							CalculatePath calculate = new CalculatePath(validate.getPlaceList(),
									validate.getFrom(), validate.getTo(), validate.getDistance());
							if (calculate.getCost() != Double.MAX_VALUE) {
								send("success");
								send(calculate.getPlacelist());
								send(calculate.getStartPlace());
								send(calculate.getEndPlace());
								send(calculate.getShortestPath());
								send(String.valueOf((int) calculate.getCost()));
								send(calculate.getPathGraph());
								send(calculate.getShortestPathGraph());
							} else {
								send("failed");
								send("Can not find shortest path");
							}
						}

					}
					if (request.equals("cpuScheduling")) {
						System.out.println(" CPU Start ");
//						in.readLine();
						String Option = receive();

						CPUScheduler scheduler;

						String result_Client = receive();


						StringTokenizer st=new StringTokenizer(result_Client,",");
						int dem=0;
						int dongdung=0;
						int st_Count = st.countTokens();
						while (st.hasMoreTokens()) {
							String temp = st.nextToken();
							StringTokenizer st_temp = new StringTokenizer(temp, "-");
							st_temp.nextToken();
							dem = 0;
							while (st_temp.hasMoreTokens()) {
								String temp1 = st_temp.nextToken();
								if (CheckNum(temp1))
									dem++;
							}
							if (dem != 2)
								break;
							dongdung++;
						}

						if(dongdung==st_Count) {
							send("Success");
							switch (Option) {
								case "FCFS":
									scheduler = new FirstComeFirstServe();
									StringTokenizer st1=new StringTokenizer(result_Client,",");
									String temp="";

									while (st1.hasMoreTokens()){
										temp=st1.nextToken();
										System.out.println("hello "+temp);
										StringTokenizer token=new StringTokenizer(temp,"-");
										scheduler.add(new Row(	token.nextToken(),							// P1
												Integer.parseInt( token.nextToken()), 		// AT(P1)
												Integer.parseInt( token.nextToken())));		// BT(B1)
									}
									scheduler.process();
									send(display(scheduler));
									break;
								case "SJF":
									scheduler = new ShortestJobFirst();
									break;
								case "PSN":
									scheduler = new PriorityNonPreemptive();
									break;
								case "RR":
									String tq = JOptionPane.showInputDialog("Time Quantum");
									if (tq == null) {
										return;
									}
									scheduler = new RoundRobin();
									scheduler.process();
									display(scheduler);
									scheduler.setTimeQuantum(Integer.parseInt(tq));
									break;
								default:
									return;
							}
						}
						else {
							send("failed");
							send("Data Incorectly ");
						}



					}
				}

			}
			System.out.println("Closing connection with" + socket.toString());
			in.close();
			out.close();
			socket.close();
		} catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException
				| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Exception " + e);
		}
	}

	public static boolean CheckNum(String so)
	{
		Pattern pattern = Pattern.compile("\\d*");
		Matcher matcher = pattern.matcher(so);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static String display(CPUScheduler object)
	{
		String result="";
		System.out.println("Process\tAT\tBT\tWT\tTAT");

		//Xuất mảng
		for (Row row : object.getRows())
		{
			System.out.println(row.getProcessName() + "\t" + row.getArrivalTime() + "\t" + row.getBurstTime()
					+ "\t" + row.getWaitingTime() + "\t" + row.getTurnaroundTime());
		}

		System.out.println();

		for (int i = 0; i < object.getTimeline().size(); i++)
		{
			List<Event> timeline = object.getTimeline();
			// Xuất thời gian và tiến trình
			if(i==0)
				result=timeline.get(i).getStartTime() + "-" + timeline.get(i).getProcessName();
			else
				result=result+ "-"+timeline.get(i).getStartTime() + "-" + timeline.get(i).getProcessName();

			System.out.print(timeline.get(i).getStartTime() + "(" + timeline.get(i).getProcessName() + ")");

			// Xuất thời gian cuối cùng
			if (i == object.getTimeline().size() - 1)
			{
				result=result + "-"+timeline.get(i).getFinishTime();
				System.out.print(timeline.get(i).getFinishTime());
			}
		}
		result=result+"-"+object.getAverageWaitingTime()+"-"+object.getAverageTurnAroundTime();
		System.out.println("say hello "+ result);
		System.out.println("\n\nAverage WT: " + object.getAverageWaitingTime() + "\nAverage TAT: " + object.getAverageTurnAroundTime());

		return result;
	}

}
