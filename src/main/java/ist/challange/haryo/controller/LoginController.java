package ist.challange.haryo.controller;

import ist.challange.haryo.model.Role;
import ist.challange.haryo.model.User;
import ist.challange.haryo.repository.RoleRepository;
import ist.challange.haryo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/")
public class LoginController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User user){
        if (user.getUsername().isEmpty()) return new ResponseEntity<>("Username atau password kosong",HttpStatus.BAD_REQUEST);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        if(!authentication.isAuthenticated())return new ResponseEntity<>("Username dan password salah", HttpStatus.UNAUTHORIZED);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfully!", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        if(userRepository.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roles = roleRepository.findByName("User").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("Registration has successfully!", HttpStatus.CREATED);
    }
}
