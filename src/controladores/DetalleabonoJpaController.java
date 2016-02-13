/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Detalleabono;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author panlo
 */
public class DetalleabonoJpaController implements Serializable {

    public DetalleabonoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleabono detalleabono) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(detalleabono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleabono detalleabono) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            detalleabono = em.merge(detalleabono);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleabono.getIdDetalleAbono();
                if (findDetalleabono(id) == null) {
                    throw new NonexistentEntityException("The detalleabono with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleabono detalleabono;
            try {
                detalleabono = em.getReference(Detalleabono.class, id);
                detalleabono.getIdDetalleAbono();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleabono with id " + id + " no longer exists.", enfe);
            }
            em.remove(detalleabono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleabono> findDetalleabonoEntities() {
        return findDetalleabonoEntities(true, -1, -1);
    }

    public List<Detalleabono> findDetalleabonoEntities(int maxResults, int firstResult) {
        return findDetalleabonoEntities(false, maxResults, firstResult);
    }

    private List<Detalleabono> findDetalleabonoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleabono.class));
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

    public Detalleabono findDetalleabono(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleabono.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleabonoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleabono> rt = cq.from(Detalleabono.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
