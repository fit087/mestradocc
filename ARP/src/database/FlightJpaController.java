/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import database.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import main.entities.Flight;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class FlightJpaController {

    public FlightJpaController() {
        emf = Persistence.createEntityManagerFactory("ARPPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Flight flight) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(flight);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Flight flight) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            flight = em.merge(flight);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = flight.getId();
                if (findFlight(id) == null) {
                    throw new NonexistentEntityException("The flight with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Flight flight;
            try {
                flight = em.getReference(Flight.class, id);
                flight.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The flight with id " + id + " no longer exists.", enfe);
            }
            em.remove(flight);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Flight> findFlightEntities() {
        return findFlightEntities(true, -1, -1);
    }

    public List<Flight> findFlightEntities(int maxResults, int firstResult) {
        return findFlightEntities(false, maxResults, firstResult);
    }

    private List<Flight> findFlightEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Flight.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Flight findFlight(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Flight.class, id);
        } finally {
            em.close();
        }
    }

    public int getFlightCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Flight> rt = cq.from(Flight.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
