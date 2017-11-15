package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.entity.Fail;

import java.util.List;

public interface FailRepository {
	public void addFail(Fail fail);
	public List<Fail> getMachineFails(String machineId, int pageNumber);
	public List<Fail> getMachineFails(String machineId, String[] range, int pageNumber);
}
