package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import groovy.sql.Sql

/**
 * Exportador para las colecciones de proveedores
 * 
 * @author Ruben Cancino
 *
 */
class ExportadorDeCFDIClientesMail {
	
	
	def entityConfigurationService
	
	def tables=["SX_CLIENTES_CFDI_MAILS"]
	
	
	def exportarCollecciones(Long id,Sql source,Sql target){
		tables.each {table->
			try {
				def rows=source.rows("SELECT * FROM $table WHERE PROVEEDOR_ID=?",[id])
				
				target.execute("DELETE  FROM $table WHERE PROVEEDOR_ID=?",[id])
				if(rows){
					SimpleJdbcInsert insert=new SimpleJdbcInsert(target.dataSource).withTableName(table)
					rows.each { row->
						insert.execute(row)
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("ERROR exportando a $table PROVEEDOR_ID:$id"+ExceptionUtils.getRootCauseMessage(e),e)
			}
		}
	}
	
	

}
