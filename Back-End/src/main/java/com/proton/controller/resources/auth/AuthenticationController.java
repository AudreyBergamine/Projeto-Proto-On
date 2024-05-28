package com.proton.controller.resources.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.controller.resources.auth.requests.RegisterRequest;
import com.proton.controller.resources.auth.requests.RegisterRequestFuncionario;
import com.proton.controller.resources.auth.requests.RegisterRequestMunicipe;
import com.proton.services.user.AuthenticationService;

import java.io.IOException;

@RestController
@RequestMapping("/protoon/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/register/municipe")
  public ResponseEntity<AuthenticationResponse> registerMunicipe(
    @RequestBody RegisterRequestMunicipe request,
    HttpServletResponse httpResponse // Inject HttpServletResponse
) {
  
    AuthenticationResponse authenticationResponse = service.registerMunicipe(request);
    // Set the access token as an HttpOnly cookie in the response
    // Cookie tokenCookie = new Cookie("token", authenticationResponse.getAccessToken());
    // tokenCookie.setHttpOnly(true); // Set HttpOnly flag
    // tokenCookie.setPath("/"); // Set cookie path as needed
    // httpResponse.addCookie(tokenCookie);

    // Optionally, you can also set the refresh token as a separate HttpOnly cookie if needed

    return ResponseEntity.ok(authenticationResponse);
}

@PostMapping("/register/funcionario/{id_s}")
  public ResponseEntity<AuthenticationResponse> registerFuncionario(
    @RequestBody RegisterRequestFuncionario request,
    @PathVariable Long id_s,
    HttpServletResponse httpResponse // Inject HttpServletResponse
) {
  
    AuthenticationResponse authenticationResponse = service.registerFuncionario(request, id_s);
    // Set the access token as an HttpOnly cookie in the response
    // Cookie tokenCookie = new Cookie("token", authenticationResponse.getAccessToken());
    // tokenCookie.setHttpOnly(true); // Set HttpOnly flag
    // tokenCookie.setPath("/"); // Set cookie path as needed
    // httpResponse.addCookie(tokenCookie);

    // Optionally, you can also set the refresh token as a separate HttpOnly cookie if needed

    return ResponseEntity.ok(authenticationResponse);
}


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse httpResponse
    ) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);

        // Criar o cookie manualmente e adicionar os atributos SameSite e Secure
        ResponseCookie cookie = ResponseCookie.from("token", authenticationResponse.getAccessToken())
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .secure(true)
                .build();

        // Adicionar o cookie na resposta HTTP
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(authenticationResponse);
    }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }




}
