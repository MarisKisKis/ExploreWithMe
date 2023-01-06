package compilation;


import compilation.dto.CompilationDto;
import compilation.dto.CompilationInfoDto;
import compilation.dto.CompilationMapper;
import event.EventRepository;
import event.model.Event;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        if (Objects.isNull(pinned)) {
            compilationRepository.findAll(pageable);
        }

        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper::toCompilDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto addNewCompilation(CompilationInfoDto compilationInfoDto) {

        Set<Event> events = eventRepository.findAllByIdIn(compilationInfoDto.getEvents());
        Compilation newCompilation = Compilation.builder()
                .events(events)
                .pinned(compilationInfoDto.getPinned())
                .title(compilationInfoDto.getTitle())
                .build();
        compilationRepository.save(newCompilation);

        return CompilationMapper.toCompilDto(newCompilation);
    }

    @Override
    public void removeCompilationById(Integer compId) {

        getCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void removeEventFromCompilation(Integer compId, Long eventId) {

        Compilation compilation = getCompilationById(compId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with ID %s was not found", eventId)));

        Set<Event> events = compilation.getEvents();
        events.remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto addEventInCompilation(Integer compId, Long eventId) {

        Compilation compilation = getCompilationById(compId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with ID %s was not found", eventId)));

        Set<Event> events = compilation.getEvents();
        events.add(event);
        return CompilationMapper.toCompilDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto pinnedOutCompilation(Integer compId) {

        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);

        return CompilationMapper.toCompilDto(compilation);
    }

    @Override
    public CompilationDto pinnedCompilation(Integer compId) {

        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);

        return CompilationMapper.toCompilDto(compilation);
    }

    @Override
    public CompilationDto getDtoCompilationById(Integer compId) {

        return CompilationMapper.toCompilDto(compilationRepository
                .findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Compilation with ID %s was not found", compId))));
    }

    private Compilation getCompilationById(Integer compId) {

        return compilationRepository
                .findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Compilation with ID %s was not found", compId)));
    }
}
