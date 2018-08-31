package org.windwant.spring.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ImageUtil {

	 public static String drawImg(ByteArrayOutputStream output){  
        String code = "";  
        //随机产生4个字符  
        for(int i=0; i<4; i++){  
            code += randomChar();  
        }  
        int width = 70;  
        int height = 25;  
        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);  
        Font font = new Font("Times New Roman",Font.PLAIN,20);  
        //调用Graphics2D绘画验证码  
        Graphics2D g = bi.createGraphics();  
        g.setFont(font);  
        Color color = new Color(66,2,82);  
        g.setColor(color);  
        g.setBackground(new Color(226,226,240));  
        g.clearRect(0, 0, width, height);  
        FontRenderContext context = g.getFontRenderContext();  
        Rectangle2D bounds = font.getStringBounds(code, context);  
        double x = (width - bounds.getWidth()) / 2;  
        double y = (height - bounds.getHeight()) / 2;  
        double ascent = bounds.getY();  
        double baseY = y - ascent;  
        g.drawString(code, (int)x, (int)baseY);  
        g.dispose();  
        try {  
            ImageIO.write(bi, "png", output);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return code;  
    }  
      
    /** 
     * 随机参数一个字符 
     * @return 
     */  
    private static char randomChar(){  
        Random r = new Random();  
        String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";  
        return s.charAt(r.nextInt(s.length()));  
    }  
}
