package com.shakepoint.web.core.repository.impl;
import com.shakepoint.web.core.repository.FailRepository;
import com.shakepoint.web.data.entity.Fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


public class FailRepositoryImpl implements FailRepository {

	@PersistenceContext
	private EntityManager em;

	public FailRepositoryImpl() {

	}

	private static final String ADD_FAIL = "insert into fail(id, machine_id, message, fail_date) values(?, ?, ?, ?)";

	@Override
	public void addFail(Fail fail) {
		em.persist(fail);
	}

	private static final String GET_MACHINE_FAILS = "select id, message, machine_id, fail_date from fail where machine_id = ? limit %d,%d";
	private static final String GET_MACHINE_FAILS_COUNT = "select count(*) from fail where machine_id = ?";
	@Override
	public List<Fail> getMachineFails(String machineId, int pageNumber) {
		return em.createQuery("SELECT f FROM Fail f where f.machineId = :machineId").setParameter("machineId", machineId)
				.getResultList();
	}

	private static final String GET_MACHINE_FAILS_RANGE = "select id, message, machine_id, fail_date from fail where machine_id = ? and fail_date >= ? and fail_date <= ? limit %d,%d";
	private static final String GET_MACHINE_FAILS_COUNT_RANGE = "select count(*) from fail where machine_id = ?";
	@Override
	public List<Fail> getMachineFails(String machineId, String[] range, int pageNumber) {
		return em.createQuery("SELECT f FROM Fail f WHERE f.machineId = :machineId AND f.failDate > :failDateAfter AND f.failDate <= :failDateBefore")
				.setParameter("machineId", machineId)
				.setParameter("failDateAfter", range[0])
				.setParameter("failDateBefore", range[1])
				.getResultList();
	}

}
