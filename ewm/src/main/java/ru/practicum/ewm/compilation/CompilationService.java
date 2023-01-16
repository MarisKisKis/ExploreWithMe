package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationInfoDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getDtoCompilationById(Integer compId);

    CompilationDto addNewCompilation(CompilationInfoDto CompilationInfoDto);

    void removeCompilationById(Integer compId);

    void removeEventFromCompilation(Integer compId, Long eventId);

    CompilationDto addEventInCompilation(Integer compId, Long eventId);

    CompilationDto pinnedOutCompilation(Integer compId);

    CompilationDto pinnedCompilation(Integer compId);
}
