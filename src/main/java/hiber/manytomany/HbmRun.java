package hiber.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            delete(session);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void delete(Session session) {
        Person person = session.get(Person.class, 4);
        session.remove(person);
    }

    private static void save(Session session) {
        Address one = Address.of("Kazanskaya", "1");
        Address two = Address.of("Piterskaya", "10");

        Person first = Person.of("Nikolay");
        first.getAddresses().add(one);
        first.getAddresses().add(two);

        Person second = Person.of("Anatoliy");
        second.getAddresses().add(two);

        session.persist(first);
        session.persist(second);
    }
}
