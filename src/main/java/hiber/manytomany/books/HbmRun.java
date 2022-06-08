package hiber.manytomany.books;

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

            save(session);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void delete(Session session) {
        Author author = session.get(Author.class, 2);
        session.remove(author);
    }

    private static void save(Session session) {
        Book one = Book.of("asdf", 1);
        Book two = Book.of("jkl;", 2);
        Book three = Book.of("qwert", 3);
        Book four = Book.of("yuiop[]", 4);

        Author first = Author.of("Huxley");
        first.getBooks().add(one);
        first.getBooks().add(two);
        Author second = Author.of("Bradbury");
        second.getBooks().add(three);
        second.getBooks().add(four);
        Author third = Author.of("Herzog");
        third.getBooks().add(one);
        third.getBooks().add(two);
        third.getBooks().add(three);
        third.getBooks().add(four);

        session.persist(first);
        session.persist(second);
        session.persist(third);
    }
}
