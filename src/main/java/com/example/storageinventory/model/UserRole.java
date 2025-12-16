package com.example.storageinventory.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {

    @Id
    // Сменяме IDENTITY със SEQUENCE
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_gen")
    @SequenceGenerator(name = "role_seq_gen", sequenceName = "ROLE_SEQ", allocationSize = 1)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    // Връзка: Една роля може да я имат много потребители
    // Това не е задължително, но помага ако искаш да видиш всички админи
    @OneToMany(mappedBy = "role")
    private Set<User> users;

    // Конструктори
    public UserRole() {} // Задължителен за Hibernate

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    // toString (за дебъгване)
    @Override
    public String toString() {
        return roleName; // Това е важно за JavaFX ComboBox-овете по-късно!
    }
}