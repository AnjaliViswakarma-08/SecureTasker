package com.securetasker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
  private String name;
  private String email;
  private String role;
}
