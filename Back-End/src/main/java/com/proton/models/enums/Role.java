package com.proton.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.proton.models.enums.Permission.ADMIN_CREATE;
import static com.proton.models.enums.Permission.ADMIN_DELETE;
import static com.proton.models.enums.Permission.ADMIN_READ;
import static com.proton.models.enums.Permission.ADMIN_UPDATE;
import static com.proton.models.enums.Permission.COORDENADOR_CREATE;
import static com.proton.models.enums.Permission.COORDENADOR_DELETE;
import static com.proton.models.enums.Permission.COORDENADOR_READ;
import static com.proton.models.enums.Permission.COORDENADOR_UPDATE;
import static com.proton.models.enums.Permission.FUNCIONARIO_CREATE;
import static com.proton.models.enums.Permission.FUNCIONARIO_DELETE;
import static com.proton.models.enums.Permission.FUNCIONARIO_READ;
import static com.proton.models.enums.Permission.FUNCIONARIO_UPDATE;
import static com.proton.models.enums.Permission.MUNICIPE_CREATE;
import static com.proton.models.enums.Permission.MUNICIPE_DELETE;
import static com.proton.models.enums.Permission.MUNICIPE_READ;
import static com.proton.models.enums.Permission.MUNICIPE_UPDATE;
import static com.proton.models.enums.Permission.SECRETARIO_CREATE;
import static com.proton.models.enums.Permission.SECRETARIO_DELETE;
import static com.proton.models.enums.Permission.SECRETARIO_READ;
import static com.proton.models.enums.Permission.SECRETARIO_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  ADMIN(
          Set.of(
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE,
                  MUNICIPE_READ,
                  MUNICIPE_UPDATE,
                  MUNICIPE_DELETE,
                  MUNICIPE_CREATE
          )
  ),
  MUNICIPE(
          Set.of(
                  MUNICIPE_READ,
                  MUNICIPE_UPDATE,
                  MUNICIPE_DELETE,
                  MUNICIPE_CREATE
          )
  ),

  FUNCIONARIO(
        Set.of(
                FUNCIONARIO_READ,
                FUNCIONARIO_UPDATE,
                FUNCIONARIO_DELETE,
                FUNCIONARIO_CREATE
        )
        ),

        COORDENADOR(
                Set.of(
                        COORDENADOR_READ,
                        COORDENADOR_UPDATE,
                        COORDENADOR_DELETE,
                        COORDENADOR_CREATE
                )
        ),

        
        SECRETARIO(
                Set.of(
                        SECRETARIO_READ,
                        SECRETARIO_UPDATE,
                        SECRETARIO_DELETE,
                        SECRETARIO_CREATE
                )
        ),



  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
