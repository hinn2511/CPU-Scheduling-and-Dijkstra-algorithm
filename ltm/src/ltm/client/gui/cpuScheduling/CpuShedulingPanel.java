package ltm.client.gui.cpuScheduling;

import ltm.client.Client;
import ltm.client.Connection;
import ltm.client.FileHandler_CPU;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CpuShedulingPanel extends JPanel {
    private JTable table;
    private JTextField txt_Path;
    String filePath;
    private Graphics cus;
    private JScrollPane cusJScroll;

    /**
     * Create the panel.
     */
    public CpuShedulingPanel() {


        DefaultTableModel model = new DefaultTableModel(new String[]{"Process", "AT", "BT", "Priority", "WT", "TAT"}, 0);
        setLayout(null);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setEnabled(false);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(305, 54, 510, 251);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setEnabled(false);
        add(scrollPane);


//		cusJScroll.setVisible();

//		cus.setVisible(false);


        JButton btn_Load = new JButton("Choose");
        btn_Load.setBounds(730, 337, 85, 33);

        btn_Load.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(btn_Load);

        JLabel lbl_AWT = new JLabel("Waiting time");
        lbl_AWT.setBounds(268, 554, 153, 32);
        lbl_AWT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_AWT);

        JLabel lbl_AT = new JLabel("Turn Around Time");
        lbl_AT.setBounds(268, 614, 153, 32);
        lbl_AT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_AT);

        JLabel lbl_Result_Awt = new JLabel("");
        lbl_Result_Awt.setBounds(452, 559, 112, 22);
        lbl_Result_Awt.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_Result_Awt);

        JLabel lbl_Result_At = new JLabel("");
        lbl_Result_At.setBounds(452, 619, 112, 22);
        lbl_Result_At.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_Result_At);

        JComboBox cbb_Option = new JComboBox(new String[]{"FCFS", "SJF", "PSN", "RR"});
        cbb_Option.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbb_Option.setBounds(716, 503, 99, 33);
        add(cbb_Option);

        JButton btn_Compute = new JButton("Compute");
        btn_Compute.setBounds(716, 614, 99, 27);
        btn_Compute.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(btn_Compute);


        txt_Path = new JTextField();
        txt_Path.setBounds(305, 337, 339, 33);
        txt_Path.setDisabledTextColor(new Color(0, 128, 128));
        txt_Path.setForeground(Color.DARK_GRAY);
        txt_Path.setEnabled(false);
        txt_Path.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txt_Path.setText("Đường dẫn");
        add(txt_Path);
        txt_Path.setColumns(10);

        btn_Compute.setEnabled(false);

        cus = new Graphics("s");
        cus.setForeground(Color.GREEN);
        cusJScroll = new JScrollPane(cus);

        cusJScroll.setBounds(305, 380, 510, 100);
        cusJScroll.setForeground(Color.WHITE);
        cusJScroll.setBackground(Color.WHITE);
        cus.setVisible(false);
        cusJScroll.setVisible(false);
        add(cusJScroll);


        btn_Load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                FileHandler_CPU filePicker = new FileHandler_CPU();
                try {
                    filePicker.pickingFile();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (filePicker.getPath() != null) {

                    filePath = filePicker.getPath();
                    txt_Path.setText(filePicker.getPath());
                    btn_Compute.setEnabled(true);


                }
            }
        });
        btn_Compute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection client = Client.getConnection();
                FileHandler_CPU fileHandler = new FileHandler_CPU();
                String result_read_file = fileHandler.readShortesPathFile(filePath);        // Xuất chuỗi sau khi đọc file
                try {

                        client.send("cpuScheduling");

//					System.out.println(" Model "+model.getRowCount());

                    // Sending Option
                    client.send((String) cbb_Option.getSelectedItem());
//                    System.out.println("result 130: " + result_read_file + "," + cbb_Option.getSelectedItem());

                    // Sending result_read_file
                    client.send(result_read_file);

                    while (true) {
                        String kq= client.receive();          // SUCCESS OR FAILED
                        System.out.println("KQ "+kq);
                        if (kq != null) {
                            if(kq.equals("failed")) {
                                alert(client.receive());
                                break;
                            }
                           else
                            {
                                String result_receive_Server = client.receive();


                                cus =new Graphics(result_receive_Server);
                                cus.setForeground(Color.GREEN);
                                cusJScroll= new JScrollPane(cus);

                                cusJScroll.setBounds(305, 380, 510, 100);
                                cusJScroll.setForeground(Color.WHITE);
                                cusJScroll.setBackground(Color.WHITE);
                                cus.setFont(new Font("Tahoma", Font.PLAIN, 14));
                                revalidate();
                                repaint();
                                cusJScroll.setVisible(true);
                                add(cusJScroll);

                                break;
                            }

                        }

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }
            }
        });

    }
    void alert(String str) {
        JOptionPane.showMessageDialog(null, str);
    }
}
