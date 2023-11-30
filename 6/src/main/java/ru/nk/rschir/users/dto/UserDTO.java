package ru.nk.rschir.users.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.components.carts.CartEntity;
import ru.nk.rschir.components.carts.dto.CartFullData;
import ru.nk.rschir.users.Role;
import ru.nk.rschir.users.User;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private Date birthday;
    @Enumerated(EnumType.STRING)
    private Role role;
    private CartFullData cart;
    public static UserDTO create(User user, CartFullData cart) {
        UserDTO newUser = create(user);
        newUser.setCart(cart);
        return newUser;
    };
    public static UserDTO create(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .phone(user.getPhone())
            .birthday(user.getBirthday())
            .build();
    }
}
