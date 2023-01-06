package user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Jacksonized
public class UserInfoDto {

    private Long id;
    @NotBlank
    @Size(max = 256)
    private String name;
}
