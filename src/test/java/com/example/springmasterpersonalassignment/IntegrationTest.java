package com.example.springmasterpersonalassignment;

import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected UserRepository userRepository;

}
