package com.proton.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

// import static com.proton.models.entities.roles.Permission.ADMIN_CREATE;
// import static com.proton.models.entities.roles.Permission.ADMIN_DELETE;
// import static com.proton.models.entities.roles.Permission.ADMIN_READ;
// import static com.proton.models.entities.roles.Permission.ADMIN_UPDATE;
// import static com.proton.models.entities.roles.Permission.MUNICIPE_CREATE;
// import static com.proton.models.entities.roles.Permission.MUNICIPE_DELETE;
// import static com.proton.models.entities.roles.Permission.MUNICIPE_READ;
// import static com.proton.models.entities.roles.Permission.MUNICIPE_UPDATE;
// import static com.proton.models.entities.roles.Role.ADMIN;
// import static com.proton.models.entities.roles.Role.MUNICIPE;
// import static org.springframework.http.HttpMethod.DELETE;
// import static org.springframework.http.HttpMethod.GET;
// import static org.springframework.http.HttpMethod.POST;
// import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

        // Lista que permite rotas que todos tem acesso...
        private static final String[] WHITE_LIST_URL = { "/protoon/auth/**",
                        // "/protoon/municipe/municipes",
                        // "/protoon/municipe/endereco",
                        "/**",

                        "/webjars/**",
                        "/swagger-ui.html" };
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutHandler logoutHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors().and()
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                                                .permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()

                                               // .requestMatchers("/protoon/municipe/municipes").permitAll()

                                                //.requestMatchers("/protoon/municipe/municipes/**").permitAll()

                                               // .requestMatchers("/protoon/auth/register/municipe").permitAll()
                                                
                                                // .requestMatchers("/protoon/protocolo/pesquisar-id/{id}",
                                                //                 "/protoon/protocolo/todos-protocolos",
                                                //                 "/protoon/protocolo/pesquisar-municipe/{nomeMunicipe}")
                                                // .hasAnyAuthority("MUNICIPE")

                                                // .requestMatchers("/protoon/municipe/**").hasAnyRole(ADMIN.name(),
                                                // MUNICIPE.name())
                                                // .requestMatchers(GET,
                                                // "/protoon/municipe/**").hasAnyAuthority(ADMIN_READ.name(),
                                                // MUNICIPE_READ.name())
                                                // .requestMatchers(POST,
                                                // "/api/v1/municipe_auth/**").hasAnyAuthority(ADMIN_CREATE.name(),
                                                // MUNICIPE_CREATE.name())
                                                // .requestMatchers(PUT,
                                                // "/api/v1/municipe_auth/**").hasAnyAuthority(ADMIN_UPDATE.name(),
                                                // MUNICIPE_UPDATE.name())
                                                // .requestMatchers(DELETE,
                                                // "/api/v1/municipe_auth/**").hasAnyAuthority(ADMIN_DELETE.name(),
                                                // MUNICIPE_DELETE.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout.logoutUrl("/protoon/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response,
                                                                authentication) -> SecurityContextHolder
                                                                                .clearContext()))
                                .headers().frameOptions().disable(); // Desabilitar proteção de frame

                return http.build();
        }
}
