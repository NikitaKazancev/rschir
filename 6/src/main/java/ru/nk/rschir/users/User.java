package ru.nk.rschir.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nk.rschir.types.EntityWithMerge;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, EntityWithMerge<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public void merge(User inputUser) {
        String field = inputUser.getFirstname();
        if (field != null) {
            this.setFirstname(field);
        }

        field = inputUser.getLastname();
        if (field != null) {
            this.setLastname(field);
        }

        field = inputUser.getPhone();
        if (field != null) {
            this.setPassword(field);
        }

        Date birthday = inputUser.getBirthday();
        if (birthday != null) {
            this.setBirthday(birthday);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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










