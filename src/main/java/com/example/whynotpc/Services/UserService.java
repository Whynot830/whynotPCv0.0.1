package com.example.whynotpc.Services;

import com.example.whynotpc.Models.User;
import com.example.whynotpc.Repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User create(User user) {
        return userRepo.save(user);
    }

    public List<User> readAll() {
        return userRepo.findAll();
    }

    public Optional<User> readOne(Integer id) {
        return userRepo.findById(id);
    }

    public boolean update(User newEntity, Integer id) {
        Optional<User> optionalEntity = userRepo.findById(id);
        if (optionalEntity.isEmpty())
            return false;
        newEntity.setId(id);
        userRepo.save(newEntity);
        return true;
    }

    public boolean delete(Integer id) {
        Optional<User> optionalEntity = userRepo.findById(id);
        if (optionalEntity.isEmpty())
            return false;
        userRepo.delete(optionalEntity.get());
        return true;
    }
    public Optional<User> findByUsername(String email) {
        return userRepo.findByUsername(email);
    }
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
