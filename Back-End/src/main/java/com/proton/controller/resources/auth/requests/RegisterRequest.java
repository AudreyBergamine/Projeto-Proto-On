package com.proton.controller.resources.auth.requests;

import com.proton.models.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String nome;
  private String email;
  private String senha;
  private Role role;
}
