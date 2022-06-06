package hiber.many.car;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HmbRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Model> models = new ArrayList<>();
            models.add(Model.of("Polo"));
            models.add(Model.of("Passat"));
            models.add(Model.of("Golf"));
            models.add(Model.of("Transporter"));
            models.add(Model.of("Multivan"));
            models.forEach(session::save);

            Car vw = Car.of("Volkswagen");
            for (int i = 1; i <= models.size(); i++) {
                vw.addModel(session.load(Model.class, i));
            }

            session.save(vw);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
