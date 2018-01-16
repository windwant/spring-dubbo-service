package org.windwant.spring.service;

import org.windwant.spring.model.Guest;
import org.windwant.spring.model.User;

import java.util.Map;

/**
 * BootService.
 */
public interface BootService {

    String hello(String name);

    String hellox(Guest guest);

    Map<String, Object> login(User user);
}
