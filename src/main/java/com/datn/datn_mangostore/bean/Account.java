package com.datn.datn_mangostore.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String numberPhone;
    @Email(message = "Invalid email format")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Boolean gender;
    private String images;
    private String encryptionPassword;
    private String veryCode;
    private Integer accumulatedPoints;
    @ManyToOne
    @JoinColumn(name = "idRank")
    private Rank rank;
    @ManyToOne
    @JoinColumn(name = "idAddressClient")
    private AddressClient addressClient;
    @OneToMany(mappedBy = "account")
    List<Authentication> authentications;
    private Integer status;

    public Account(Long id,
                   String fullName,
                   String numberPhone,
                   String email,
                   LocalDate birthday,
                   Boolean gender,
                   String images,
                   String encryptionPassword,
                   String veryCode,
                   Integer accumulatedPoints,
                   Rank rank,
                   AddressClient addressClient,
                   List<Authentication> authentications,
                   Integer status) {
        this.id = id;
        this.fullName = fullName;
        this.numberPhone = numberPhone;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.images = images;
        this.encryptionPassword = encryptionPassword;
        this.veryCode = veryCode;
        this.accumulatedPoints = accumulatedPoints;
        this.rank = rank;
        this.addressClient = addressClient;
        this.authentications = authentications;
        this.status = status;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getEncryptionPassword() {
        return encryptionPassword;
    }

    public void setEncryptionPassword(String encryptionPassword) {
        this.encryptionPassword = encryptionPassword;
    }

    public String getVeryCode() {
        return veryCode;
    }

    public void setVeryCode(String veryCode) {
        this.veryCode = veryCode;
    }

    public Integer getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public void setAccumulatedPoints(Integer accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public AddressClient getAddressClient() {
        return addressClient;
    }

    public void setAddressClient(AddressClient addressClient) {
        this.addressClient = addressClient;
    }

    public List<Authentication> getAuthentications() {
        return authentications;
    }

    public void setAuthentications(List<Authentication> authentications) {
        this.authentications = authentications;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authentications.forEach(items -> authorities.add(new SimpleGrantedAuthority(items.getRole().getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public String getPassword() {
        return encryptionPassword;
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
        return true;
    }
}
