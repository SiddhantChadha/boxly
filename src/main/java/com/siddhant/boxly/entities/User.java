package com.siddhant.boxly.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String firstName;


    private String lastName;

    @Column(unique = true)
    private String email;


    private String password;

    private boolean isEnabled;

    @OneToMany(mappedBy = "createdBy",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<File> createdFiles;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_file_access_mapping",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<File> sharedFiles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
