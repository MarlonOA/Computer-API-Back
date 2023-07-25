package project.ufrn.pw.api_rest.controller;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.ufrn.pw.api_rest.config.LoginDTO;
import project.ufrn.pw.api_rest.service.TokenService;

@RestController
@RequestMapping("/token")
@CrossOrigin
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public String token(@RequestBody @Valid LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password()));
        return tokenService.generateToken(authentication);
    }
}