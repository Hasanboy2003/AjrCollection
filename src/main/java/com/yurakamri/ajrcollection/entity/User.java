package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsUUIDEntity;
import com.yurakamri.ajrcollection.entity.enums.Lang;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User extends AbsUUIDEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    String phoneNumber;

    @Column(nullable = false)
    String firstName;

    String lastName;

    String code;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    Attachment attachment;

    //ROLE ga lazy qoysa workerda xato beryapti
    @ManyToOne
    @JoinColumn(nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    Lang language = Lang.UZ;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    )
    List<Product> favourites;

    @OneToMany(mappedBy = "user")
    List<UserSearch> userSearches;

    boolean accountNonExpired = true;
    boolean accountNonLocked = true;
    boolean credentialsNonExpired = true;
    boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.role);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    public User(String phoneNumber, String firstName, String lastName, String code, Role role) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
        this.role = role;
    }
}
