package com.example.rediscache;

import com.example.rediscache.entity.User;
import com.example.rediscache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class RedisCacheApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Populating embedded database here
        LOG.info("Saving users. Current user count is {}.", userRepository.count());
        User shubham = new User("User1", 2000);
        User pankaj = new User("User2", 29000);
        User lewis = new User("User3", 550);

        userRepository.save(shubham);
        userRepository.save(pankaj);
        userRepository.save(lewis);
        LOG.info("Done saving users. Data: {}.", userRepository.findAll());
    }
}
