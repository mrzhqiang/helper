package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.Classes;
import com.github.mrzhqiang.helper.captcha.Noise;
import com.github.mrzhqiang.helper.captcha.Ripple;
import com.google.common.base.Preconditions;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.image.WaterFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class WaterRipple implements Ripple {

    private static final Noise DEFAULT_NOISE = new SimpleNoise();

    @Override
    public BufferedImage distort(BufferedImage source) {
        Preconditions.checkNotNull(source, "source == null");

        String noiseClass = SimpleConfig.Producer.NOISE;
        Noise noise = Classes.ofInstance(noiseClass, DEFAULT_NOISE);
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(2.6f);
        rippleFilter.setYAmplitude(1.7f);
        rippleFilter.setXWavelength(15);
        rippleFilter.setYWavelength(5);
        rippleFilter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);

        WaterFilter waterFilter = new WaterFilter();
        waterFilter.setAmplitude(1.5f);
        waterFilter.setPhase(10);
        waterFilter.setWavelength(2);

        BufferedImage effectImage = waterFilter.filter(image, null);
        effectImage = rippleFilter.filter(effectImage, null);

        graphics.drawImage(effectImage, 0, 0, null, null);

        graphics.dispose();

        noise.make(image, .1f, .1f, .25f, .25f);
        noise.make(image, .1f, .25f, .5f, .9f);
        return image;
    }
}
