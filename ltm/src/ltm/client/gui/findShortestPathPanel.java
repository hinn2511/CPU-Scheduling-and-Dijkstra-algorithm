package ltm.client.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import ltm.client.bus.filePicker;
import ltm.client.bus.findShortestPath;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class findShortestPathPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtFilePath;
	private JTextField txtCost;
	private JTextField txtPath;
	private graphPanel gp = new graphPanel();
	private findShortestPath fsp = new findShortestPath();
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public findShortestPathPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Choose text file");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(26, 87, 133, 27);
		add(lblNewLabel);
		
		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		txtFilePath.setBounds(26, 124, 530, 39);
		add(txtFilePath);
		txtFilePath.setColumns(10);
		
		JButton btnPickFile = new JButton("Choose");
		btnPickFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPickFile.setBounds(589, 121, 233, 41);
		add(btnPickFile);
		
		
		final JButton btnFind = new JButton("Find shortest path");
		btnFind.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnFind.setBounds(589, 210, 233, 48);
		add(btnFind);
		btnFind.setEnabled(false);
		
		JButton btnExport = new JButton("Export graph to image");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnExport.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExport.setBounds(589, 279, 233, 48);
		add(btnExport);
//		btnExport.setEnabled(false);
		
		JLabel lblResult = new JLabel("Result");
		lblResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResult.setBounds(26, 173, 133, 27);
		add(lblResult);
		
		JLabel lblNewLabel_1 = new JLabel("Cost");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(589, 619, 45, 39);
		add(lblNewLabel_1);
		
		txtCost = new JTextField();
		txtCost.setEditable(false);
		txtCost.setColumns(10);
		txtCost.setBounds(589, 653, 233, 39);
		add(txtCost);
		
		JLabel lblNewLabel_1_1 = new JLabel("Path\r\n");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(589, 533, 73, 39);
		add(lblNewLabel_1_1);
		
		txtPath = new JTextField();
		txtPath.setEditable(false);
		txtPath.setColumns(10);
		txtPath.setBounds(589, 570, 233, 39);
		add(txtPath);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollPane.setBounds(26, 210, 530, 482);
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		gp.setBounds(0, 0 , 200, 200);
		panel.add(gp);
		
		JLabel lblNewLabel_2 = new JLabel("FIND SHORTEST PATH");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel_2.setBounds(26, 27, 257, 38);
		add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(589, 484, 233, 39);
		add(textField);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(589, 399, 233, 39);
		add(textField_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("End\r\n");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(589, 446, 73, 39);
		add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Start");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_2.setBounds(589, 364, 73, 39);
		add(lblNewLabel_1_1_2);
		
		
		btnPickFile.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	filePicker filePicker = new filePicker();
	        	try {
					filePicker.pickingFile();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
	        	txtFilePath.setText(filePicker.getPath());
	        	btnFind.setEnabled(true);
	        }

	    });
		
		btnExport.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
	        		fsp.exportGraph(gp.getGraph());
	        		alert("Export successfully at C:\\Users\\Administrator\\graph.jpg");
					
				} catch (Exception e2) {
					alert("Error: Can not export image.");
				}
	        }

	    });

	}
	
	void alert(String str){
        JOptionPane.showMessageDialog(null, str);
    }
}
