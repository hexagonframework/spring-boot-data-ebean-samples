package sample.spring.data.ebean.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.spring.data.ebean.domain.User;
import sample.spring.data.ebean.domain.UserRepository;

/**
 * @author Xuegui Yuan
 */
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  UserRepository userRepository;

  @PostMapping()
  void createUser(@RequestBody User user) {
    userRepository.save(user);
  }

  @PutMapping("/{id}")
  void updateUser(@PathVariable Long id, @RequestBody User user) {
    user.setId(id);
    userRepository.update(user);
  }

  @GetMapping(path = "/{id}")
  User getUserDetails(@PathVariable Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @GetMapping()
  List<User> getUserList() {
    List<User> users = userRepository.findAll();
    return users;
  }

}
