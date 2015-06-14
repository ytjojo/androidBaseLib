package com.kerkr.edu.math;

import java.util.Random;


import android.graphics.Color;

/**
 * 随机数 生成器
 * 
 * @author chenxin
 * @version [版本号, 2012-5-21]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RandomUtil {

	/**
     *该函数获得随机数字符串
     * 
     * @param iLen int :需要获得随机数的长度
     * @param iType int:随机数的类型：'0':表示仅获得数字随机数；'1'：表示仅获得字符随机数；'2'：表示获得数字字符混合随机数
     * @since 1.0.0
     * @return String
     */
    public static final String createRadom(int iLen, int iType)
    {
        StringBuffer strRandom = new StringBuffer();// 随机字符串
        Random rnd = new Random();
        if (iLen < 0)
        {
            iLen = 5;
        }
        if ((iType > 2) || (iType < 0))
        {
            iType = 2;
        }
        switch (iType)
        {
            case 0:
                for (int iLoop = 0; iLoop < iLen; iLoop++)
                {
                    strRandom.append(rnd.nextInt(10));
                }
                break;
            case 1:
                for (int iLoop = 0; iLoop < iLen; iLoop++)
                {
                    strRandom.append(Integer.toString(35 - rnd.nextInt(10), 36));
                }
                break;
            case 2:
                for (int iLoop = 0; iLoop < iLen; iLoop++)
                {
                    strRandom.append(Integer.toString(rnd.nextInt(36), 36));
                }
                break;
        }
        return strRandom.toString();
    }
    
    /**
     * get a integer array filled with random integer without reduplicate [min, max)
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param size the capacity of the array
     * @return a integer array filled with random integer without redupulicate
     */
    public static int[] getRandomIntWithoutReduplicate(int min, int max, int size)
    {
        int[] result = new int[size];
        
        int arraySize = max - min;
        int[] intArray = new int[arraySize];
        // init intArray
        for (int i = 0; i < intArray.length; i++)
        {
            intArray[i] = i + min;
        }
        // get randome interger without reduplicate
        for (int i = 0; i < size; i++)
        {
            int c = getRandomInt(min, max - i);
            int index = c - min;
            swap(intArray, index, arraySize - 1 - i);
            result[i] = intArray[arraySize - 1 - i];
        }
        
        return result;
    }
    
    private static void swap(int[] array, int x, int y)
    {
        int temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }
    
    /**
     * get a random Integer with the range [min, max)
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @return the random Integer value
     */
    public static int getRandomInt(int min, int max)
    {
        // include min, exclude max
        int result = min + (int)(Math.random() * (max - min));
        
        return result;
    }
    
    /**
     * get a random double with the range [min, max)
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @return the random double value
     */
    public static double getRandomDouble(double min, double max)
    {
        // include min, exclude max
        double result = min + (Math.random() * (max - min));
        return result;
    }
    
    /**
     * a random char with the range ASCII 33(!) to ASCII 126(~)
     * <功能详细描述>
     * @return char
     * @see [类、类#方法、类#成员]
     */
    public static char getRandomChar()
    {
        // from ASCII code 33 to ASCII code 126
        int firstChar = 33; // "!"
        int lastChar = 126; // "~"
        char result = (char)(getRandomInt(firstChar, lastChar + 1));
        return result;
    }
    
    /**
     * a random char with the range [0-9],[a-z],[A-Z]
     * <功能详细描述>
     * @return char
     * @see [类、类#方法、类#成员]
     */
    public static char getRandomNormalChar()
    {
        // include 0-9,a-z,A-Z
        int number = getRandomInt(0, 62);
        int zeroChar = 48;
        int nineChar = 57;
        int aChar = 97;
        int zChar = 122;
        int capitalAChar = 65;
        int capitalZChar = 90;
        
        char result;
        
        if (number < 10)
        {
            result = (char)(getRandomInt(zeroChar, nineChar + 1));
            return result;
            
        }
        else if (number >= 10 && number < 36)
        {
            result = (char)(getRandomInt(capitalAChar, capitalZChar + 1));
            return result;
        }
        else if (number >= 36 && number < 62)
        {
            result = (char)(getRandomInt(aChar, zChar + 1));
            return result;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * getRandomString
     * @param length the length of the String
     * @return a String filled with random char
     */
    public static String getRandomString(int length)
    {
        // include ASCII code from 33 to 126
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            result.append(getRandomChar());
        }
        return result.toString();
    }
    
    /**
     * getRandomNormalString
     * @param length the length of the String
     * @return a String filled with normal random char
     */
    public static String getRandomNormalString(int length)
    {
        // include 0-9,a-z,A-Z
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            result.append(getRandomNormalChar());
        }
        return result.toString();
    }



    //给定范围获得随机颜色 
    public Color getRandColor(int fc, int bc) {   
        Random random = new Random();    
        if (fc > 255)    
            fc = 255;    
        if (bc > 255)    
            bc = 255;    
        int r = fc + random.nextInt(bc - fc);    
        int g = fc + random.nextInt(bc - fc);    
        int b = fc + random.nextInt(bc - fc); 
        Color c= new Color();
        c.argb(255, r, g, b);
        return c;    
    }



}