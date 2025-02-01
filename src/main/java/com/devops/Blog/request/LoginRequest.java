package com.devops.Blog.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequest {


 private String email;
 private String password;

 public String getEmail(){
  return email;
 }

 public String getPassword(){
  return password;
 }

}
