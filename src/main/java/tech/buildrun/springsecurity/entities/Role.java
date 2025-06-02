package tech.buildrun.springsecurity.entities;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa uma Role (perfil/permissão) do sistema.
 * 
 * Mapeada para a tabela "tb_roles" no banco de dados.
 * 
 * Cada Role possui um identificador único e um nome que representa o tipo da
 * role.
 * 
 * Define também um enum interno {@link Values} para representar roles padrão
 * com seus respectivos IDs.
 * 
 * @author Emanuel
 */
@Entity
@Table(name = "tb_roles")
public class Role {

  /**
   * Identificador único da Role, gerado automaticamente pelo banco.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleID;

  /**
   * Nome da Role, usado para identificar a permissão ou perfil.
   */
  private String name;

  /**
   * Define o ID da Role.
   * 
   * @param roleID identificador único da Role.
   */
  public void setRoleID(Long roleID) {
    this.roleID = roleID;
  }

  /**
   * Retorna o ID da Role.
   * 
   * @return identificador único da Role.
   */
  public Long getRoleID() {
    return roleID;
  }

  /**
   * Define o nome da Role.
   * 
   * @param name nome da Role.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retorna o nome da Role.
   * 
   * @return nome da Role.
   */
  public String getName() {
    return name;
  }

  /**
   * Enum interno que define valores padrão para as Roles do sistema.
   * 
   * Cada valor representa uma Role comum com seu respectivo ID fixo.
   */
  public enum Values {
    /**
     * Role de administrador, com privilégios elevados.
     */
    ADMIN(1L),

    /**
     * Role básica, com permissões padrão.
     */
    BASIC(2L);

    /**
     * Identificador fixo da Role no enum.
     */
    long roleId;

    /**
     * Construtor do enum que associa o ID fixo a cada valor.
     * 
     * @param roleId identificador da Role.
     */
    Values(long roleId) {
      this.roleId = roleId;
    }

    /**
     * Retorna o identificador associado à Role.
     * 
     * @return ID da Role.
     */
    public long getRoleId() {
      return roleId;
    }
  }
}
