package org.windwant.util;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 18-4-11.
 */
public class NetUtil {
    public static String getHost(){
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "localhost";
    }

    public static String formatRMIServiceName(String host, int port, String className){
        return String.format("rmi://%s:%d/%s", host, port, className);
    }
}
