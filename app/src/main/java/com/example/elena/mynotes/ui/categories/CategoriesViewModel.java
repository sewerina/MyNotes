package com.example.elena.mynotes.ui.categories;

import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoriesViewModel extends ViewModel {
    private final MyNotesDao mMyNotesDao;

    public MutableLiveData<List<CategoryEntity>> categories = new MutableLiveData<>();

    public CategoriesViewModel() {
        mMyNotesDao = MyNotesApp.getDatabase().myNotesDao();
        loadCategories();
    }

    public void createCategory(String name, String icon) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.name = name;
        categoryEntity.imageName = icon;
        mMyNotesDao.createCategory(categoryEntity);
        loadCategories();
    }

    public void loadCategories() {
        categories.setValue(mMyNotesDao.getAllCategories());
    }
}
