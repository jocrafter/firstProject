package de.micromata.jonas.project.models;

import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "user")
public class User implements UserDetails {
    @NonNull
    private String name;
    @NonNull
    private String password;
    @NonNull
    private Date birthday;
    @DBRef
    @Lazy
    private ArrayList<Like> likes;

    @Id
    private String id;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", bDay=" + birthday +
                ", id='" + id + '\'' +
                '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
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
