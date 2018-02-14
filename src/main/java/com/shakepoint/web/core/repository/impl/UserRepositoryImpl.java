package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.dto.res.rest.UserProfileResponse;
import com.shakepoint.web.data.security.UserInfo;
import com.shakepoint.web.data.v1.entity.ShakepointUser;
import com.shakepoint.web.data.v1.entity.ShakepointUserProfile;
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
    public void saveProfile(ShakepointUserProfile profile) {
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
    public List<ShakepointUser> getUsers(int pageNumber) {
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
    public ShakepointUser getUserByEmail(String email) {
        try {
            return (ShakepointUser) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
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
            ShakepointUser user = (ShakepointUser) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email).getSingleResult();
            return new UserInfo(user.getEmail(), user.getPassword(), user.getRole());
        } catch (Exception ex) {
            log.error("UserInfo not found", ex);
            return null;
        }
    }

    private static final String GET_TECHNICIANS_COUNT = "select count(*) from user where role = 'technician'";

    @Override
    public List<ShakepointUser> getTechnicians() {
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
    public ShakepointUser getTechnician(String id) {
        try {
            return (ShakepointUser) em.createQuery("SELECT u FROM User u WHERE u.id = :id AND u.role = 'technician'")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get technician", ex);
            return null;
        }
    }

    private static final String GET_USER_PROFILE = "select p.id, p.age, p.birthday, p.weight, p.height, u.name, u.id user_id as userId, u.creation_date as userSince, u.email,  "
            + "(select sum(pu.total) from purchase pu where pu.user_id = p.user_id) purchasesTotal "
            + "from user_profile p inner join user u on p.user_id = u.id where p.user_id = ?";

    @Override
    public ShakepointUserProfile getUserProfile(String userId) {

        try {
            return (ShakepointUserProfile) em.createQuery("SELECT p FROM Profile p WHERE p.user.id = :id")
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
    public void addShakepointUser(ShakepointUser shakepointUser) {
        try {
            em.persist(shakepointUser);
        } catch (Exception ex) {
            log.error("Could not persist Entity", ex);
        }
    }


}
