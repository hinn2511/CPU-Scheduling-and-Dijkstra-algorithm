package ltm.client.gui.cpuScheduling;

import java.awt.*;
import java.util.StringTokenizer;

public class Graphics extends Canvas {
    public String a = "";

    public Graphics(String a) {
        this.a = a;

    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }


    public void paint(java.awt.Graphics g) {
        StringTokenizer st = new StringTokenizer(a, "-");
        int cd=st.countTokens();
        String arr_Process[]=new String[(cd/2)-1];
        String trong_So[]=new String[(cd/2)];

        int dem=0;
        for(int i=0;i<trong_So.length;i++)
        {
            String temp=st.nextToken();
            trong_So[dem]=temp;

            st.nextToken();
            dem++;
        }
        dem=0;
        st = new StringTokenizer(a, "-");
        for(int i=0;i<arr_Process.length;i++)
        {
            st.nextToken();
            String temp=st.nextToken();

            arr_Process[dem]=temp;

            dem++;
        }


        for(int i=0;i<(cd/2);i++) {
            g.drawLine(50+(i*50),60,50+(i*50),25);
            g.drawString(trong_So[i],40+(i*50),80);
        }
        for (int i=0;i<(cd/2)-1;i++){
            g.drawString(arr_Process[i],70+(i*50),50);
            g.drawLine(50+(i*50),60,100+(i*50),60);

        }

    }


}
