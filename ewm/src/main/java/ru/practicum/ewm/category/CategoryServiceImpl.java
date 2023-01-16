package ru.practicum.ewm.category;

import ru.practicum.ewm.exception.ExistsElementException;
import ru.practicum.ewm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        verifyCategoryByName(categoryDto.getName());

        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        getCategoryById(categoryDto.getId());
        verifyCategoryByName(categoryDto.getName());

        Category category = CategoryMapper.toCategory(categoryDto);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void removeCategory(Integer catId) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %s not found", catId)));
        if (!category.getEvents().isEmpty()) {
            throw new ExistsElementException(String.format("Category %s contains events and can't be removed", catId));
        } else {
            categoryRepository.deleteById(catId);
        }
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {

        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Integer catId) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with ID %s not found", catId)));
        return CategoryMapper.toCategoryDto(category);
    }

    private void verifyCategoryByName(String catName) {
        if (categoryRepository.countByName(catName) != 0) {
            throw new ExistsElementException("Category with this name already exist");
        }
    }
}
