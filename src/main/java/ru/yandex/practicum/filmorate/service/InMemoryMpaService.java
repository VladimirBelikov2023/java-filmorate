package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnknownException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class InMemoryMpaService implements MpaService {
    MpaStorage mpaStorage;

    @Override
    public List<Mpa> getLsMpa() {
        return mpaStorage.getLsMpa();
    }

    @Override
    public Mpa getMpa(int id) {
        try {
            return mpaStorage.getMpa(id);
        } catch (Exception e) {
            throw new UnknownException("Wrong id");
        }
    }
}
