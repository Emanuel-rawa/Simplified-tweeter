package tech.buildrun.springsecurity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.buildrun.springsecurity.entities.User;

/**
 * Repositório Spring Data JPA para a entidade {@link User}.
 * 
 * Fornece métodos padrão para operações CRUD e query por UUID.
 * 
 * Inclui método personalizado para busca de usuário por nome de usuário
 * (username).
 * 
 * @author Emanuel
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Busca um usuário pelo seu nome de usuário (username).
   * 
   * @param username o nome de usuário a ser buscado.
   * @return um {@link Optional} contendo o usuário caso exista, ou vazio caso
   *         contrário.
   */
  Optional<User> findByUsername(String username);
}
