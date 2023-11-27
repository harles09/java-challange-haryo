package ist.challange.haryo.controller;

import ist.challange.haryo.model.User;
import ist.challange.haryo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {
    private UserRepository userRepository;
//    private PasswordEncoder passwordEncoder;
    @GetMapping
    public ResponseEntity<List<User>> findAllUser(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> changeUserData(@PathVariable("id") Long id,@RequestBody User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username sudah terpakai", HttpStatus.CONFLICT);
        } else if (userRepository.existsByPassword(user.getPassword())) {
            return new ResponseEntity<>("Password tidak boleh sama dengan password sebelumnya"
            ,HttpStatus.BAD_REQUEST);
        }
        userRepository.findById(id)
                .map(dataUser->{
                    if(user.getUsername() !=null &&!user.getUsername().isEmpty()) {
                        dataUser.setUsername(user.getUsername());
                    }
                    if(user.getPassword() != null && !user.getPassword().isEmpty()) {
                        dataUser.setPassword(user.getPassword());
                    }
                    return userRepository.save(dataUser);
                });
        return new ResponseEntity<>("User Berhasil Diubah", HttpStatus.CREATED);
    }
}
