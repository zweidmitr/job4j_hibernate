package hiber.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private static BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/scripts/update.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void dropTable() throws SQLException {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("drop table orders")) {
            ps.execute();
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        pool.close();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindIdAndFindByName() {
        OrdersStore store = new OrdersStore(pool);
        var ordOne = Order.of("toNight", "weStudy");
        store.save(ordOne);
        var ordTwo = Order.of("toMorrow", "weStudy");
        store.save(ordTwo);
        var ordThree = Order.of("everyDay", "weStudy");
        store.save(ordThree);

        assertThat(store.findById(1), is(ordOne));
        assertThat(store.findByName("everyDay"), is(ordThree));
    }

    @Test
    public void whenSaveOrderAndFindByNameAndUpdate() {
        OrdersStore store = new OrdersStore(pool);
        var ordOne = Order.of("toNight", "weStudy");
        store.save(ordOne);
        var newOrd = Order.of("everyDay", "weStudy");
        store.update(newOrd, ordOne.getId());

        assertThat(store.findByName("everyDay"), is(ordOne));
    }
}