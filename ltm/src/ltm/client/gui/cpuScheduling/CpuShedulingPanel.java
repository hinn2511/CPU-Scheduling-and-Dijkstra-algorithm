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
import java.util.StringTokenizer;

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


        final DefaultTableModel[] model = {new DefaultTableModel(new String[]{"Process", "AT", "BT", "WT", "TAT"},
                0)};

//model[0].addRow(new Object[]{"Process", "AT", "BT", "WT", "TAT"});

        setLayout(null);
        table = new JTable(model[0]);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setEnabled(false);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 510, 400);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setEnabled(false);
        add(scrollPane);


//		cusJScroll.setVisible();

//		cus.setVisible(false);


        JButton btn_Load = new JButton("Choose");
        btn_Load.setBounds(430, 10, 100, 33);
        btn_Load.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(btn_Load);

        JLabel lbl_gantt = new JLabel("Gantt chart");
        lbl_gantt.setBounds(600, 10, 100, 40);
        lbl_gantt.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_gantt);
        
        JLabel lbl_AWT = new JLabel("Waiting time");
        lbl_AWT.setBounds(600, 400, 153, 32);
        lbl_AWT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_AWT);

        JLabel lbl_AT = new JLabel("Turn Around Time");
        lbl_AT.setBounds(600, 450, 153, 32);
        lbl_AT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_AT);

        JLabel lbl_Result_AT = new JLabel("");
        lbl_Result_AT.setBounds(750, 405, 112, 22);
        lbl_Result_AT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_Result_AT);

        JLabel lbl_Result_AWT = new JLabel("");
        lbl_Result_AWT.setBounds(750, 455, 112, 22);
        lbl_Result_AWT.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lbl_Result_AWT);

        JComboBox cbb_Option = new JComboBox(new String[]{"FCFS", "SJF", "SRT", "RR"});
        cbb_Option.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cbb_Option.setBounds(20, 470, 200, 35);
        add(cbb_Option);

        JButton btn_Compute = new JButton("Compute");
        btn_Compute.setBounds(230, 470, 300, 35);
        btn_Compute.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(btn_Compute);


        txt_Path = new JTextField();
        txt_Path.setBounds(20, 10, 400, 33);
        txt_Path.setDisabledTextColor(new Color(0, 128, 128));
        txt_Path.setForeground(Color.DARK_GRAY);
        txt_Path.setEnabled(false);
        txt_Path.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txt_Path.setText("Đường dẫn");
        add(txt_Path);
        txt_Path.setColumns(10);

        btn_Compute.setEnabled(false);

        cus = new Graphics("s");
        cus.setForeground(Color.BLACK);
        
        cusJScroll = new JScrollPane(cus);

        cusJScroll.setBounds(600, 70, 700, 300);
        cusJScroll.setForeground(Color.WHITE);
        cusJScroll.setBackground(Color.WHITE);
        cus.setVisible(true);
        cusJScroll.setVisible(true);
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
                        String kq = client.receive();          // SUCCESS OR FAILED
                        System.out.println("KQ " + kq);
                        if (kq != null) {
                            if (kq.equals("failed")) {
                                alert(client.receive());
                                break;
                            } else {
                                String result_receive_Server = client.receive();
                                
                                DefaultTableModel newModel = (DefaultTableModel) table.getModel();
                                newModel.setRowCount(0);
                                
                                StringTokenizer token = new StringTokenizer(result_receive_Server, ",");
                                String cuc1 = token.nextToken();
                                String cuc2 = token.nextToken();

                                StringTokenizer Token_Cuc1 = new StringTokenizer(cuc1, "-");
                                int cd=Token_Cuc1.countTokens();

//                                String arr[] = new String[cd];
//                                for (int i = 0; i < cd; i++) {
//                                    arr[i] = Token_Cuc1.nextToken();
//
//                                }
//                                System.out.println("cd arr "+arr.length);

                                while (Token_Cuc1.hasMoreTokens()) {
                                    model[0].addRow(new Object[]{Token_Cuc1.nextToken(), Token_Cuc1.nextToken(),
                                            Token_Cuc1.nextToken(), Token_Cuc1.nextToken(), Token_Cuc1.nextToken()});
                                }

                                StringTokenizer Token_cuc2 = new StringTokenizer(cuc2, "-");
                                int dem = 0;

                                cd = Token_cuc2.countTokens();
                                // Xử lí setText AWT VÀ AT
                                while (Token_cuc2.hasMoreTokens()) {
                                    String a = Token_cuc2.nextToken();
                                    dem++;
                                    if (dem == (cd - 1))
                                        lbl_Result_AT.setText(a);
                                    if (dem == (cd))
                                        lbl_Result_AWT.setText(a);
                                }
                                remove(cus);
                                remove(cusJScroll);
                                cus = new Graphics(cuc2);
                                cus.setForeground(Color.BLACK);
                                cusJScroll = new JScrollPane(cus);

                                cusJScroll.setBounds(600, 70, 700, 300);
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
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
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
