package category;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void removeCategory(Integer catId);

    CategoryDto getCategoryById(Integer catId);

    List<CategoryDto> getAllCategories(int from, int size);
}
