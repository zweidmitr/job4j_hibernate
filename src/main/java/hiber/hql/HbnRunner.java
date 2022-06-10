package hiber.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbnRunner {
    public static void main(String[] args) {
        Candidate result = null;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            printResult(session);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void printResult(Session session) {
        Candidate result;
        result = session.createQuery(
                "select distinct cn from Candidate  cn "
                        + "join fetch cn.vacancyDB b "
                        + "join fetch b.vacancies v "
                        + "where cn.id = :cId", Candidate.class
        ).setParameter("cId", 1).uniqueResult();
        System.out.println(result);
    }

    private static void save(Session session) {

        Vacancy first = Vacancy.of("best");
        Vacancy second = Vacancy.of("theBest");
        session.save(first);
        session.save(second);

        VacancyDB vacancy = VacancyDB.of("sber");
        VacancyDB vacancyT = VacancyDB.of("sberT");
        vacancy.addVacancy(first);
        vacancyT.addVacancy(second);
        session.save(vacancy);
        session.save(vacancyT);

        Candidate one = Candidate.of("Dmitry", 0, 100_000);
        Candidate two = Candidate.of("AfterTime", 1, 200_000);
        Candidate three = Candidate.of("MoreTime", 2, 300_000);
        Candidate four = Candidate.of("Time", 10, 1_000_000);
        one.setVacancyDB(vacancy);
        two.setVacancyDB(vacancy);
        three.setVacancyDB(vacancyT);
        four.setVacancyDB(vacancyT);

        session.save(one);
        session.save(two);
        session.save(three);
        session.save(four);
    }
}
