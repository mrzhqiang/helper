package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.Ripple;
import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class FishEyeRipple implements Ripple {

    private final Config config;

    public FishEyeRipple(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public BufferedImage distort(BufferedImage source) {
        Preconditions.checkNotNull(source, "source == null");

        Graphics2D graph = (Graphics2D) source.getGraphics();
        int imageHeight = source.getHeight();
        int imageWidth = source.getWidth();

        // want lines put them in a variable so we might configure these later
        int horizontalLines = imageHeight / 7;
        int verticalLines = imageWidth / 7;

        // calculate space between lines
        int horizontalGaps = imageHeight / (horizontalLines + 1);
        int verticalGaps = imageWidth / (verticalLines + 1);

        // draw the horizontal stripes
        for (int i = horizontalGaps; i < imageHeight; i = i + horizontalGaps) {
            graph.setColor(Color.blue);
            graph.drawLine(0, i, imageWidth, i);
        }

        // draw the vertical stripes
        for (int i = verticalGaps; i < imageWidth; i = i + verticalGaps) {
            graph.setColor(Color.red);
            graph.drawLine(i, 0, i, imageHeight);
        }

        // create a pixel array of the original image.
        // we need this later to do the operations on..
        int[] pix = new int[imageHeight * imageWidth];
        int i = 0;

        for (int j = 0; j < imageWidth; j++) {
            for (int k = 0; k < imageHeight; k++) {
                pix[i] = source.getRGB(j, k);
                i++;
            }
        }

        double distance = RandomNumbers.rangeInt(imageWidth / 4, imageWidth / 3);

        // put the distortion in the (dead) middle
        int widthMiddle = source.getWidth() / 2;
        int heightMiddle = source.getHeight() / 2;

        // again iterate over all pixels..
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                int relX = x - widthMiddle;
                int relY = y - heightMiddle;

                double d1 = Math.sqrt(relX * relX + relY * relY);
                if (d1 < distance) {
                    int j2 = widthMiddle
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (x - widthMiddle));
                    int k2 = heightMiddle
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (y - heightMiddle));
                    source.setRGB(x, y, pix[j2 * imageHeight + k2]);
                }
            }
        }
        return source;
    }

    private double fishEyeFormula(double s) {
        if (s < 0.0D)
            return 0.0D;
        if (s > 1.0D)
            return s;
        else
            return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
}
