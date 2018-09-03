package org.windwant.spring.service;

import org.windwant.common.api.model.User;

import java.util.Map;

/**
 * BootService.
 */
public interface BootService {

    Map<String, Object> login(User user);

}
