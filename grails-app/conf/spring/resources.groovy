import grails.util.Environment
import org.apache.commons.dbcp.BasicDataSource
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup

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
}
