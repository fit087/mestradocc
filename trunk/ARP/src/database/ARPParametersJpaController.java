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
import main.heuristic.ARPParameters;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPParametersJpaController {

    public ARPParametersJpaController() {
        emf = Persistence.createEntityManagerFactory("ARPPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ARPParameters ARPParameters) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ARPParameters);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ARPParameters ARPParameters) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ARPParameters = em.merge(ARPParameters);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ARPParameters.getId();
                if (findARPParameters(id) == null) {
                    throw new NonexistentEntityException("The aRPParameters with id " + id + " no longer exists.");
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
            ARPParameters ARPParameters;
            try {
                ARPParameters = em.getReference(ARPParameters.class, id);
                ARPParameters.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ARPParameters with id " + id + " no longer exists.", enfe);
            }
            em.remove(ARPParameters);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ARPParameters> findARPParametersEntities() {
        return findARPParametersEntities(true, -1, -1);
    }

    public List<ARPParameters> findARPParametersEntities(int maxResults, int firstResult) {
        return findARPParametersEntities(false, maxResults, firstResult);
    }

    private List<ARPParameters> findARPParametersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ARPParameters.class));
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

    public ARPParameters findARPParameters(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ARPParameters.class, id);
        } finally {
            em.close();
        }
    }

    public int getARPParametersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ARPParameters> rt = cq.from(ARPParameters.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
