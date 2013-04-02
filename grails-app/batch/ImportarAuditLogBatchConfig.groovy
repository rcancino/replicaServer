
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;


import com.luxsoft.replica.AuditLogMapper;



beans{
	xmlns batch:"http://www.springframework.org/schema/batch"
	
	batch.job(id:'importacionDeAuditLogsJob'){
		batch.step(id:'leerRegistros'){
			batch.tasklet(){
				batch.chunk(reader:'auditLogReader',writer:'auditLogWriter','commit-interval':'10'){
					
				}
			}
		}
	}
	
	auditLogReader(JdbcCursorItemReader){bean->
		bean.scope="step"
		sql="select * from AUDIT_LOG where replicado is null  "
		//dataSource=ref("#{jobParameters['sourceDataSource']}")
		dataSource=ref "#{jobParameters['sourceDataSource']}"
		//dataSource=ref("tacubaDataSource")
		rowMapper={AuditLogMapper mapper->}
	}
	
	auditLogWriter(HibernateItemWriter){bean ->
		bean.scope="step"
		sessionFactory=ref('sessionFactory')
	}
}