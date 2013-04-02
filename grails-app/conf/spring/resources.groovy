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
	}

	dataSourceLookup(BeanFactoryDataSourceLookup){
		
	}
	
	stepScope(StepScope){}
	
	

}
