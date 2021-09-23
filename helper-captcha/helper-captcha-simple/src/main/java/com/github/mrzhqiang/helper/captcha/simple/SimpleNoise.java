package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.Noise;
import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

public final class SimpleNoise implements Noise {

    @Override
    public void make(BufferedImage image, float factor1, float factor2, float factor3, float factor4) {
        Preconditions.checkNotNull(image, "image == null");

        int width = image.getWidth();
        int height = image.getHeight();

        Random random = new SecureRandom();
        // the curve from where the points are taken
        CubicCurve2D cc = new CubicCurve2D.Float(
                width * factor1, height * random.nextFloat(),
                width * factor2, height * random.nextFloat(),
                width * factor3, height * random.nextFloat(),
                width * factor4, height * random.nextFloat());

        // creates an iterator to define the boundary of the flattened curve
        PathIterator pi = cc.getPathIterator(null, 2);
        Point2D[] tmp = new Point2D[200];
        int i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            float[] coords = new float[6];
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        // the points where the line changes the stroke and direction
        Point2D[] pts = new Point2D[i];
        System.arraycopy(tmp, 0, pts, 0, i);

        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        Color color = Colors.of(SimpleConfig.Noise.COLOR, Color.BLACK);
        graph.setColor(color);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(0.9f * (4 - i)));
            }
            graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
                    (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
        }

        graph.dispose();
    }
}
