package com.shakepoint.web.core.repository.impl;


import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.dto.res.ShakePointUser;
import com.shakepoint.web.data.dto.res.TechnicianDTO;
import com.shakepoint.web.data.dto.res.rest.UserProfile;
import com.shakepoint.web.data.entity.Profile;
import com.shakepoint.web.data.entity.User;
import com.shakepoint.web.data.security.UserInfo;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
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

    private static final String CREATE_PROFILE = "insert into user_profile(id, user_id, age, birthday, weight, height) values(?, ?, ?, ?, ?, ?)";

    @Override
    public void saveProfile(Profile profile) {
        try {
            em.createNativeQuery(CREATE_PROFILE)
                    .setParameter(1, profile.getId())
                    .setParameter(2, profile.getUserId())
                    .setParameter(3, profile.getAge())
                    .setParameter(4, profile.getBirthday())
                    .setParameter(5, profile.getWeight())
                    .setParameter(6, profile.getHeight())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add profile", ex);
        }
    }

    private static final String UPDATE_PROFILE = "update user_profile set height = ?, weight = ? where user_id = ?";

    @Override
    public void updateProfile(Profile profile) {
        try {
            em.createNativeQuery(UPDATE_PROFILE)
                    .setParameter(1, profile.getHeight())
                    .setParameter(2, profile.getWeight())
                    .setParameter(3, profile.getUserId())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not update profile", ex);
        }
    }

    private static final String HAS_PROFILE = "select count(*) from user_profile where user_id = ?";

    @Override
    public boolean hasProfile(String userId) {
        try {
            Long count = (Long) em.createNativeQuery(HAS_PROFILE).setParameter(1, userId).getSingleResult();
            return count > 0;
        } catch (Exception ex) {
            log.error("Could not get has profile value", ex);
            return false;
        }
    }

    private static final String USER_EXISTS = "select count(*) from user where email = ?";

    @Override
    public boolean userExists(String email) {
        try {
            Long count = (Long) em.createNativeQuery(USER_EXISTS).setParameter(1, email).getSingleResult();
            return count > 0;
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

    @Override
    public void updateLastSignin(String email) {
        //get id
        String id = getUserId(email);
        if (id == null){
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
            Long count = (Long) em.createNativeQuery(GET_TECHNICIANS_COUNT).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get technicians count", ex);
            return 0;
        }
    }

    private static final String GET_USERS = "select u.id, u.name, u.email, "
            + "(select sum(p.total) from purchase p where p.user_id = u.id) purchasesTotal "
            + "from user u";

    @Override
    public List<ShakePointUser> getUsers(int pageNumber) {
        try {
            return em.createNativeQuery(GET_USERS)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get shakepoint users", ex);
            return null;
        }
    }

    private static final String GET_USER_INFO = "select email, password, role from user where email = ?";

    @Override
    public UserInfo getUserInfo(String email) {
        Object[] args = {email};
        UserInfo info = null;
        try {
            return (UserInfo) em.createNativeQuery(GET_USER_INFO)
                    .setParameter(1, email).getSingleResult();
        } catch (Exception ex) {
            log.error("UserInfo not found", ex);
        }
        return info;
    }

    private static final String ADD_USER = "insert into user(id, name, email, creation_date, password, is_confirmed, role, active, added_by) "
            + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String addUser(User user) {
        try {
            em.createNativeQuery(ADD_USER)
                    .setParameter(1, user.getId())
                    .setParameter(2, user.getName())
                    .setParameter(3, user.getEmail())
                    .setParameter(4, user.getCreationDate())
                    .setParameter(5, user.getPassword())
                    .setParameter(6, user.isConfirmed() ? 1 : 0)
                    .setParameter(7, user.getRole())
                    .setParameter(8, user.isActive() ? 1 : 0)
                    .setParameter(9, user.getAddedBy()).executeUpdate();
            return user.getId();
        } catch (Exception ex) {
            log.error("Could not add User", ex);
            return null;
        }
    }

    private static final String GET_TECHNICIANS_COUNT = "select count(*) from user where role = 'technician'";
    private static final String GET_TECHNICIANS = "select id, name, email, creation_date from user where role = 'technician'";

    public List<TechnicianDTO> getTechnicians(int pageNumber) {
        try {
            return em.createNativeQuery(GET_TECHNICIANS)
                    .getResultList();
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
    public TechnicianDTO getTechnician(String id) {
        Object[] args = {id};
        TechnicianDTO dto = null;

        try {
            return (TechnicianDTO) em.createNativeQuery(GET_TECHNICIAN).setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get technician", ex);
            return null;
        }
    }

    /**
     * static class UserRepositoryMappers {
     * <p>
     * public static ParameterizedRowMapper<TechnicianDTO> TECHNICIAN_MAPPER = new ParameterizedRowMapper<TechnicianDTO>() {
     *
     * @Override public TechnicianDTO mapRow(ResultSet rs, int i) throws SQLException {
     * TechnicianDTO dto = new TechnicianDTO();
     * dto.setId(rs.getString(ID));
     * dto.setEmail(rs.getString(EMAIL));
     * dto.setName(rs.getString(NAME));
     * dto.setCreationDate(rs.getString(CREATION_DATE));
     * return dto;
     * }
     * };
     * <p>
     * public static ParameterizedRowMapper<ShakePointUser> SHAKEPOINT_USER_MAPPER = new ParameterizedRowMapper<ShakePointUser>() {
     * @Override public ShakePointUser mapRow(ResultSet rs, int i) throws SQLException {
     * ShakePointUser dto = new ShakePointUser();
     * dto.setId(rs.getString(ID));
     * dto.setEmail(rs.getString(EMAIL));
     * dto.setName(rs.getString(NAME));
     * dto.setPurchasesTotal(rs.getDouble(TOTAL));
     * <p>
     * return dto;
     * }
     * };
     * public static ParameterizedRowMapper<UserInfo> USER_INFO_MAPPER = new ParameterizedRowMapper<UserInfo>() {
     * @Override public UserInfo mapRow(ResultSet rs, int i) throws SQLException {
     * UserInfo info = new UserInfo();
     * info.setPassword(rs.getString(PASSWORD));
     * info.setRole(rs.getString(ROLE));
     * info.setEmail(rs.getString(EMAIL));
     * return info;
     * }
     * };
     * public static ParameterizedRowMapper<UserProfile> USER_PROFILE_MAPPER = new ParameterizedRowMapper<UserProfile>() {
     * @Override public UserProfile mapRow(ResultSet rs, int i) throws SQLException {
     * UserProfile info = new UserProfile();
     * info.setAge(rs.getInt(AGE));
     * info.setBirthday(rs.getString(BIRTHDAY));
     * info.setAvailableProfile(true);
     * info.setHeight(rs.getDouble(HEIGHT));
     * info.setWeight(rs.getDouble(WEIGHT));
     * info.setUserName(rs.getString(NAME));
     * info.setUserId(rs.getString(USER_ID));
     * info.setUserSince(rs.getString(CREATION_DATE));
     * info.setPurchasesTotal(rs.getDouble(TOTAL));
     * info.setEmail(rs.getString(EMAIL));
     * return info;
     * }
     * };
     * }
     **/

    private static final String GET_USER_PROFILE = "select p.id, p.age, p.birthday, p.weight, p.height, u.name, u.id user_id as userId, u.creation_date as userSince, u.email,  "
            + "(select sum(pu.total) from purchase pu where pu.user_id = p.user_id) purchasesTotal "
            + "from user_profile p inner join user u on p.user_id = u.id where p.user_id = ?";

    @Override
    public UserProfile getUserProfile(String userId) {
        UserProfile profile = null;

        try {
            return (UserProfile) em.createNativeQuery(GET_USER_PROFILE)
                    .setParameter(1, userId)
                    .getSingleResult();

        } catch (Exception ex) {
            log.error("Could not get user profile", ex);
            profile = new UserProfile();
            profile.setAvailableProfile(false);

        }
        return profile;
    }

    private static final String GET_USER_EMAIL = "select email from user where id = ?";

    @Override
    public String getUserEmail(String userId) {
        String email = "";
        try {
            return (String) em.createNativeQuery(GET_USER_EMAIL)
                    .setParameter(1, userId).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get user email", ex);
            return null;
        }
    }

}
