package in.virajraut.foodiesapi.controller;

import in.virajraut.foodiesapi.io.AuthenticationRequest;
import in.virajraut.foodiesapi.io.AuthenticationResponse;
import in.virajraut.foodiesapi.service.AppUserDetailsService;
import in.virajraut.foodiesapi.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class AuthController {

    public final AuthenticationManager authenticationManager;
    public final AppUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    //Will use authentication manager to authenticate the user >> pass an object of 'UsernamePasswordAuthenticationToken' with user's email and password
    //Next, we need to generate the JWT token for that we need user details >> pass this generated token to 'AuthenticationResponse' >> create generateToken() method in util class
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){
        log.info("Inside /api/login");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (Exception e){
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(request.getEmail(), jwtToken);
    }
}
