package com.github.mrzhqiang.helper.captcha.gif;

import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.WordRenderer;
import com.github.mrzhqiang.helper.text.Names;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

import static com.github.mrzhqiang.helper.random.RandomNumbers.nextInt;
import static com.github.mrzhqiang.helper.random.RandomNumbers.rangeInt;

final class GifWordRenderer implements WordRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GifWordRenderer.class);

    private int fontIndex = -1;
    private int flag = -1;
    private int[][] besselXY;
    private Color[] fontColor;

    public void setFontIndex(int fontIndex) {
        this.fontIndex = fontIndex;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setFontColor(Color[] fontColor) {
        this.fontColor = fontColor;
    }

    @Override
    public BufferedImage render(String word, int width, int height) {
        Preconditions.checkNotNull(word, "word == null");

        int length = word.length();
        if (fontColor == null) {
            fontColor = new Color[length];
            for (int i = 0; i < length; i++) {
                fontColor[i] = Colors.of(Names.randomColor());
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        // 填充背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画干扰圆圈
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * nextInt(10)));  // 设置透明度
        for (int i = 0; i < 2; i++) {
            g2d.setColor(Colors.of(Names.randomColor()));
            int w = 5 + nextInt(10);
            g2d.drawOval(nextInt(width - 25), nextInt(height - 15), w, w);
        }
        // 画干扰线
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));  // 设置透明度
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // 随机生成贝塞尔曲线参数
        if (besselXY == null) {
            int x1 = 5, y1 = rangeInt(5, height / 2);
            int x2 = width - 5, y2 = rangeInt(height / 2, height - 5);
            int ctrlx = rangeInt(width / 4, width / 4 * 3), ctrly = rangeInt(5, height - 5);
            if (nextInt(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            int ctrlx1 = rangeInt(width / 4, width / 4 * 3), ctrly1 = rangeInt(5, height - 5);
            besselXY = new int[][]{{x1, y1}, {ctrlx, ctrly}, {ctrlx1, ctrly1}, {x2, y2}};
        }
        g2d.setColor(fontColor[0]);
        CubicCurve2D shape = new CubicCurve2D.Double(besselXY[0][0], besselXY[0][1], besselXY[1][0], besselXY[1][1], besselXY[2][0], besselXY[2][1], besselXY[3][0], besselXY[3][1]);
        g2d.draw(shape);
        // 画验证码
        if (fontIndex < 0) {
            fontIndex = nextInt(GifConfig.FONTS.size());
        }
        String fontFile = GifConfig.FONTS.get(fontIndex);
        try {
            InputStream fontStream = Objects.requireNonNull(this.getClass().getResourceAsStream("/font/" + fontFile));
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream)
                    .deriveFont(Font.BOLD, 32f);
            g2d.setFont(font);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.BOLD, 32));
            LOGGER.error("无法找到字体文件： {}，异常消息为：{}，将设置默认的 Arial 字体。", fontFile, e.getLocalizedMessage());
        }
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int fW = width / length;  // 每一个字符所占的宽度
        int fSp = (fW - (int) fontMetrics.getStringBounds("W", g2d).getWidth()) / 2;  // 字符的左右边距
        if (flag < 0) {
            flag = nextInt(length);
        }
        for (int i = 0; i < length; i++) {
            // 设置透明度
            AlphaComposite ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i, length));
            g2d.setComposite(ac3);
            g2d.setColor(fontColor[i]);
            String value = String.valueOf(word.charAt(i));
            int fY = height - ((height - (int) fontMetrics.getStringBounds(value, g2d).getHeight()) >> 1);  // 文字的纵坐标
            int x;
            if (Names.checkChinese(value)) {
                x = i * fW + fSp - 3;
            } else {
                x = i * fW + fSp + 3;
            }
            g2d.drawString(value, x, fY - 3);
        }
        g2d.dispose();
        return image;
    }

    private float getAlpha(int i, int j, int len) {
        int num = i + j;
        float r = (float) 1 / (len - 1);
        float s = len * r;
        return num >= len ? (num * r - s) : num * r;
    }
}
