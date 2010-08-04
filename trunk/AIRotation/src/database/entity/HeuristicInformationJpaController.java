/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entity;

import database.entity.exceptions.NonexistentEntityException;
import database.entity.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class HeuristicInformationJpaController {

    private static HeuristicInformationJpaController instance = new HeuristicInformationJpaController();

    public static HeuristicInformationJpaController getInstance(){
        return instance;
    }

    private HeuristicInformationJpaController() {
        emf = Persistence.createEntityManagerFactory("airotationpu");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HeuristicInformation heuristicInformation) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(heuristicInformation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHeuristicInformation(heuristicInformation.getSeed()) != null) {
                throw new PreexistingEntityException("HeuristicInformation " + heuristicInformation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HeuristicInformation heuristicInformation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            heuristicInformation = em.merge(heuristicInformation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = heuristicInformation.getSeed();
                if (findHeuristicInformation(id) == null) {
                    throw new NonexistentEntityException("The heuristicInformation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HeuristicInformation heuristicInformation;
            try {
                heuristicInformation = em.getReference(HeuristicInformation.class, id);
                heuristicInformation.getSeed();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The heuristicInformation with id " + id + " no longer exists.", enfe);
            }
            em.remove(heuristicInformation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HeuristicInformation> findHeuristicInformationEntities() {
        return findHeuristicInformationEntities(true, -1, -1);
    }

    public List<HeuristicInformation> findHeuristicInformationEntities(int maxResults, int firstResult) {
        return findHeuristicInformationEntities(false, maxResults, firstResult);
    }

    private List<HeuristicInformation> findHeuristicInformationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from HeuristicInformation as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public HeuristicInformation findHeuristicInformation(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HeuristicInformation.class, id);
        } finally {
            em.close();
        }
    }

    public int getHeuristicInformationCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from HeuristicInformation as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
