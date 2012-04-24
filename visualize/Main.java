import java.io.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;

class Main {
    Scanner sc;
    BufferedImage img;

    public final static int MAX_SIZE = 1000;

    int min_x;
    int max_x;
    int min_y;
    int max_y;

    int size_x;
    int size_y;

    double scale_x;
    double scale_y;

    ArrayList<Integer> points_x = new ArrayList<Integer>();
    ArrayList<Integer> points_y = new ArrayList<Integer>();
    ArrayList<Integer> points_c = new ArrayList<Integer>(); // cluster

    public Main()
    {
        sc = new Scanner(System.in);
    }

    void run()
    {
        min_x = Integer.MAX_VALUE;
        max_x = 0;
        min_y = Integer.MAX_VALUE;
        max_y = 0;

        for (int i = 0; sc.hasNextInt(); i++) {
            points_x.add(sc.nextInt());
            points_y.add(sc.nextInt());
            points_c.add(sc.nextInt());

            if (points_x.get(i) < min_x) {
                min_x = points_x.get(i);
            }
            if (points_x.get(i) > max_x) {
                max_x = points_x.get(i);
            }
            if (points_y.get(i) < min_y) {
                min_y = points_y.get(i);
            }
            if (points_y.get(i) > max_y) {
                max_y = points_y.get(i);
            }
        }

        // calculate image dimensions, we add two to the diff, so the edge
        // values will also be displayed correctly
        int diff_x = max_x - min_x + 2;
        int diff_y = max_y - min_y + 2;

        if (diff_x > diff_y) {
            size_x = MAX_SIZE;
            size_y = ((MAX_SIZE * diff_y) / diff_x);

            scale_x = size_x / (diff_x * 1.0);
            scale_y = size_y / (diff_y * 1.0);
        } else {
            size_y = MAX_SIZE;
            size_x = (MAX_SIZE * diff_x) / diff_y + ((int) (0.05 * MAX_SIZE));

            scale_y = size_y / (diff_y * 1.0);
            scale_x = size_x / (diff_x * 1.0);
        }

        System.out.println("X scale: " + scale_x + " diff: " + diff_x);
        System.out.println("Y scale: " + scale_y + " diff: " + diff_y);

        draw();
    }

    void draw()
    {
        // now start creating the image
        img = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();

        g.setColor(Color.WHITE);

        g.fillRect(0, 0, size_x, size_y);

        for (int i = 0; i < points_x.size(); i++) {
            g.setColor(getColor(points_c.get(i)));
            g.fillOval((int) ((points_x.get(i) - min_x) * scale_x), (int) ((points_y.get(i) - min_y) * scale_y), 2 + ((int) scale_x), 2 + ((int) scale_y));
        }

        try {
            File outputfile = new File("output.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }
    
    Color getColor(int cluster)
    {
        if (cluster == 0) {
            return Color.BLACK;
        }

        switch (cluster % 5) {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.PINK;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.CYAN;
            default:
                return Color.GRAY;
        }
    }

    public static void main(String args[])
    {
        new Main().run();
    }
}
