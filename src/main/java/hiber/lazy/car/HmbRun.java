package hiber.lazy.car;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HmbRun {
    public static void main(String[] args) {
        List<Car> cars = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            cars = session.createQuery("from  Car").list();
            for (Car car : cars) {
                for (Model model : car.getModels()) {
                    System.out.println(model);
                }
            }

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void save(Session session) {
        Car vw = Car.of("Volkswagen");
        session.save(vw);

        List<Model> models = new ArrayList<>();
        models.add(Model.of("Polo", vw));
        models.add(Model.of("Passat", vw));
        models.add(Model.of("Golf", vw));
        models.add(Model.of("Transporter", vw));
        models.add(Model.of("Multivan", vw));
        models.forEach(session::save);
    }
}
