package com.zb.common.code;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DummyWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.util.Random;

/**
 * Created by zhangbo on 17-8-1.
 */
public class VerifImage extends ListImageCaptchaEngine {
    static {
        System.setProperty("java.awt.headless", "true");
    }

    //壹 贰 叁 肆 伍 陆 柒 捌 玖
    private final static String[] nods = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    //一，二，三，四，五，六，七，八，九
    private final static String[] nohs = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    //0 1 2 3 4 5 6 7 8 9
    private final static String[] noss = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    // 存放加减乘运算符
    private final static char[] arr = {'+', '-', '×'};
    //汉字加减乘运算符
    private final static char[] arrh = {'加', '减', '乘'};
    private StringBuffer sb = null;
    private Random random = null;

    private int result;

    public int getResult() {
        return result;
    }

    @Override
    protected void buildInitialFactories() {
        random = new Random();
        sb = new StringBuffer();
        //获取一个随机boolean
        boolean bool = random.nextBoolean();
        //掉用生成运算模式方法
        this.model(bool);
        int len = sb.toString().length();
        WordGenerator wgen = new DummyWordGenerator(sb.toString());
        //设置字体颜色 比较正常的字体 new int[]{0, 60}, new int[]{0, 60}, new int[]{0, 60}
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(new int[]{0, 60}, new int[]{0, 60}, new int[]{0, 60});
        // 文字显示的个数
        TextPaster textPaster = new RandomTextPaster(len, len, cgen, true);
        //设置图片背景颜色  比较正常的背景颜色  new int[]{200, 200}, new int[]{200, 200}, new int[]{200, 200}
        ColorGenerator colorGenerator = new RandomRangeColorGenerator(new int[]{200, 200}, new int[]{200, 200}, new int[]{200, 200});
        // 图片的大小
        BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(92, 30, colorGenerator);
        // 字体格式
        Font[] fontsList = new Font[]{new Font("华文细黑", 0, 10)};
        // 文字的大小
        FontGenerator fontGenerator = new RandomFontGenerator(21, 21, fontsList);
        //将文字写入到图片中
        WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
        this.addFactory(new GimpyFactory(wgen, wordToImage));
    }

    private void model(boolean bool) {
        // 生成随机整数num1
        int num1 = getRandomNum();
        // 生成随机整数num2
        int num2 = getRandomNum();
        //随机产生运算方式
        int operate = random.nextInt(3);
        switch (operate) {
            case 0:
                this.result = num1 + num2;
                break;
            case 1:
                this.result = num1 - num2;
                break;
            case 2:
                this.result = num1 * num2;
                break;
        }
        try {
            //随机生成运算表达试
            if (bool) {
                this.getRes(operate, num1, num2);
            } else {
                int r = random.nextInt(3);
                sb.append(getNos(r, num1) + " ");

                r = random.nextInt(2);
                sb.append(getOperate(r, operate) + " ");

                r = random.nextInt(3);
                sb.append(getNos(r, num2));

                sb.append(" = ?");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成运算表达试
    private void getRes(int operate, int num1, int num2) {
        int r = random.nextInt(2);
        switch (r) {
            case 0:
                r = random.nextInt(3);
                sb.append(getNos(r, num1) + " ");

                r = random.nextInt(2);
                sb.append(getOperate(r, operate) + " ");
                sb.append("?");

                sb.append(" = " + this.result);
                this.result = num2;
                break;
            case 1:
                sb.append("? ");
                r = random.nextInt(2);
                sb.append(getOperate(r, operate) + " ");

                r = random.nextInt(3);
                sb.append(getNos(r, num2));

                sb.append(" = " + this.result);
                this.result = num1;
                break;
        }
    }

    //获取运算数字
    private String getNos(int random, int num) {
        switch (random) {
            case 0:
                return nods[num];
            case 1:
                return nohs[num];
            case 2:
                return noss[num];
        }
        return null;
    }

    //获取运算符
    private String getOperate(int random, int num) {
        switch (random) {
            case 0:
                return String.valueOf(arr[num]);
            case 1:
                return String.valueOf(arrh[num]);
        }
        return null;
    }

    //获取0~9随机数
    private int getRandomNum() {
        int num = random.nextInt(10);
        while (num == 0) {
            num = random.nextInt(10);
        }
        return num;
    }
}
