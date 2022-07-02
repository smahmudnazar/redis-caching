package com.example.rediscache.controller;


import com.example.rediscache.entity.User;
import com.example.rediscache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final CacheManager cacheManager;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Cacheable(value = "random", key = "#userId")
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        //in memory cache like database
        Cache cache = cacheManager.getCache("random");
        cache.put("userId", userId);
        System.out.println("Come !");
        Cache.ValueWrapper wrapper = cache.get("userId");
        System.out.println(Objects.requireNonNull(Objects.requireNonNull(wrapper).get()).toString());
        System.out.println("Come !");
        LOG.info("GETTING USER ID {}.", userId);
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    //cacheable
    @GetMapping("/all")
    public List<User> getAll() {
        LOG.info("GET ALL {} ");
        return userRepository.findAll();
    }

    @CachePut(value = "random", key = "#id")
    @PutMapping("/{id}")
    public User edit(@PathVariable Long id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return new User();

        User edited = optionalUser.get();
        edited.setName(user.getName());
        edited.setFollowers(user.getFollowers());

        User save = userRepository.save(edited);
        return save;
    }

    @CacheEvict(value = "random", allEntries = true)
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return new HttpEntity("User not found!");

        userRepository.deleteById(id);
        return new HttpEntity("User deleted!");
    }
}
