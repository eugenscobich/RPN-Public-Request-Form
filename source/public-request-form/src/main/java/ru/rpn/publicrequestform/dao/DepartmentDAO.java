package ru.rpn.publicrequestform.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ru.rpn.publicrequestform.model.Department;

@Repository
public class DepartmentDAO extends BaseDAO<Department> {

	@SuppressWarnings("unchecked")
	public List<Department> getAllActive() {
		Query query = getEntityManager().createQuery("from " + Department.class.getName() + " d where d.isEnabled = true");
		return query.getResultList();
	}


}
