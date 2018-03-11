package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.security.UserInfo;
import com.shakepoint.web.data.v1.entity.PartnerProductOrder;
import com.shakepoint.web.data.v1.entity.User;
import com.shakepoint.web.data.v1.entity.UserProfile;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * Users Repository extending the BaseRepository class
 *
 * @author Alberto Rubalcaba
 */
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public UserRepositoryImpl() {
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveProfile(UserProfile profile) {
        try {
            em.persist(profile);
        } catch (Exception ex) {
            log.error("Could not add profile", ex);
        }
    }


    private static final String USER_EXISTS = "select count(*) from user where email = ?";

    @Override
    public boolean userExists(String email) {
        try {
            final BigInteger count = (BigInteger) em.createNativeQuery(USER_EXISTS).setParameter(1, email).getSingleResult();
            return count.longValue() > 0;
        } catch (Exception ex) {
            log.error("Could not get user existence", ex);
            return false;
        }
    }

    private static final String GET_LAST_SIGNIN = "select last_signin from user where id = ?";

    @Override
    public String getLastSignin(String id) {

        try {
            return (String) em.createNativeQuery(GET_LAST_SIGNIN).setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get last sign in", ex);
            return null;
        }
    }

    private static final String UPDATE_LAST_SIGNIN = "update user set last_signin = ? where id = ?";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateLastSignin(String email) {
        //get id
        String id = getUserId(email);
        if (id == null) {
            //super admin user
            return;
        }
        //updates
        try {
            em.createNativeQuery(UPDATE_LAST_SIGNIN).setParameter(1, ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date())).setParameter(2, id).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not update user", ex);
        }
    }

    @Override
    public int getRegisteredTechnicians() {
        try {
            BigInteger count = (BigInteger) em.createNativeQuery(GET_TECHNICIANS_COUNT).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get technicians count", ex);
            return 0;
        }
    }

    @Override
    public List<User> getUsers(int pageNumber) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = 'member'")
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get shakepoint users", ex);
            return null;
        }
    }

    private static final String GET_USER_INFO = "select email, password, role from user where email = ?";

    @Override
    public User getUserByEmail(String email) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get user by email", ex);
            return null;
        }
    }

    @Override
    public UserInfo getUserInfo(String email) {
        UserInfo info = null;
        try {
            User user = (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email).getSingleResult();
            return new UserInfo(user.getEmail(), user.getPassword(), user.getRole());
        } catch (Exception ex) {
            log.error("UserInfo not found", ex);
            return null;
        }
    }

    private static final String GET_TECHNICIANS_COUNT = "select count(*) from user where role = 'technician'";

    @Override
    public List<User> getTechnicians() {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = 'technician'").getResultList();
        } catch (Exception ex) {
            log.error("Could not get Technicians", ex);
            return null;
        }
    }


    private static final String GET_USER_ID = "select id from user where email = ?";

    @Override
    public String getUserId(String email) {
        try {
            return (String) em.createNativeQuery(GET_USER_ID).setParameter(1, email).getSingleResult();
        } catch (Exception ex) {
            //not found
            return null;
        }
    }

    private static final String GET_TECHNICIAN = "select id, name, email, creation_date from user where id = ? and role = 'technician'";

    @Override
    public User getTechnician(String id) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.id = :id AND u.role = 'technician'")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get technician", ex);
            return null;
        }
    }


    @Override
    public UserProfile getUserProfile(String userId) {

        try {
            return (UserProfile) em.createQuery("SELECT p FROM Profile p WHERE p.user.id = :id")
                    .setParameter("id", userId)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            log.error("Could not get user profile", ex);
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addShakepointUser(User user) {
        try {
            em.persist(user);
        } catch (Exception ex) {
            log.error("Could not persist Entity", ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateProfile(UserProfile existingProfile) {
        try{
            em.merge(existingProfile);
        }catch(Exception ex){
            log.error("Could not update profile", ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveUserOrder(PartnerProductOrder order) {
        try{
            em.persist(order);
        }catch(Exception ex){
            log.error("Could not persist requested order", ex);
        }
    }


}
