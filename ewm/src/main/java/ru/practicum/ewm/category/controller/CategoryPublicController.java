package ru.practicum.ewm.category.controller;


import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryPublicController {

    @Autowired
    private final CategoryService categoryService;

    @GetMapping(value = "/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Get ru.practicum.category with ID = {}", catId);
        return categoryService.getCategoryById(catId);
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10")
                                                   Integer size) {
        log.info("Get all categories");
        return categoryService.getAllCategories(from, size);
    }
}
