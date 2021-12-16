package ltm.client.gui.pathFinder;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import ltm.client.Connection;
import ltm.client.Client;
import ltm.client.Utility;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class FindShortestPathPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtFilePath;
	private JTextField txtCost;
	private GraphDrawer gDrawer;
	private Utility u = new Utility();
	private JTextField txtEnd;
	private JTextField txtStart;
	String filePath;

	/**
	 * Create the panel.
	 */
	public FindShortestPathPanel() {
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Choose text file");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(26, 56, 118, 27);
		add(lblNewLabel);

		txtFilePath = new JTextField();
		txtFilePath.setBackground(SystemColor.text);
		txtFilePath.setEditable(false);
		txtFilePath.setBounds(26, 93, 477, 39);
		add(txtFilePath);
		txtFilePath.setColumns(10);

		JButton btnPickFile = new JButton("Choose");
		btnPickFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPickFile.setBounds(370, 56, 133, 27);
		add(btnPickFile);

		final JButton btnFind = new JButton("Find shortest path");
		btnFind.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnFind.setBounds(26, 142, 477, 39);
		add(btnFind);
		btnFind.setEnabled(false);

		JButton btnExport = new JButton("Export graph to image");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnExport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExport.setBounds(26, 485, 477, 39);
		add(btnExport);
		btnExport.setEnabled(false);

		JLabel lblResult = new JLabel("Result");
		lblResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResult.setBounds(526, 10, 133, 27);
		add(lblResult);

		JLabel lblNewLabel_1 = new JLabel("Cost");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(26, 382, 45, 39);
		add(lblNewLabel_1);

		txtCost = new JTextField();
		txtCost.setBackground(SystemColor.text);
		txtCost.setEditable(false);
		txtCost.setColumns(10);
		txtCost.setBounds(26, 415, 477, 39);
		add(txtCost);

		JLabel lblNewLabel_1_1 = new JLabel("Path\r\n");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(26, 275, 118, 39);
		add(lblNewLabel_1_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollPane.setBounds(526, 43, 796, 481);
		add(scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);

		JLabel lblNewLabel_2 = new JLabel("FIND SHORTEST PATH");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel_2.setBounds(26, 0, 257, 38);
		add(lblNewLabel_2);

		txtEnd = new JTextField();
		txtEnd.setBackground(SystemColor.text);
		txtEnd.setEditable(false);
		txtEnd.setColumns(10);
		txtEnd.setBounds(273, 226, 230, 39);
		add(txtEnd);

		txtStart = new JTextField();
		txtStart.setBackground(SystemColor.text);
		txtStart.setEditable(false);
		txtStart.setColumns(10);
		txtStart.setBounds(26, 226, 224, 39);
		add(txtStart);

		JLabel lblNewLabel_1_1_1 = new JLabel("End\r\n");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(273, 191, 73, 39);
		add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_1_1_2 = new JLabel("Start");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_2.setBounds(26, 191, 73, 39);
		add(lblNewLabel_1_1_2);
		
		JTextArea txtPath = new JTextArea();
		txtPath.setBackground(SystemColor.text);
		txtPath.setEditable(false);
		txtPath.setBounds(26, 324, 477, 48);
		txtPath.setBorder(BorderFactory.createCompoundBorder(
				txtPath.getBorder(), 
		        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		add(txtPath);

		btnPickFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//FileHandler filePicker = new FileHandler();
				try {
					u.pickingFile();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				if (u.getPath() != null) {
					filePath = u.getPath();
					txtFilePath.setText(u.getPath());
					btnFind.setEnabled(true);
				}
			}

		});

		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection client = Client.getConnection();
				String path = u.readShortesPathFile(filePath);
				try {
					client.send("findPath");
					client.send(path);
					while (true) {
							String result = client.receive();
							if (result != null) {
								
								if(gDrawer != null) {
									panel.remove(gDrawer);
								}
								
								if(result.equals("failed") || result.equals("error")) {
									alert(client.receive());
									break;
								}
								else {								
									String placeList = client.receive();
									String startPlace = client.receive();
									String endPlace = client.receive();
									String shortestPath = client.receive();
									String cost = client.receive();
									String pathGraph = client.receive();
									String shortestPathGraph = client.receive();
									
									gDrawer = new GraphDrawer(placeList, startPlace, endPlace, pathGraph, shortestPathGraph);
									gDrawer.setBounds(0, 0 , 500, 500);
									panel.revalidate();
									panel.repaint();
									panel.add(gDrawer);
									
									txtCost.setText(cost);
									txtStart.setText(startPlace);
									txtEnd.setText(endPlace);
									txtPath.setText(shortestPath);
									
									btnExport.setEnabled(true);
									
									break;
								}
								
							}
						
					}
				} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e1) {
					alert("Can not send data to server.");
				}
				
			}
		});
		
	

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gDrawer.getGraph() != null) {
					try {
						u.exportGraph(gDrawer.getGraph());
						String userHomeFolder = System.getProperty("user.home");
						alert("Export successfully at " + userHomeFolder + "\\Downloads" + "\\graph.jpg");

					} catch (Exception e2) {
						alert("Error: Can not export image.");
					}
				} 
				else
					alert("Error: Please add graph to export.");
			}

		});

	}

	void alert(String str) {
		JOptionPane.showMessageDialog(null, str);
	}
}
	
