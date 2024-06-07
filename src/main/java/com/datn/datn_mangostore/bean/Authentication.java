package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;

@Entity
@Table(name = "authentication")
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id")
    private Role role;

    public Authentication(Long id,
                          Account account,
                          Role role) {
        this.id = id;
        this.account = account;
        this.role = role;
    }

    public Authentication() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
