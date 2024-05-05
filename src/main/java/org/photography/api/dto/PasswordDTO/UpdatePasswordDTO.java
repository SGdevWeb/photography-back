package org.photography.api.dto.PasswordDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

}
