package ist.challange.haryo.controller;

import ist.challange.haryo.model.Role;
import ist.challange.haryo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/roles")
public class RoleController {
    private RoleRepository roleRepository;
    @PostMapping
    public ResponseEntity<String> saveRoles(@RequestBody Role role){
        roleRepository.save(role);
        return new ResponseEntity<>("Role Berhasil dibuat", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteRoles(@PathVariable("id")Long id){
        roleRepository.deleteById(id);
        return new ResponseEntity<>("Role Berhasil Dihapus", HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> UpdateRoles(@PathVariable("id")Long id,
                                            @RequestBody Role role){
        if(role.getName().isEmpty())
            return new ResponseEntity<>("Role name kosong", HttpStatus.BAD_REQUEST);
        if(roleRepository.findByName(role.getName()).isPresent())
            return new ResponseEntity<>("Role Sudah ada", HttpStatus.CONFLICT);
        roleRepository.findById(id)
                .map(dataRole->{
                    dataRole.setName(role.getName());
                    return roleRepository.save(dataRole);
                });
        return new ResponseEntity<>("Role Berhasil diubah", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Role>> findAllRoles(){
        return ResponseEntity.ok(roleRepository.findAll());
    }
}
