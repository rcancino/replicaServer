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
	
	
	abstractDataSource(BasicDataSource){bean->
		bean.'abstract'=true	
		driverClassName = 'com.mysql.jdbc.Driver'
		username = 'root'
		password = 'sys'
		maxWait=6000
	}
	

	switch(Environment.current) {
		case Environment.TEST:			
		break
		case Environment.DEVELOPMENT:
			oficinasDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.1.221/produccion'
			}
			tacubaDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.1.101/tacuba_produccion'
			}
			andradeDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.2.1/produccion'
			}
			bolivarDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.4.1/produccion'
			}
			cincoFebreroDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.7.1/produccion'
			}
			vertizDataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.2.100/produccion'
			}
			calle4DataSource(){bean->
				bean.parent = abstractDataSource
				url = 'jdbc:mysql://10.10.5.1/produccion'
			}
		break
	}

	dataSourceLookup(BeanFactoryDataSourceLookup){}
	
	stepScope(StepScope){}
	
	

}
