package com.proton.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MUNICIPE_READ("municipe:read"),

    MUNICIPE_UPDATE("municipe:update"),
    MUNICIPE_CREATE("municipe:create"),
    MUNICIPE_DELETE("municipe:delete"),

    FUNCIONARIO_READ("funcionario:read"),
    FUNCIONARIO_UPDATE("funcionario:update"),
    FUNCIONARIO_CREATE("funcionario:create"),
    FUNCIONARIO_DELETE("funcionario:delete"),

    COORDENADOR_READ("coordenador:read"),
    COORDENADOR_UPDATE("coordenador:update"),
    COORDENADOR_CREATE("coordenador:create"),
    COORDENADOR_DELETE("coordenador:delete"),

    SECRETARIO_READ("secretario:read"),
    SECRETARIO_UPDATE("secretario:update"),
    SECRETARIO_CREATE("secretario:create"),
    SECRETARIO_DELETE("secretario:delete")
    
    

    ;

    @Getter
    private final String permission;
}
