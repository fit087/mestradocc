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
import main.heuristic.GRASPParameters;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class GRASPParametersJpaController {

    public GRASPParametersJpaController() {
        emf = Persistence.createEntityManagerFactory("ARPPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GRASPParameters GRASPParameters) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(GRASPParameters);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GRASPParameters GRASPParameters) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GRASPParameters = em.merge(GRASPParameters);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = GRASPParameters.getId();
                if (findGRASPParameters(id) == null) {
                    throw new NonexistentEntityException("The gRASPParameters with id " + id + " no longer exists.");
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
            GRASPParameters GRASPParameters;
            try {
                GRASPParameters = em.getReference(GRASPParameters.class, id);
                GRASPParameters.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The GRASPParameters with id " + id + " no longer exists.", enfe);
            }
            em.remove(GRASPParameters);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GRASPParameters> findGRASPParametersEntities() {
        return findGRASPParametersEntities(true, -1, -1);
    }

    public List<GRASPParameters> findGRASPParametersEntities(int maxResults, int firstResult) {
        return findGRASPParametersEntities(false, maxResults, firstResult);
    }

    private List<GRASPParameters> findGRASPParametersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GRASPParameters.class));
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

    public GRASPParameters findGRASPParameters(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GRASPParameters.class, id);
        } finally {
            em.close();
        }
    }

    public int getGRASPParametersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GRASPParameters> rt = cq.from(GRASPParameters.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
