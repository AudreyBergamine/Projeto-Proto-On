package com.proton.controller.resources.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proton.models.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("id") // Add annotation for the ID field
  private Integer id;
  @JsonProperty("role")
  private Role role;
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
}
