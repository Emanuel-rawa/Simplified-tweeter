package tech.buildrun.springsecurity.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
import java.util.Set;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

  private RoleRepository roleRepository;

  private UserRepository userRepository;

  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public AdminUserConfig(BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository,
      UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

    var userAdmin = userRepository.findByUsername("admin");

    userAdmin.ifPresentOrElse(
        user -> {
          System.out.println("admin already exists");
        },
        () -> {
          var user = new User();
          user.setUsername("admin");
          user.setPassword(bCryptPasswordEncoder.encode("123"));
          user.setRoles(Set.of(roleAdmin));
          userRepository.save(user);
        });
  }
}
