// package com.proton.config;

// import com.proton.models.repositories.TokenRepository;
// import com.proton.services.jwt.JwtService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;

// @Component
// @RequiredArgsConstructor
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;
//     private final UserDetailsService userDetailsService;
//     private final TokenRepository tokenRepository;

//     @Override
//     protected void doFilterInternal(
//             HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain filterChain
//     ) throws ServletException, IOException {
//         if (request.getServletPath().contains("/protoon/auth")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String jwt = null;
//         Cookie[] cookies = request.getCookies();
//         if (cookies != null) {
//             for (Cookie cookie : cookies) {
//                 if (cookie.getName().equals("token")) {
//                     jwt = cookie.getValue();
//                     break;
//                 }
//             }
//         }

//         if (jwt == null) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String userEmail = jwtService.extractUsername(jwt);
//         if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//             boolean isTokenValid = tokenRepository.findByToken(jwt)
//                     .map(t -> !t.isExpired() && !t.isRevoked())
//                     .orElse(false);
//             if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                         userDetails,
//                         null,
//                         userDetails.getAuthorities()
//                 );
//                 authToken.setDetails(
//                         new WebAuthenticationDetailsSource().buildDetails(request)
//                 );
//                 SecurityContextHolder.getContext().setAuthentication(authToken);
//             }
//         }
//         filterChain.doFilter(request, response);
//     }
// }

/*
 * Versão antiga do código, ele trabalha com tokens no header (menos seruro)!
 * 
 *  */
package com.proton.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.beans.Transient;
import java.io.IOException;
import java.security.Security;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.proton.models.repositories.TokenRepository;
import com.proton.services.jwt.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/protoon/auth")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var isTokenValid = tokenRepository.findByToken(jwt)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}

