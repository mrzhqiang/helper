package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.Classes;
import com.github.mrzhqiang.helper.captcha.Noise;
import com.github.mrzhqiang.helper.captcha.Ripple;
import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.google.common.base.Preconditions;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.TransformFilter;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class ShadowRipple implements Ripple {

    private final Config config;
    private final Noise defaultNoise;

    public ShadowRipple(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
        this.defaultNoise = new SimpleNoise(this.config);
    }

    @Override
    public BufferedImage distort(BufferedImage source) {
        Preconditions.checkNotNull(source, "source == null");

        BufferedImage distortedImage = new BufferedImage(source.getWidth(), source.getHeight(), TYPE_INT_ARGB);
        Graphics2D graph = (Graphics2D) distortedImage.getGraphics();

        ShadowFilter shadowFilter = new ShadowFilter();
        shadowFilter.setRadius(10);
        shadowFilter.setDistance(5);
        shadowFilter.setOpacity(1);

        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(7.6f);
        rippleFilter.setYAmplitude(RandomNumbers.nextFloat() + 1.0f);
        rippleFilter.setXWavelength(RandomNumbers.nextInt(7) + 8);
        rippleFilter.setYWavelength(RandomNumbers.nextInt(3) + 2);
        rippleFilter.setEdgeAction(TransformFilter.BILINEAR);

        BufferedImage effectImage = rippleFilter.filter(source, null);
        effectImage = shadowFilter.filter(effectImage, null);

        graph.drawImage(effectImage, 0, 0, null, null);
        graph.dispose();

        String noiseClass = config.getString("producer.noise");
        Noise noise = Classes.ofInstance(noiseClass, defaultNoise);
        // draw lines over the image and/or text
        noise.make(distortedImage, .1f, .1f, .25f, .25f);
        noise.make(distortedImage, .1f, .25f, .5f, .9f);

        return distortedImage;
    }
}
