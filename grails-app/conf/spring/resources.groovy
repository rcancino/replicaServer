import grails.util.Environment
import org.apache.commons.dbcp.BasicDataSource
import org.springframework.batch.core.scope.StepScope;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup
import org.springframework.orm.hibernate3.HibernateTemplate;

// Place your Spring DSL code here
beans = {
	customPropertyEditorRegistrar(CustomDateEditorRegistrar)
	
	
	tacubaDataSource(BasicDataSource){
		driverClassName = 'com.mysql.jdbc.Driver'
		username = 'root'
		password = 'sys'
		url = 'jdbc:mysql://10.10.1.228/tacuba?autoReconnect=true'
	}
	
	oficinasDataSource(BasicDataSource){
		driverClassName = 'com.mysql.jdbc.Driver'
		username = 'root'
		password = 'sys'
		url = 'jdbc:mysql://10.10.1.221/certificacion?autoReconnect=true'
	}

	switch(Environment.current) {
		case Environment.TEST:
			tacubaDataSource(SingleConnectionDataSource){
				driverClassName="com.mysql.jdbc.Driver"
				url="jdbc:mysql://localhost:3306/tacuba"
				username="root"
				password="sys"
				suppressClose="false"
			}
		break
		case Environment.DEVELOPMENT:
			tacubaDataSource(BasicDataSource){
				driverClassName = 'com.mysql.jdbc.Driver'
				username = 'root'
				password = 'sys'
				url = 'jdbc:mysql://localhost/tacuba?autoReconnect=true'
			}
		
			oficinasDataSource(BasicDataSource){
				driverClassName = 'com.mysql.jdbc.Driver'
				username = 'root'
				password = 'sys'
				url = 'jdbc:mysql://localhost/produccion?autoReconnect=true'
			}
		break
	}

	dataSourceLookup(BeanFactoryDataSourceLookup){
		
	}
	
	stepScope(StepScope){}
	
	

}
