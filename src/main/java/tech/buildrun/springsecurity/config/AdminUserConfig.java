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

/**
 * Classe de configuração responsável pela criação de um usuário administrador
 * padrão
 * na inicialização da aplicação, caso ele ainda não exista.
 * 
 * <p>
 * Implementa {@link CommandLineRunner} para que o método {@code run} seja
 * executado
 * automaticamente após a inicialização do contexto Spring.
 * </p>
 * 
 * <p>
 * A anotação {@link Configuration} define que esta classe é um componente de
 * configuração
 * do Spring.
 * </p>
 * 
 * <p>
 * O método {@code run} é marcado com {@link Transactional} para garantir que a
 * operação
 * de verificação e criação do usuário admin ocorra dentro de uma transação
 * atômica.
 * </p>
 * 
 * @author Emanuel
 */
@Configuration
public class AdminUserConfig implements CommandLineRunner {

  /**
   * Repositório para operações de persistência com a entidade {@link Role}.
   */
  private RoleRepository roleRepository;

  /**
   * Repositório para operações de persistência com a entidade {@link User}.
   */
  private UserRepository userRepository;

  /**
   * Encoder para hash seguro de senhas utilizando o algoritmo BCrypt.
   */
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * Construtor para injeção de dependências necessárias.
   *
   * @param bCryptPasswordEncoder o encoder de senha baseado em BCrypt.
   * @param roleRepository        o repositório para gerenciamento de perfis de
   *                              usuário.
   * @param userRepository        o repositório para gerenciamento de usuários.
   */
  public AdminUserConfig(BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository,
      UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  /**
   * Método executado automaticamente na inicialização da aplicação.
   * 
   * Responsável por verificar se o usuário "admin" já existe no banco de dados.
   * Caso não exista, cria um novo usuário com:
   * <ul>
   * <li>username: "admin"</li>
   * <li>password: "123" (criptografada com BCrypt)</li>
   * <li>role: ADMIN</li>
   * </ul>
   *
   * A operação é realizada dentro de uma transação, garantindo integridade e
   * atomicidade.
   *
   * @param args argumentos passados na linha de comando (não utilizados).
   * @throws Exception caso ocorra alguma falha na execução.
   */
  @Override
  @Transactional
  public void run(String... args) throws Exception {
    var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

    var userAdmin = userRepository.findByUsername("admin");

    userAdmin.ifPresentOrElse(
        user -> {
          // Caso o usuário admin já exista, apenas exibe uma mensagem.
          System.out.println("admin already exists");
        },
        () -> {
          // Caso o usuário admin não exista, cria um novo.
          var user = new User();
          user.setUsername("admin");
          user.setPassword(bCryptPasswordEncoder.encode("123"));
          user.setRoles(Set.of(roleAdmin));
          userRepository.save(user);
        });
  }
}
