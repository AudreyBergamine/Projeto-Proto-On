package com.proton.config;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite(null); // Configura SameSite como "None"
        serializer.setUseSecureCookie(true); // Garante que o cookie seja enviado apenas em conexões HTTPS seguras
        return serializer;
    }
}