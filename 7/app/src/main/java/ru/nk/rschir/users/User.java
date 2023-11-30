package ru.nk.rschir.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class User implements EntityWithMerge<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
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

        Date birthday = inputUser.getBirthday();
        if (birthday != null) {
            this.setBirthday(birthday);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday=" + birthday +
                ", role=" + role +
                '}';
    }
}










