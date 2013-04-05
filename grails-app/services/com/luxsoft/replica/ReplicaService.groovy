package com.luxsoft.replica

import groovy.sql.Sql
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

/**
 * ReplicaService
 * A service class encapsulates the core business logic of a Grails application
 */
class ReplicaService {

    static transactional = false
	
	def dataSourceLookup

    def resolverUdateQuery(String db,String table) {
		def ds=dataSourceLookup.getDataSource(db)
		Sql sql=new Sql(ds)
		
		def pk=sql.firstRow("""
			SELECT COLUMN_NAME FROM information_schema.COLUMNS
			WHERE (TABLE_SCHEMA = ?)
			AND (TABLE_NAME = ?)
			AND (COLUMN_KEY = 'PRI');
		""",['tacuba',table]).COLUMN_NAME
		
		def columns=sql.rows("""
			SELECT COLUMN_NAME FROM information_schema.COLUMNS
			WHERE (TABLE_SCHEMA = ?)
			AND (TABLE_NAME = ?)
			AND (COLUMN_KEY <> 'PRI');
		""",['tacuba',table])
			
		def res="UPDATE $table SET "
		res+=columns.collect {it.COLUMN_NAME+":"+it.COLUMN_NAME}.join(",")
		
		res+=" WHERE $pk=:$pk"
		
		return res;
	
    }
	
	def importarAuditLog(String origen,String destino){
		def sourceDataSource=dataSourceLookup.getDataSource(origen)
		def targetDataSource=dataSourceLookup.getDataSource(destino)
		Sql sql=new Sql(sourceDataSource)
		Sql target=new Sql(targetDataSource)
		
		sql.eachRow("select * from audit_log where replicado is null order by id") {
			def model=EntityModelFactory.getModel(it.entityName)
			if(model){
				def origenSql="select * from $model.table where $model.columnId=?"
				def row=sql.firstRow(origenSql, [it.entityId])
				if(it.action=='INSERT'){
					println 'Insertando registro: '+row
					try{
						SimpleJdbcInsert insert=new SimpleJdbcInsert(targetDataSource).withTableName(model.table)
						model.excludeColumns.each{
							row.put(it,null)
						}
						insert.execute(row)
						sql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
					}catch(Exception e){
						
						String err=ExceptionUtils.getRootCauseMessage(e)+ " Tipo: "+ExceptionUtils.getRootCause(e).getClass().getName()
						println 'Error insertando: '+err
						sql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", [err,it.id])
						
					}
					
				}else if(it.action=='UPDATE'){
					println 'Update: '+row
					try{
						int updated=target.executeUpdate(row, model.updateSql)
						if(updated)
							sql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
					}catch(Exception e){
						String err=ExceptionUtils.getRootCauseMessage(e)+ " Tipo: "+ExceptionUtils.getRootCause(e).getClass().getName()
						println 'Error en update: '+it+"  "+err
						sql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", [err,it.id])
					}
					
				}else if(it.action=='DELETE'){
					target.execute("DELETE FROM $model.table WHERE $model.columnId=?", [it.id])
				}
				
			}else{
				sql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", ['NO REPLICABLE POR FALTA DE CONFIGURACION',it.id])
			}
			
		}
		
	}
}
