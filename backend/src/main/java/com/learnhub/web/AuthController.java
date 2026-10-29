package com.learnhub.web;

import com.learnhub.model.User;
import com.learnhub.repo.UserRepository;
import com.learnhub.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public record RegisterReq(@NotBlank String name, @NotBlank @Email String email,
                              @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password) {}
    public record LoginReq(@NotBlank String email, @NotBlank String password) {}
    public record UserDto(Long id, String name, String email, String role) {}
    public record AuthRes(String token, UserDto user) {}

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthRes register(@Valid @RequestBody RegisterReq r) {
        String email = r.email().trim().toLowerCase();
        if (users.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        User u = new User();
        u.name = r.name().trim(); u.email = email; u.passwordHash = encoder.encode(r.password());
        users.save(u);
        return respond(u);
    }

    @PostMapping("/login")
    public AuthRes login(@Valid @RequestBody LoginReq r) {
        User u = users.findByEmail(r.email().trim().toLowerCase())
                .filter(x -> encoder.matches(r.password(), x.passwordHash))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password is incorrect"));
        return respond(u);
    }

    private AuthRes respond(User u) {
        return new AuthRes(jwt.issue(u), new UserDto(u.id, u.name, u.email, u.role));
    }
}
