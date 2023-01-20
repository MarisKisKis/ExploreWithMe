package ru.practicum.ewm.compilation.controller;

import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    @Autowired
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addNewCompilation(@Valid @RequestBody CompilationInfoDto compilationInfoDto) {

        log.info("Add new ru.practicum.compilation {}", compilationInfoDto);
        return compilationService.addNewCompilation(compilationInfoDto);
    }

    @DeleteMapping("/{compId}")
    public void removeCompilationById(@PathVariable @NotNull Integer compId) {

        log.info("Remove ru.practicum.compilation with ID = {}", compId);
        compilationService.removeCompilationById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable @NotNull Integer compId,
                                           @PathVariable @NotNull Long eventId) {

        log.info("Remove ru.practicum.event with ID = {} from ru.practicum.compilation with ID = {}", eventId, compId);
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEventInCompilation(@PathVariable @NotNull Integer compId,
                                                @PathVariable @NotNull Long eventId) {

        log.info("Add ru.practicum.event with ID = {} in ru.practicum.compilation with ID = {}", eventId, compId);
        return compilationService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public CompilationDto pinnedOutCompilation(@PathVariable @NotNull Integer compId) {

        log.info("Pinned out Compilation with ID = {}", compId);
        return compilationService.pinnedOutCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public CompilationDto pinnedCompilation(@PathVariable @NotNull Integer compId) {

        log.info("Pinned Compilation with ID = {}", compId);
        return compilationService.pinnedCompilation(compId);
    }
}
