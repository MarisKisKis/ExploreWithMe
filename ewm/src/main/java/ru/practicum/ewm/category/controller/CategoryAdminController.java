package ru.practicum.ewm.category.controller;

import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.category.CategoryService;
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
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    @Autowired
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Create ru.practicum.category = {}", categoryDto);
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Update ru.practicum.category {}", categoryDto);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping(value = "/{catId}")
    public void removeCategory(@PathVariable @NotNull Integer catId) {
        log.info("Remove ru.practicum.category with ID = {}", catId);
        categoryService.removeCategory(catId);
    }
}
