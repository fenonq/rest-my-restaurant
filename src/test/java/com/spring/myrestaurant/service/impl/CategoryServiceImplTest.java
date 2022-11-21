package com.spring.myrestaurant.service.impl;

import com.spring.myrestaurant.dto.CategoryDto;
import com.spring.myrestaurant.exception.EntityNotFoundException;
import com.spring.myrestaurant.model.Category;
import com.spring.myrestaurant.repository.CategoryRepository;
import com.spring.myrestaurant.test.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void findAllTest() {
        List<Category> categories = List.of(
                TestDataUtil.createCategory(),
                TestDataUtil.createCategory()
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDto> returnedCategories = categoryService.findAll();

        verify(categoryRepository).findAll();
        assertThat(returnedCategories, hasSize(categories.size()));
    }

    @Test
    void findByIdTest() {
        Category category = TestDataUtil.createCategory();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryDto returnedCategory = categoryService.findById(anyLong());
        verify(categoryRepository).findById(anyLong());
        assertThat(returnedCategory, allOf(
                hasProperty("id", equalTo(category.getId())),
                hasProperty("name", equalTo(category.getName()))
        ));
    }

    @Test
    void findByIdNotFoundTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(anyLong()));
    }

    @Test
    void saveTest() {
        Category category = TestDataUtil.createCategory();
        CategoryDto categoryDto = TestDataUtil.createCategoryDto();
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryDto returnedCategory = categoryService.save(categoryDto);

        assertThat(returnedCategory, allOf(
                hasProperty("id", equalTo(categoryDto.getId())),
                hasProperty("name", equalTo(categoryDto.getName()))
        ));
    }

    @Test
    void updateTest() {
        Category category = TestDataUtil.createCategory();
        CategoryDto categoryDto = TestDataUtil.createCategoryDto();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);

        CategoryDto returnedCategory = categoryService.update(category.getId(), categoryDto);

        assertThat(returnedCategory, allOf(
                hasProperty("id", equalTo(categoryDto.getId())),
                hasProperty("name", equalTo(categoryDto.getName()))
        ));
    }

    @Test
    void updateNotFoundTest() {
        CategoryDto categoryDto = TestDataUtil.createCategoryDto();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryDto.getId(), categoryDto));
    }

    @Test
    void deleteByIdTest() {
        Category category = TestDataUtil.createCategory();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteById(anyLong());

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteByIdNotFoundTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(anyLong()));
    }

    @Test
    void changeVisibilityTest() {
        Category category = TestDataUtil.createCategory();
        category.getDishes().add(TestDataUtil.createDish());
        boolean currentVisibility = category.isVisible();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryDto returnedCategory;
        for (int i = 0; i < 3; i++) {
            returnedCategory = categoryService.changeVisibility(category.getId());
            currentVisibility = !currentVisibility;
            assertThat(returnedCategory, hasProperty("visible", is(currentVisibility)));
        }
    }

    @Test
    void changeVisibilityNotFoundTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.changeVisibility(anyLong()));
    }

}
