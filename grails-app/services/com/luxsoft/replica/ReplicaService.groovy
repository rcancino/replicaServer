package com.luxsoft.replica

import groovy.sql.Sql

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

/**
 * ReplicaService
 * A service class encapsulates the core business logic of a Grails application
 */
class ReplicaService {

    static transactional = false
	
	def dataSourceLookup
	
	def findUpdateQuery(def db,String table) {
		def ds=dataSourceLookup.getDataSource(db)
		Sql sql=new Sql(ds)
		return resolverUdateQuery(sql,table)
	}
	
	def resolverPk(def sql,String table) {
		def pk=sql.firstRow("""
			SELECT COLUMN_NAME FROM information_schema.COLUMNS
			WHERE (TABLE_SCHEMA = ?)
			AND (TABLE_NAME = ?)
			AND (COLUMN_KEY = 'PRI');
		""",['tacuba',table]).COLUMN_NAME
		return pk
	}

	def resolverUdateQuery(def sql,String table) {
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
		res+=columns.collect {it.COLUMN_NAME+"=:"+it.COLUMN_NAME}.join(",")
		
		res+=" WHERE $pk=:$pk"
		
		return res;
	}
	
    def importarAuditLog(String origen,String destino){
		println "Importando logs De $origen a $destino"
		def sourceDataSource=dataSourceLookup.getDataSource(origen)
		def targetDataSource=dataSourceLookup.getDataSource(destino)
		Sql sourceSql=new Sql(sourceDataSource)
		Sql targetSql=new Sql(targetDataSource)
		
		sourceSql.eachRow("select * from audit_log where replicado is null order by id") {
			def model=EntityModelFactory.getModel(it.entityName)
			
			def config=EntityConfiguration.findByName(it.entityName)
			if(config){
				//println 'Usando Config: '+config
				def origenSql="select * from $config.table where $config.pk=?"
				def row=sourceSql.firstRow(origenSql, [it.entityId])
				
				try {
					println 'Importacion de registro tipo: '+it.action+ 'Row: '+row
					switch (it.action) {
						case 'INSERT':
						println 'Insertando '+config.table
							SimpleJdbcInsert insert=new SimpleJdbcInsert(targetDataSource).withTableName(config.table)
							if(config.excludeInsertColumns){
								println 'Exlude: '+config.excludeInsertColumns
								def cols=config.excludeInsertColumns.split(',')
								cols.each{
									row.put(it,null)
								}
								
							}
							
							insert.execute(row)
							sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
							break
						case 'UPDATE':
							if(config.excludeUpdateColumns){
								println 'Exlude: '+config.excludeInsertColumns
								def cols=config.excludeUpdateColumns.split(',')
								cols.each{
									row.put(it,null)
								}
								
							}
							int updated=targetSql.executeUpdate(row, config.updateSql)
							if(updated)
								sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
							break
						case 'DELETE':
							int eliminados=targetSql.execute("DELETE FROM $config.table WHERE $config.columnId=?", [it.id])
							if(eliminados)
								sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["NO SE PUDO ELIMIAR",it.id])
							break;
						default:
							break;
					}
				} catch (Exception e) {
					e.printStackTrace()
					String err=ExceptionUtils.getRootCauseMessage(e)
					sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", [err,it.id])
				}
				
			}else
				sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", ['NO REPLICABLE POR FALTA DE CONFIGURACION',it.id])
			
		}
	}
	
	def instalarConfiguraciones(){
		/*def entidades=['SolicitudDeDeposito':'SX_SOLICITUDES_DEPOSITO','Movimiento':'SX_MOVI'
				,'MovimientoDet':'SX_INVENTARIO_MOV'
				,'Existencia':'SX_EXISTENCIAS']*/
		def ds=dataSourceLookup.getDataSource('tacubaDataSource')
		
		def sql=new Sql(ds)
		EntityModelFactory.entidades.each { key, value ->
			println "Instalando configuracion $key - $value"
			
			def config=EntityConfiguration.findByName(key)
			
			if(!config){
				config=new EntityConfiguration(name:key,table:value)
				config.updateSql=resolverUdateQuery(sql,config.table)
				SimpleJdbcInsert insert=new SimpleJdbcInsert(ds).withTableName(value)
				insert.compile()
				config.insertSql=insert.getInsertString()
				config.pk=resolverPk(sql, config.table)
				config.save(failOnError:true)
			}
		}
	}
}
