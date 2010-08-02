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
import statistic.HeuristicInfo;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class HeuristicInfoJpaController {

    public HeuristicInfoJpaController() {
        emf = Persistence.createEntityManagerFactory("ARPPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HeuristicInfo heuristicInfo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(heuristicInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HeuristicInfo heuristicInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            heuristicInfo = em.merge(heuristicInfo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = heuristicInfo.getId();
                if (findHeuristicInfo(id) == null) {
                    throw new NonexistentEntityException("The heuristicInfo with id " + id + " no longer exists.");
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
            HeuristicInfo heuristicInfo;
            try {
                heuristicInfo = em.getReference(HeuristicInfo.class, id);
                heuristicInfo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The heuristicInfo with id " + id + " no longer exists.", enfe);
            }
            em.remove(heuristicInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HeuristicInfo> findHeuristicInfoEntities() {
        return findHeuristicInfoEntities(true, -1, -1);
    }

    public List<HeuristicInfo> findHeuristicInfoEntities(int maxResults, int firstResult) {
        return findHeuristicInfoEntities(false, maxResults, firstResult);
    }

    private List<HeuristicInfo> findHeuristicInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HeuristicInfo.class));
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

    public HeuristicInfo findHeuristicInfo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HeuristicInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeuristicInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HeuristicInfo> rt = cq.from(HeuristicInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
