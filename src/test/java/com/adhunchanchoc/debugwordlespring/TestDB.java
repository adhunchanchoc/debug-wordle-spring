package com.adhunchanchoc.debugwordlespring;

import com.adhunchanchoc.debugwordlespring.db.DBEntry;
import com.adhunchanchoc.debugwordlespring.db.Database;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestDB {
    @Autowired
    private Database database;

    @Test
    public void testDatabase() {
        DBEntry entry = new DBEntry("User", "Guess", "Word", 3);
        database.insert(entry);
        List<DBEntry> entryList = database.selectAll("User");
        assertThat(entryList).size().isEqualTo(1);
        assertThat(entryList).element(0).isEqualTo(entry);
    }

    @Test
    public void testAttemtsNumberIncrement() {
        DBEntry entry = new DBEntry("OtherUser", "Guess", "Word", 0);
        database.insert(entry);
        DBEntry entry2 = new DBEntry("OtherUser", "Using", "Word", 0);
        database.insert(entry2);

        List<DBEntry> entryList = database.selectAll("OtherUser");
        assertThat(entryList).size().isEqualTo(2);

        assertThat(entryList).contains(entry, new DBEntry("OtherUser", "Using", "Word", 1));
    }
}
