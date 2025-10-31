package com.strhzy.finalpr.service;

import com.strhzy.finalpr.repository.AuthRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    public boolean login(String username, String password, HttpServletResponse response) {
        try {
            Map<String, String> result = authRepository.login(username, password);
            String token = result.get("token");

            if (token != null && !token.isEmpty()) {
                setTokenCookie(response, token);
                HttpService.setToken(token);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean register(String username, String password) {
        try {
            String result = authRepository.register(username, password);
            return result != null && result.contains("зарегистрирован");
        } catch (Exception e) {
            return false;
        }
    }

    public void logout(HttpServletResponse response) {
        removeTokenCookie(response);
        try {
            authRepository.logout();
        } catch (Exception ignored) {
        }
        HttpService.clearToken();
    }

    public String getCurrentUsername(HttpServletRequest request) {
        return isAuthenticated(request) ? "user" : null;
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        return token != null && !token.isEmpty();
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

    private void removeTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}