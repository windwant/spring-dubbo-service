package org.windwant.common.util;

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
}
