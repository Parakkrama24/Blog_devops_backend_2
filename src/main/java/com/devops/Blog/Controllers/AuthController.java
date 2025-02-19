package com.devops.Blog.Controllers;

import com.devops.Blog.Exceptions.UserException;
import com.devops.Blog.config.JwtProvider;
import com.devops.Blog.domain.USER_ROLE;
import com.devops.Blog.model.user;
import com.devops.Blog.request.LoginRequest;
import com.devops.Blog.response.AuthResponse;
import com.devops.Blog.service.CustomUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.devops.Blog.service.userService;
import com.devops.Blog.repository.userRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private CustomUserServiceImplementation customUserServiceImplementation;

    private userService userService;

    @Autowired
    public AuthController(userRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          CustomUserServiceImplementation customUserServiceImplementation,
                          userService userService){

        this.userRepository =userRepository;
        this.passwordEncoder= passwordEncoder;
        this.jwtProvider=jwtProvider;
        this.customUserServiceImplementation=customUserServiceImplementation;
        this.userService=userService;
    }

    @PostMapping("/signup") // Maps HTTP POST requests to "/signup" endpoint
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody user user) throws Exception {

        // Extract user details from the request body
        String email = user.getEmail();
        String password = user.getPassword();
        String fullname = user.getFullName();
        USER_ROLE role = user.getRole();

        // Check if the email is already registered
        user isEmailExist = userRepository.findUserByEmail(email);
        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account"); // Throw exception if email exists
        }

        // Create a new user object and set the details
        user createUser = new user();
        createUser.setEmail(email);
        createUser.setFullName(fullname);
        createUser.setPassword(passwordEncoder.encode(password)); // Encrypt password before saving
        createUser.setRole(role);

        // Save the new user to the database
        user savedUser = userRepository.save(createUser);

        // Create an empty list of authorities (roles/permissions) for the authentication token
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Create an authentication token for the newly registered user
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, authorities);

        // Generate a JWT token for the user
        String token = jwtProvider.generateToken(authentication);

        // Prepare the authentication response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token); // Set JWT token
        authResponse.setMessage("Register Success"); // Set success message
        authResponse.setRole(savedUser.getRole()); // Set the user's role in the response

        // Return the response entity with authentication details and HTTP status 200 (OK)
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @RequestMapping("/sign-in") // Maps HTTP requests to "/sign-in" endpoint
    public ResponseEntity<AuthResponse> signInHandler( @RequestBody LoginRequest loginRequest) {

        // Extract email and password from the request body
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Print email and password for debugging purposes (remove in production)
        System.out.println(email + " : " + password);

        // Authenticate the user with the provided credentials
        Authentication authentication = authenticate(email, password);

        // Store the authentication object in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate a JWT token for the authenticated user
        String token = jwtProvider.generateToken(authentication);

        // Create an AuthResponse object to send back the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Success"); // Set success message
        authResponse.setJwt(token); // Set the generated JWT token

        // Get the user roles from the authentication object
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Extract the first role (assuming single-role users)
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName)); // Set the role in the response

        // Return the response entity with the authentication details and HTTP status 200 (OK)
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }


    private Authentication authenticate(String email, String password) {

        // Load user details using the provided email (username)
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(email);

        // Check if the user exists in the system
        if (userDetails == null) {
            System.out.println("Sign-in details null"); // Debugging statement (remove in production)
            throw new BadCredentialsException("Invalid username or password"); // Throw exception if user not found
        }

        // Verify if the provided password matches the stored (hashed) password
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("Sign-in user details mismatch"); // Debugging statement
            throw new BadCredentialsException("Invalid username or password"); // Throw exception if password is incorrect
        }

        // Create an authentication token for the user with their roles/authorities
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    @GetMapping("/")
    public String hello(){
        return "Hi";
    }
}
