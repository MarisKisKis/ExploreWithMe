package ru.practicum.ewm.compilation.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class CompilationInfoDto {

    private Boolean pinned;
    @NotBlank
    private String title;
    private Set<Long> events;

}
