package collector.database.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import collector.database.model.Metric;

public final class DatabaseUtil {
	private static final EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("collector.database.jpa");

	public static void save(Metric metric) {

		EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		// From Hibernate doc: Note that even if your datastore does not support
		// transactions, we recommend you use transaction demarcations with
		// Hibernate OGM to trigger the flush operation transparently (on
		// commit).
		// But do not consider rollback as a possibility, this won’t work.
		
		// will fail if no transaction
		entityManager.getTransaction().begin();
		entityManager.persist(metric);
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
	}

}
