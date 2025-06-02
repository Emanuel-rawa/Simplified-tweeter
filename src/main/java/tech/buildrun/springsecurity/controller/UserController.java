package tech.buildrun.springsecurity.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import tech.buildrun.springsecurity.controller.dto.CreateUserDto;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

/**
 * Controlador REST para gerenciamento de usuários do sistema.
 * 
 * Permite o cadastro de novos usuários e listagem de usuários existentes.
 * A listagem de usuários está protegida para usuários com permissão ADMIN.
 * 
 * Utiliza criptografia BCrypt para armazenar senhas de forma segura.
 * 
 * @author Emanuel
 */
@RestController
public class UserController {

  /**
   * Repositório para acesso e persistência dos dados dos usuários.
   */
  private final UserRepository userRepository;

  /**
   * Repositório para acesso aos dados de roles (perfis/permissões).
   */
  private final RoleRepository roleRepository;

  /**
   * Encoder BCrypt para criptografia segura de senhas.
   */
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * Construtor com injeção dos repositórios e do encoder de senha.
   * 
   * @param userRepository        repositório para usuários.
   * @param roleRepository        repositório para roles.
   * @param bCryptPasswordEncoder encoder para criptografar senhas.
   */
  public UserController(UserRepository userRepository, RoleRepository roleRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  /**
   * Endpoint HTTP POST para criação de um novo usuário.
   * 
   * Recebe um DTO com nome de usuário e senha, valida se o usuário já existe
   * e cria um novo usuário com a role básica padrão.
   * 
   * O método é transacional para garantir atomicidade da operação.
   * 
   * @param createUserDto DTO contendo os dados para cadastro do usuário.
   * @return resposta HTTP 200 OK em caso de sucesso.
   * @throws ResponseStatusException com status 422 (Unprocessable Entity) caso o
   *                                 username já exista.
   */
  @PostMapping("/users")
  @Transactional
  public ResponseEntity<Void> newUser(@RequestBody CreateUserDto createUserDto) {

    var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

    var userFromDb = userRepository.findByUsername(createUserDto.username());

    if (userFromDb.isPresent()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var user = new User();
    user.setUsername(createUserDto.username());
    user.setPassword(bCryptPasswordEncoder.encode(createUserDto.password()));
    user.setRoles(Set.of(basicRole));

    userRepository.save(user);

    return ResponseEntity.ok().build();
  }

  /**
   * Endpoint HTTP GET para listar todos os usuários cadastrados.
   * 
   * Recurso protegido, acessível apenas para usuários com autoridade ADMIN.
   * 
   * @return lista de usuários do sistema com resposta HTTP 200 OK.
   */
  @GetMapping("/users")
  @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
  public ResponseEntity<List<User>> listUsers() {
    var users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }

}
