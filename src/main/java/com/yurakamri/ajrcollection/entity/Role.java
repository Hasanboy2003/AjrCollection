package com.yurakamri.ajrcollection.entity;

import com.yurakamri.ajrcollection.entity.abs.AbsLongEntity;
import com.yurakamri.ajrcollection.entity.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role extends AbsLongEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, updatable = false)
    RoleEnum roleName;

    @Column(columnDefinition = "TEXT")
    String description;

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }
}
