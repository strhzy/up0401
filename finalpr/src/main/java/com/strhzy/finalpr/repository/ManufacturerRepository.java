package com.strhzy.finalpr.repository;

import com.strhzy.finalpr.model.Manufacturer;
import com.strhzy.finalpr.service.HttpService;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class ManufacturerRepository {

    private static final String BASE_PATH = "/manufacturers";

    public List<Manufacturer> findAll() {
        try {
            return HttpService.get(BASE_PATH, List.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении списка производителей", e);
        }
    }

    public Optional<Manufacturer> findById(Long id) {
        try {
            Manufacturer manufacturer = HttpService.get(BASE_PATH + "/" + id, Manufacturer.class);
            return Optional.ofNullable(manufacturer);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при получении производителя с ID: " + id, e);
        }
    }

    public Manufacturer save(Manufacturer manufacturer) {
        try {
            if (manufacturer.getId() == null) {
                return HttpService.post(BASE_PATH, manufacturer, Manufacturer.class);
            } else {
                return HttpService.put(BASE_PATH + "/" + manufacturer.getId(), manufacturer, Manufacturer.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при сохранении производителя", e);
        }
    }

    public void deleteById(Long id) {
        try {
            HttpService.delete(BASE_PATH + "/" + id, Void.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при удалении производителя с ID: " + id, e);
        }
    }
}