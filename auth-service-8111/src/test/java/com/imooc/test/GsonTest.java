package com.imooc.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@Slf4j
public class GsonTest {


    @Test
    public void objectToJson() {
        TestUsers testUsers = new TestUsers("1", "1235456", "nick", "real", 1, LocalDate.now());
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String json = gson.toJson(testUsers);
        log.info(json);
    }
}
