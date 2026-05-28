package scope.app.controller;

import scope.app.domain.User;
import scope.app.dto.AuthResponse;
import scope.app.dto.LoginRequest;
import scope.app.dto.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scope.app.service.JwtService;
import scope.app.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService =  jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        String email = userService.login(request.email, request.password);
        String token = jwtService.generateToken(email);

        return new AuthResponse(token);
    }
}
