package com.example.springjpa;

import com.example.springjpa.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"com.example.springjpa.bootstrap"})
public class MySQLIntegrationTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testMysql(){
        long countBefore = bookRepository.count();
        assertThat(countBefore).isEqualTo(2);
    }

}
