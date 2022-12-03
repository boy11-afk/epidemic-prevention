package com.libo.springboot.imageClassify;



import java.awt.AWTException;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.Robot;

import java.awt.Toolkit;

import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;

public class ReadColor {

    /**

     * 读取一张图片的RGB值

     * @throws Exception

     */

    public void getImagePixel(String image) throws Exception {

        int[] rgb = new int[3];

        File file = new File(image);

        BufferedImage bi = null;

        try {

            bi = ImageIO.read(file);

        } catch (Exception e) {

            e.printStackTrace();

        }

        int width = bi.getWidth();    //拿到当前图片的宽度

        int height = bi.getHeight();  //拿到当前图片的高度

        int minx = bi.getMinX();    //横坐标开始扫描位置

        int miny = bi.getMinY();    //纵坐标开始扫描位置

        System.out.println("当前图片分辨率："+"width=" + width + ",height=" + height + ".");

        System.out.println("开始扫描位置："+"minx=" + minx + ",miniy=" + miny + ".");

        for (int i = minx; i < width; i++) {

            for (int j = miny; j < height; j++) {

                int pixel = bi.getRGB(i, j); //下面三行代码将一个数字转换为RGB数字

                rgb[0] = (pixel & 0xff0000) >> 16;

                rgb[1] = (pixel & 0xff00) >> 8;

                rgb[2] = (pixel & 0xff);

                System.out.println("i="+i+",j="+j+":("+rgb[0]+","+rgb[1]+","+rgb[2]+")");

            }

        }

    }

    /**

     * 返回屏幕色彩值

     *

     * @param x

     * @param y

     * @return

     * @throws AWTException

     */

    public int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。

        Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。

        rb = new Robot();

        Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包

        Dimension di = tk.getScreenSize(); // 屏幕尺寸规格

        Rectangle rec = new Rectangle(0, 0, di.width, di.height);

        BufferedImage bi = rb.createScreenCapture(rec);

        int pixelColor = bi.getRGB(x, y);

        return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。

    }

    /**

     * @param args

     */

    public static void main(String[] args) throws Exception {

        int x = 0;

        ReadColor rc = new ReadColor();

        x = rc.getScreenPixel(100, 345);

        System.out.println("实际颜色值："+x + " - ");

        rc.getImagePixel("src/main/java/com/libo/springboot/imageClassify/行程码.jpg");

        System.out.println("判定结果："+"低风险");

    }

}
