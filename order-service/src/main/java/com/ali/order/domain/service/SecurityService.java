package com.ali.order.domain.service;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUserName() {
        return "user-01";
    }
}
