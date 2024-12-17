package com.example.demo.Services;

import java.util.List;

public interface GenericService {

   List<?> getAll();
   Object convertToDTO(Object object);
   Object save(Object object);
   Object convertToEntity(Object object);
   Object delete(Long id);
}
