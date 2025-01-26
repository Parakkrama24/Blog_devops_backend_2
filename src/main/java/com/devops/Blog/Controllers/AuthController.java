package com.devops.Blog.Controllers;

import com.devops.Blog.Exceptions.UserException;
import com.devops.Blog.config.JwtProvider;
import com.devops.Blog.domain.USER_ROLE;
import com.devops.Blog.model.user;
import com.devops.Blog.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import com.devops.Blog.service.userService;
import com.devops.Blog.repository.userRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    // private CustomUserServiceImplementation customUserServiceImplementation;

    private userService userService;

    @Autowired
    public AuthController(userRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          //CustomUserServiceImplementation customUserServiceImplementation,
                          userService userService){

        this.userRepository =userRepository;
        this.passwordEncoder= passwordEncoder;
        this.jwtProvider=jwtProvider;
        // this.customUserServiceImplementation=customUserServiceImplementation;
        this.userService=userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody user user ) throws Exception{
        String email= user.getEmail();
        String password =user.getPassword();
        String fullname= user.getFullName();
        USER_ROLE role=user.getRole();

        user isEmailExist = userRepository.findUserByEmail(email);
        if(isEmailExist != null){
            throw new UserException("Email Is Already used with another account");
        }

        //create new user
        user createUser= new user();
        createUser.setEmail(email);
        createUser.setFullName(fullname);
        createUser.setPassword(passwordEncoder.encode(password));
        createUser.setRole(role);

        user savedUser= userRepository.save(createUser);

        List<GrantedAuthority> authorities=new ArrayList<>();

        Authentication authentication= new UsernamePasswordAuthenticationToken(email,password,authorities);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);


    }

    @GetMapping("/")
    public String hello(){
        return "Hi";
    }
}
