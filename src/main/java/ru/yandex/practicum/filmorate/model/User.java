package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен содержать символ @")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелов")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthday;
}

