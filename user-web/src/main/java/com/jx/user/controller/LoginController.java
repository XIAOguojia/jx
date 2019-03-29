package com.jx.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/29
 * Time:17:18
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/name")
    public Map showName(){
        //登录人的名称
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("username",name);
        return map;
    }
}
