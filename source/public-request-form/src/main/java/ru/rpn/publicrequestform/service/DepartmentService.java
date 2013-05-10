package ru.rpn.publicrequestform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.rpn.publicrequestform.dao.DepartmentDAO;
import ru.rpn.publicrequestform.model.Department;

@Service
public class DepartmentService {
	
	@Autowired
	private DepartmentDAO departmentDAO;
	
	@Transactional(readOnly = true)
	public Department get(long id) {
		return departmentDAO.find(id);
	}

	@Transactional(readOnly = true)
	public List<Department> getAll() {
		return departmentDAO.getAll();
	}
	
	
	@Transactional
	public void save(Department department) {
		if (department != null) {
			if (department.getId() == null) {
				departmentDAO.persist(department);
			} else {
				departmentDAO.merge(department);
			}
		}
	}
	
	@Transactional
	public List<Department> getAllActive() {
		return departmentDAO.getAllActive();
	}

	@Transactional
	public void remove(Long departmentId) {
		Department department = get(departmentId);
		department.setIsEnabled(Boolean.FALSE);
		departmentDAO.merge(department);
	}

	@Transactional
	public void add(String departmentName) {
		Department department = new Department();
		department.setIsEnabled(Boolean.TRUE);
		department.setName(departmentName);
		save(department);
	}
}
