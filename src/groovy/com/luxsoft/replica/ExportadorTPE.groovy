package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import groovy.sql.Sql

/**
 * Exportador para las colecciones de los clientes
 * 
 * @author Ruben Cancino
 *
 */
class ExportadorTPE {
	
	private static final log=LogFactory.getLog(this)
	
	def acutalizarExistencias(def row,Sql target){
		try {
			int year=row.fecha.getAt(Calendar.YEAR)
			int mes=row.fecha.getAt(Calendar.MONTH)+1
			def exis=target.firstRow("SELECT * FROM SX_EXISTENCIAS where YEAR=? and MES=? and PRODUCTO_ID=?", [year,mes,row.PRODUCTO_ID])
			if(exis){
				def cantidad=exis.cantidad+row.cantidad
				int updated=target.executeUpdate(
					"UPDATE SX_EXISTENCIAS SET CANTIDAD=? WHERE INVENTARIO_ID=?"
					,[cantidad,exis.INVENTARIO_ID])
				log.debug "Existencia actualizada por TPE: "+updated
			}
		} catch (Exception e) {
			throw new RuntimeException("Error actualizando existencia posterior a TPE ${row.INVENTARIO_ID} "+ExceptionUtils.getRootCauseMessage(e),e)
		}
		
	}
	
	

}
