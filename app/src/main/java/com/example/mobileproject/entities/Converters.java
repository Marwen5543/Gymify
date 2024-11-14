package com.example.mobileproject.entities;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List<String> imageUrls) {
        if (imageUrls == null) {
            return null;
        }
        return String.join(",", imageUrls); // Converts the list to a comma-separated string
    }

    @TypeConverter
    public List<String> toList(String data) {
        if (data == null) {
            return null;
        }
        return Arrays.asList(data.split(",")); // Converts a comma-separated string back to a list
    }
    @TypeConverter
    public static String fromCategory(Category category) {
        return category == null ? null : category.name();
    }

    @TypeConverter
    public static Category toCategory(String category) {
        return category == null ? null : Category.valueOf(category);
    }
}

