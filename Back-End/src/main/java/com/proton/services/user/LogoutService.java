package com.proton.services.user;

import com.proton.models.repositories.TokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      org.springframework.security.core.Authentication authentication) {
    // Extrair o valor do cookie "token"
    Cookie[] cookies = request.getCookies();
    String tokenCookieValue = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          tokenCookieValue = cookie.getValue();
          break;
        }
      }
    }

    // Verificar se o valor do cookie é válido
    if (tokenCookieValue != null) {
      var storedToken = tokenRepository.findByToken(tokenCookieValue)
          .orElse(null);
      if (storedToken != null) {
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        SecurityContextHolder.clearContext();

        
        Cookie cookieToDelete = new Cookie("token", null);
        cookieToDelete.setMaxAge(0);
        cookieToDelete.setPath("/"); // Certifique-se de definir o mesmo caminho usado para criar o cookie
        response.addCookie(cookieToDelete);
      }
    }
  }
}

/*
 * Método antigo de logout, que se baseava no auth bearer token. O de cima se
 * baseia em cookies!
 * public void logout(
 * HttpServletRequest request,
 * HttpServletResponse response,
 * Authentication authentication
 * ) {
 * final String authHeader = request.getHeader("Authorization");
 * final String jwt;
 * if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
 * return;
 * }
 * jwt = authHeader.substring(7);
 * var storedToken = tokenRepository.findByToken(jwt)
 * .orElse(null);
 * if (storedToken != null) {
 * storedToken.setExpired(true);
 * storedToken.setRevoked(true);
 * tokenRepository.save(storedToken);
 * SecurityContextHolder.clearContext();
 * }
 * }
 * }
 */