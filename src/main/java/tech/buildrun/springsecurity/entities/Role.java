package tech.buildrun.springsecurity.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleID;

  private String name;

  public void setRoleID(Long roleID) {
    this.roleID = roleID;
  }

  public Long getRoleID() {
    return roleID;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public enum Values {
    ADMIN(1L),
    BASIC(2L);

    long roleId;

    Values(long roleId) {
      this.roleId = roleId;
    }

    public long getRoleId() {
      return roleId;
    }
  }
}
