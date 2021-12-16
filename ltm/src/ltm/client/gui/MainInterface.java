package ltm.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ltm.client.Client;
import ltm.client.Connection;
import ltm.client.gui.cpuScheduling.CpuShedulingPanel;
import ltm.client.gui.pathFinder.FindShortestPathPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.Color;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class MainInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private FindShortestPathPanel findShortestPathPane;
	private CpuShedulingPanel cpuShedulingPanel;

	/**
	 * Launch the MainInterface.
	 */

	/**
	 * Create the frame.
	 */
	public MainInterface() {
		super("Find shortest path and CPU scheduling");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1371, 663);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(10, 71, 1337, 550);
		contentPane.add(layeredPane);
		
		
		findShortestPathPane = new FindShortestPathPanel();
		cpuShedulingPanel = new CpuShedulingPanel();
		
		findShortestPathPane.setBounds(0, 0, 1337, 551);
		cpuShedulingPanel.setBounds(0, 0, 1337, 551);
		
		findShortestPathPane.setOpaque(false);
		cpuShedulingPanel.setOpaque(false);
	    
		layeredPane.add(findShortestPathPane);
		layeredPane.add(cpuShedulingPanel);
		
		findShortestPathPane.setVisible(true);
		cpuShedulingPanel.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(0, 0, 1347, 69);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnFsp = new JButton("FIND SHORTEST PATH\r\n");
		btnFsp.setIcon(new ImageIcon("src/ltm/assets/magnifier.png"));
		btnFsp.setBounds(32, 10, 292, 39);
		panel.add(btnFsp);
		btnFsp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		JButton btnCs = new JButton("CPU SCHEDULING");
		btnCs.setBounds(341, 10, 292, 39);
		panel.add(btnCs);
		btnCs.setIcon(new ImageIcon("src/ltm/assets/cpu.png"));
		btnCs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setBounds(1034, 10, 292, 39);
		panel.add(btnExit);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Connection client = Client.getConnection();
				try {
					client.send("exit");
					System.exit(0);
					dispose();
					client.closeSocket();
					setVisible(false);
				} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
		
		btnCs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				findShortestPathPane.setVisible(false);
				cpuShedulingPanel.setVisible(true);
			}
			
		});
		
		btnFsp.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	findShortestPathPane.setVisible(true);
	        	cpuShedulingPanel.setVisible(false);
	        }

	    });
	}
}
