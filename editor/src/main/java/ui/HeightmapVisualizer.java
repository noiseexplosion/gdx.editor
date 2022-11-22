package ui;

import gdx.editor.EditorRootScreen;
import squidpony.squidgrid.mapping.HeightMapFactory;
import squidpony.squidgrid.mapping.WorldMapGenerator;
import utils.ModelUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightmapVisualizer {
    //input a 2d array of doubles and construct a heightmap from it

    int width = 128;
    int height = 128;
    double[][] heightmap;

    public HeightmapVisualizer(int width, int height, double[][] heightmap){
        this.width = width;
        this.height = height;
        this.heightmap = heightmap;
    }

    public void visualize() {

    }

    public void createImage(int width, int height, double[][] heightmap){
        //create a new image
        //for each pixel in the image, set the color to the heightmap value
        //save the image

        //create a new image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //for each pixel in the image, set the color to the heightmap value
        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[i].length; j++) {
                double value = heightmap[i][j];

                int color = (int) (value * 255);
                Color c = new Color(color, color, color);

                image.setRGB(i, j, c.getRGB());
            }
        }

        //save the image
        File outputfile = new File("image.png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double[][] generateHeightmap() {

        //generate a heightmap
        double[][] heightmap = new double[width][height];

        //return the heightmap
        return heightmap;
    }

    public static void main(String[] args) {
        com.badlogic.gdx.graphics.Color color = ModelUtils.getRandomColor();
        System.out.println(color);
    }

}
