package ru.rpn.publicrequestform.util.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liferay.portal.kernel.util.InfrastructureUtil;

@Configuration
public class LiferayDatabaseContextConfig {

	public @Bean DataSource dataSource() throws Exception {
		return InfrastructureUtil.getDataSource();
	}
	
}
