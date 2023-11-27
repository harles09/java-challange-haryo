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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/")
public class LoginController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) return new ResponseEntity<>("Username atau password kosong",HttpStatus.BAD_REQUEST);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(AuthenticationException e) {
            return new ResponseEntity<>("Username dan password salah", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Sukses Login", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        if(userRepository.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("Username sudah terpakai", HttpStatus.BAD_REQUEST);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roles = roleRepository.findByName("admin").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("Registration has successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>("User logout successfully!", HttpStatus.OK);
    }

}
