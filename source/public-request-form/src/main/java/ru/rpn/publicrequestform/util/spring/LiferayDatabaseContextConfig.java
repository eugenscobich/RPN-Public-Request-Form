package ru.rpn.publicrequestform.util.spring;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.liferay.portal.kernel.util.InfrastructureUtil;

@Configuration
public class LiferayDatabaseContextConfig {

	@Autowired
	private HibernateJpaVendorAdapter jpaAdapter;

	public @Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setJpaVendorAdapter(jpaAdapter);
		//entityManagerFactoryBean.setPersistenceUnitName("requestFormPersistenceUnit");
		entityManagerFactoryBean.setDataSource(InfrastructureUtil.getDataSource());
		return entityManagerFactoryBean;
	}

}
