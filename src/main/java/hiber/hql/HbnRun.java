package hiber.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;

public class HbnRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void insert(Session session) {
        session.createQuery("insert into Candidate (name, experience, salary) "
                        + "select concat(c.name, ' Dm'), c.experience + 5, c.salary + 400000  "
                        + "from Candidate c where c.id = :fId")
                .setParameter("fId", 1)
                .executeUpdate();
    }

    private static void delete(Session session) {
        session.createQuery("delete from Candidate c where c.id = :fId")
                .setParameter("fId", 4)
                .executeUpdate();
    }

    private static void update(Session session) {
        session.createQuery(
                        "update Candidate c  set c.salary = :salary, c.name = :name where c.id = :fId")
                .setParameter("fId", 1)
                .setParameter("name", "Java")
                .setParameter("salary", 111_111)
                .executeUpdate();
    }

    private static void selectByName(Session session) {
        Query query = session.createQuery("from Candidate c where c.name = :name");
        query.setParameter("name", "Time");
        System.out.println(query.getSingleResult());
    }

    private static void selectById(Session session) {
        Query query = session.createQuery("from Candidate c where c.id= :fId");
        query.setParameter("fId", 1);
        System.out.println(query.getSingleResult());
    }

    private static void selectAll(Session session) {
        Query query = session.createQuery("from Candidate");
        for (Object ct : query.getResultList()) {
            System.out.println(ct);
        }
    }

    private static void created(Session session) {
        Candidate one = Candidate.of("Dmitry", 0, 100_000);
        Candidate two = Candidate.of("AfterTime", 1, 200_000);
        Candidate three = Candidate.of("MoreTime", 2, 300_000);
        Candidate four = Candidate.of("Time", 10, 1_000_000);

        session.save(one);
        session.save(two);
        session.save(three);
        session.save(four);
    }
}
