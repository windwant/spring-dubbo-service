package org.windwant.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.windwant.common.api.DubboService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2018/2/6.
 */
@Service("dubbosvr")
public class DubboServiceImpl implements DubboService {
    private static final Logger logger = LoggerFactory.getLogger(DubboServiceImpl.class);
    @Override
    public String getSysTime() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        logger.info("current time: {}", currentTime);
        return currentTime;
    }
}
