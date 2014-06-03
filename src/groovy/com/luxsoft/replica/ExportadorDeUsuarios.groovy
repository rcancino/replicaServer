package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import groovy.sql.Sql

/**
 * Exportador para las colecciones de los clientes
 * 
 * @author Ruben Cancino
 *
 */
class ExportadorDeUsuarios {
	
	
	def entityConfigurationService
	
	def tables=["SX_USER_ROLE"]
	
	
	def exportarCollecciones(Long id,Sql source,Sql target){
		tables.each {table->
			try {
				def rows=source.rows("SELECT * FROM $table WHERE USER_ID=?",[id])
				println "Buscando Roles para usuario"+id
				target.execute("DELETE  FROM $table WHERE USER_ID=?",[id])
				println "Borrando Roles para usuario"+id
				if(rows){
					SimpleJdbcInsert insert=new SimpleJdbcInsert(target.dataSource).withTableName(table)
					rows.each { row->
						println "Insertando"+row
						insert.execute(row)
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("ERROR exportando a $table USER_ID:$id"+ExceptionUtils.getRootCauseMessage(e),e)
			}
		}
	}
	
	

}
