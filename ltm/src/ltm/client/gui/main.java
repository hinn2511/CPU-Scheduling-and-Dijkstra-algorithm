package ltm.client.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private findShortestPathPanel findShortestPathPane;
	private cpuShedulingPanel cpuShedulingPanel;

	/**
	 * Launch the main.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public main() {
		super("Find shortest path and CPU scheduling");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1190, 767);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(311, 0, 865, 720);
		contentPane.add(layeredPane);
		
		
		findShortestPathPane = new findShortestPathPanel();
		cpuShedulingPanel = new cpuShedulingPanel();
		
		findShortestPathPane.setBounds(0, 0, 865, 720);
		cpuShedulingPanel.setBounds(0, 0, 865, 720);
		
		findShortestPathPane.setOpaque(false);
		cpuShedulingPanel.setOpaque(false);
	    
		layeredPane.add(findShortestPathPane);
		layeredPane.add(cpuShedulingPanel);
		
		findShortestPathPane.setVisible(true);
		cpuShedulingPanel.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(176, 196, 222));
		panel.setBounds(0, 0, 312, 767);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnFsp = new JButton("FIND SHORTEST PATH\r\n");
		btnFsp.setIcon(new ImageIcon("src/ltm/assets/magnifier.png"));
		btnFsp.setBounds(10, 16, 292, 58);
		panel.add(btnFsp);
		btnFsp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnFsp.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	findShortestPathPane.setVisible(true);
	        	cpuShedulingPanel.setVisible(false);
	        }

	    });
		
		
		JButton btnCs = new JButton("CPU SCHEDULING");
		btnCs.setIcon(new ImageIcon("src/ltm/assets/cpu.png"));
		btnCs.setBounds(10, 84, 292, 58);
		btnCs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(btnCs);
		
		btnCs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				findShortestPathPane.setVisible(false);
				cpuShedulingPanel.setVisible(true);
			}
			
		});
	}
}
