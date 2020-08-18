package com.hydeze.hypad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.regex.Pattern;

@SpringBootTest
class HypadApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("file".substring(2));
    }

}
