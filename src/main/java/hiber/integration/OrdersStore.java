package hiber.integration;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrdersStore {

    private final BasicDataSource pool;

    public OrdersStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Order save(Order order) {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "INSERT INTO orders(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, order.getName());
            pr.setString(2, order.getDescription());
            pr.setTimestamp(3, order.getCreated());
            pr.execute();
            ResultSet id = pr.getGeneratedKeys();
            if (id.next()) {
                order.setId(id.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Collection<Order> findAll() {
        List<Order> rsl = new ArrayList<>();
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement("SELECT * FROM orders")) {
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    rsl.add(
                            createOrder(rs)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Order findById(int id) {
        Order rsl = null;
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                rsl = createOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Order findByName(String name) {
        Order rsl = null;
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement("SELECT  * FROM orders WHERE name =?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rsl = createOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public void update(Order order, int id) {
        try (var cn = pool.getConnection();
             var ps = cn.prepareStatement(
                     "UPDATE orders SET name = ?, description = ? WHERE id = ?")) {
            ps.setString(1, order.getName());
            ps.setString(2, order.getDescription());
            ps.setInt(3, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order createOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getTimestamp(4)
        );
    }
}
