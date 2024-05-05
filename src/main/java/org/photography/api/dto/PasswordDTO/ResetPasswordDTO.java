package org.photography.api.dto.PasswordDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {

    private String email;

    private String password;

    private String confirmPassword;

    private String token;

}
