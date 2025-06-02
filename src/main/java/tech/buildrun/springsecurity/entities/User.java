package tech.buildrun.springsecurity.entities;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.*;
import tech.buildrun.springsecurity.controller.dto.LoginRequest;

/**
 * Entidade JPA que representa um usuário do sistema.
 * 
 * Mapeada para a tabela "tb_users" no banco de dados.
 * 
 * Contém informações de autenticação, identificação única e os papéis (roles)
 * associados ao usuário.
 * 
 * @author Emanuel
 */
@Entity
@Table(name = "tb_users")
public class User {

  /**
   * Identificador único do usuário, gerado automaticamente como UUID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_id")
  private UUID userId;

  /**
   * Nome de usuário único para login.
   */
  @Column(unique = true)
  private String username;

  /**
   * Senha codificada (hash) do usuário para autenticação.
   */
  private String password;

  /**
   * Conjunto de roles (permissões) associados ao usuário.
   * 
   * Relacionamento Many-to-Many com a entidade {@link Role}, carregado eager.
   */
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "tb_users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  /**
   * Retorna o UUID único do usuário.
   * 
   * @return identificador único do usuário.
   */
  public UUID getUserId() {
    return userId;
  }

  /**
   * Define o UUID único do usuário.
   * 
   * @param userId identificador único para o usuário.
   */
  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  /**
   * Retorna o nome de usuário para login.
   * 
   * @return nome de usuário único.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Define o nome de usuário para login.
   * 
   * @param username nome de usuário único.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Retorna a senha codificada (hash) do usuário.
   * 
   * @return senha codificada.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Define a senha codificada (hash) do usuário.
   * 
   * @param password senha codificada para autenticação.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Retorna o conjunto de roles (permissões) do usuário.
   * 
   * @return conjunto de roles associados ao usuário.
   */
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * Define o conjunto de roles (permissões) do usuário.
   * 
   * @param roles conjunto de roles para associar ao usuário.
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  /**
   * Verifica se as credenciais de login fornecidas são válidas para este usuário.
   * 
   * Compara a senha fornecida no {@link LoginRequest} com a senha codificada
   * armazenada,
   * usando o {@link PasswordEncoder} para validação.
   * 
   * @param loginRequest    objeto contendo o nome de usuário e senha para login.
   * @param passwordEncoder encoder usado para verificar a senha codificada.
   * @return true se a senha fornecida corresponder à senha codificada; false caso
   *         contrário.
   */
  public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(loginRequest.password(), this.password);
  }
}
