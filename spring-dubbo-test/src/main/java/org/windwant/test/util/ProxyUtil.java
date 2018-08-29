package org.windwant.test.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.windwant.common.util.DateUtil;
import org.windwant.protocal.BootRequestResponse;

/**
 * Created by Administrator on 2018/2/9.
 */
public class ProxyUtil {
    public static BootRequestResponse.BootRequest getBootRequest(int id){
        BootRequestResponse.BootRequest.Builder builder = BootRequestResponse.BootRequest.newBuilder();
        builder.setRequestCode(id);
        builder.setName(String.valueOf(id));
        builder.setSex(1);
        builder.setAccessTime(DateUtil.getCurrentTime());
        return builder.build();
    }

    public static ByteBuf getByteBufBootRequest(int id){
        BootRequestResponse.BootRequest.Builder builder = BootRequestResponse.BootRequest.newBuilder();
        builder.setRequestCode(id);
        builder.setName(String.valueOf(id));
        builder.setSex(1);
        builder.setAccessTime(DateUtil.getCurrentTime());
        return Unpooled.wrappedBuffer(builder.build().toByteArray());
    }
}
