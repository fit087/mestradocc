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
import main.entities.Teste;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class TesteJpaController {

    public TesteJpaController() {
        emf = Persistence.createEntityManagerFactory("ARPPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Teste teste) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(teste);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Teste teste) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            teste = em.merge(teste);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = teste.getId();
                if (findTeste(id) == null) {
                    throw new NonexistentEntityException("The teste with id " + id + " no longer exists.");
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
            Teste teste;
            try {
                teste = em.getReference(Teste.class, id);
                teste.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The teste with id " + id + " no longer exists.", enfe);
            }
            em.remove(teste);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Teste> findTesteEntities() {
        return findTesteEntities(true, -1, -1);
    }

    public List<Teste> findTesteEntities(int maxResults, int firstResult) {
        return findTesteEntities(false, maxResults, firstResult);
    }

    private List<Teste> findTesteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Teste.class));
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

    public Teste findTeste(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Teste.class, id);
        } finally {
            em.close();
        }
    }

    public int getTesteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Teste> rt = cq.from(Teste.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
