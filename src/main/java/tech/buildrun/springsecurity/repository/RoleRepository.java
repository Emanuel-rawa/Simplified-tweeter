package tech.buildrun.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.buildrun.springsecurity.entities.Role;

/**
 * Repositório Spring Data JPA para a entidade {@link Role}.
 * 
 * Fornece operações padrão de CRUD para gerenciamento de roles
 * (papéis/autoridades) do sistema,
 * com identificador do tipo {@link Long}.
 * 
 * Inclui método customizado para buscar uma role pelo seu nome.
 * 
 * @author Emanuel
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * Busca uma role pelo seu nome.
   * 
   * @param name o nome da role a ser buscada.
   * @return a instância da {@link Role} correspondente ao nome informado, ou
   *         {@code null} se não existir.
   */
  Role findByName(String name);
}
