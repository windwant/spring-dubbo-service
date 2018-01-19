package org.windwant.spring.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * ftp 上传 Created by robin on 2017/1/10.
 */
@Component
public class FTPTransport {

    private static final Logger logger = LoggerFactory.getLogger(FTPTransport.class);

    private static final int DEFAULT_RETRY_TIMES = 3;

    private static final long DEFAULT_SO_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(1);

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.connectTimeOut}")
    private Integer connectTimeOut;

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param host     FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/201701/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    private boolean uploadFile(String host, int port, String username, String password, String basePath,
                               String filePath, String filename, InputStream input) {
        logger.info("uploadFile host:{}, port:{}, basePath:{}, filePath:{}, filename:{}", host, port, basePath, filePath, filename);
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            if (null != connectTimeOut && connectTimeOut > 0) {
                ftp.setConnectTimeout(connectTimeOut);
            }
            int reply;
            if (port == 0) {
                ftp.connect(host);
                ftp.setSoTimeout((int) DEFAULT_SO_TIMEOUT_MS);
            } else {
                ftp.connect(host, port);// 连接FTP服务器
                ftp.setSoTimeout((int) DEFAULT_SO_TIMEOUT_MS);
            }
            logger.debug("FTP connect 成功");
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("登陆失败，返回code码：" + reply);
                return false;
            }
            logger.info("登陆返回code码：" + reply);
            String path = "";
            if (StringUtils.isNotBlank(basePath) && StringUtils.isBlank(filePath)) {
                path = basePath;
            } else if (StringUtils.isBlank(basePath) && StringUtils.isNotBlank(filePath)) {
                path = filePath;
            } else if (StringUtils.isNotBlank(basePath) && StringUtils.isNotBlank(filePath)) {
                path = basePath + File.separator + filePath;
            }
            path = path.replace("\\", "/");
            if (StringUtils.isNotBlank(path)) {
                if (!changeWorkDirectory(ftp, path)) {
                    logger.info("切换路径失败{}", path);
                    return false;
                }
            }
            ftp.enterLocalActiveMode();
            ftp.setBufferSize(1024);
            ftp.setControlEncoding("UTF-8");
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            result = ftp.storeFile(filename, input);
            logger.info("上传 {} 到 ftp: {}", filename, result);
            ftp.logout();
        } catch (SocketTimeoutException e) {
            logger.error("FTP connect  failed ", e);
            return false;
        } catch (IOException e) {
            logger.error("FTP 异常：", e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("FTP input close failed ", e);
                }
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP connect close failed", ioe);
                }
            }
        }
        return result;
    }


    private boolean changeWorkDirectory(FTPClient ftp, String path) {
        try {
            if (!ftp.changeWorkingDirectory(path)) {
                //如果目录不存在创建目录
                String[] dirs = path.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += File.separator + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return false;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            } else {
                return true;
            }
        } catch (IOException e) {
            logger.error("FTP 切换工作目录失败，", e);
            return false;
        }

        return true;
    }

    /**
     * 增加了三次的重试，
     */
    public boolean uploadFile(String basePath, String filePath, String filename, InputStream input) {
        int retryTimes = 0;
        boolean success = false;
        while (retryTimes < DEFAULT_RETRY_TIMES) {
            success = uploadFile(host, port, username, password, basePath, filePath, filename, input);
            if (success) {
                break;
            }
            retryTimes++;
        }
        return success;
    }

    /**
     * 增加了三次的重试，
     */
    public boolean uploadFile(String filename, InputStream input) {
        return uploadFile("", "", filename, input);
    }

}
