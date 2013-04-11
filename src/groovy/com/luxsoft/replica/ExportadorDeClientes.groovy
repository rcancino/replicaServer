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
class ExportadorDeClientes {
	
	
	def entityConfigurationService
	
	def tables=[
			'SX_CLIENTES_DIRECCIONES'
			,"SX_CLIENTES_COMENT"
			,"SX_CLIENTES_TELS"
			,"SX_CLIENTES_CONTACTOS"
			,"SX_CLIENTES_CUENTAS"]
	
	
	def exportarCollecciones(Long id,Sql source,Sql target){
		tables.each {table->
			try {
				def rows=source.rows("SELECT * FROM $table WHERE CLIENTE_ID=?",[id])
				
				target.execute("DELETE  FROM $table WHERE CLIENTE_ID=?",[id])
				if(rows){
					SimpleJdbcInsert insert=new SimpleJdbcInsert(target.dataSource).withTableName(table)
					rows.each { row->
						insert.execute(row)
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("ERROR exportando a $table CLIENTE_ID:$id"+ExceptionUtils.getRootCauseMessage(e),e)
			}
		}
	}
	
	

}
