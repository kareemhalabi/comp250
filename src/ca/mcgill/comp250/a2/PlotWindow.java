package ca.mcgill.comp250.a2;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.*;  

public class PlotWindow extends JFrame{
    private static final long serialVersionUID = 1473505112164488758L;
    private static int I=0;
    Plot p;
    

    public PlotWindow(String title){
        super( title );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        p = new Plot();
        p.init();
        add(p);
        p.setSize(800, 480);
        setSize(800,480);
        setLocation(100+10*I,100+10*I);
        I++;
        setVisible(true);
    }

    public void addPlot(String name, int[] data){
        p.addPlot(name, data);
    }

    public class Plot extends JPanel {
        private static final long serialVersionUID = 8855911865861199436L;
        private ArrayList<int[]> plot_data;
        private ArrayList<String> plot_names;
        private ArrayList<Color> plot_colors;

        private Font textFont;
        final int PAD = 80;
        private int MAX_Y = 50;
        private int MAX_X = 10;
        private int COLOR_RATIO = (int)(255*(Math.sqrt(5.0)-1.0)/2.0);

        public void init(){
            plot_names = new ArrayList<String>();
            plot_data = new ArrayList<int[]>();
            plot_colors = new ArrayList<Color>();
            textFont = new Font("Verdana", Font.BOLD, 14);
        }

        public void addPlot(String name, int[] data){
            plot_names.add(name);
            plot_data.add(data);
            int new_max = getMax(plot_data.size()-1);
            if (new_max > MAX_Y){
                MAX_Y = new_max;
            }
            if (data.length > MAX_X){
                MAX_X = data.length;
            }
            int i = plot_data.size();
            Color c = new Color((i*COLOR_RATIO)%255, ((i+2)*COLOR_RATIO)%255, ((i+3)*COLOR_RATIO)%255, 255);
            plot_colors.add(c);
            for (int j = 0; j < plot_colors.size(); j++) {
                Color color = plot_colors.get(j);
                Color new_color = new Color(color.getRed(), color.getBlue(), color.getGreen(), 127/plot_colors.size() + 128);
                plot_colors.set(j, new_color);
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);			
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int pointSize = Math.max(Math.min(w, h)/80,4);

            double xInc = (double)(w - 2*PAD)/(MAX_X-1);
            double scale = (double)(h - 2*PAD)/MAX_Y;
            // Draw abcissa.
            int tickInc = MAX_X/10;
            for (int i = 0; i <= MAX_X; i+=tickInc) {
                int x = PAD + (int)(i*xInc);
                int y = h-PAD;
                g.drawString(Integer.toString(i), x-5, y+20);
                g2.draw(new Line2D.Double(x, y-5, x, y+5));
            }
            g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD/2, h-PAD));
            AffineTransform orig = g2.getTransform();
            g2.rotate(-Math.PI/2);
            g2.setColor(Color.black);
            g2.drawString("Number of comparisons",-((h+PAD)/2), PAD/3);
            g2.setTransform(orig);

            // Draw ordinate.
            tickInc = (h-PAD)/10;	

            for (int i = tickInc; i < h - PAD; i+=tickInc) {
                int x = PAD;
                int closest_10 = ((int)(i/scale)/10)*10;

                int y = h - PAD - (int)(closest_10*scale);
                if (y < PAD)
                    break;
                String tickMark = Integer.toString(closest_10);
                int stringLen = (int) g2.getFontMetrics().getStringBounds(tickMark, g2).getWidth();
                g.drawString(tickMark, x-stringLen-8, y+5);
                g2.draw(new Line2D.Double(x-5, y, x+5, y));
            }
            g2.draw(new Line2D.Double(PAD, PAD/2, PAD, h-PAD));
            g.drawString("Array Size", (w-PAD)/2, h-PAD+40);

            for (int index = 0; index < plot_data.size(); index++) {
                int[] data = plot_data.get(index);

                // Mark data points.
                g2.setPaint(plot_colors.get(index));

                for(int i = 0; i < data.length; i++) {
                    double x = PAD + i*xInc;
                    double y = h - PAD - scale*data[i];
                    g2.fill(new Ellipse2D.Double(x-pointSize/2, y-pointSize/2, pointSize, pointSize));
                }

                g2.setFont(textFont);
                int stringHeight = (int) g2.getFontMetrics().getStringBounds(plot_names.get(index), g2).getHeight();
                g.drawString(plot_names.get(index), PAD+20, PAD+(index+1)*stringHeight);
            }
        }

        private int getMax(int index) {
            int[] data = plot_data.get(index);
            int max = -Integer.MAX_VALUE;
            for(int i = 0; i < data.length; i++) {
                if(data[i] > max)
                    max = data[i];
            }
            return max;
        }

    }
}