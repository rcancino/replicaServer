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
			
			def sqlSucursalLocal="SELECT * FROM SX_CONFIGURACION WHERE ID=2"
			def cantidad=0;
			def sucLocal=target.firstRow(sqlSucursalLocal)
			if(row.SUCURSAL_ID == sucLocal.SUCURSAL ){
				
				int year=row.fecha.getAt(Calendar.YEAR)
				int mes=row.fecha.getAt(Calendar.MONTH)+1
				def exis=target.firstRow("SELECT * FROM SX_EXISTENCIAS where YEAR=? and MES=? and PRODUCTO_ID=? and SUCURSAL_ID=?", [year,mes,row.PRODUCTO_ID,row.SUCURSAL_ID])
				if(exis){
					
					if(row.TIPO=='TPE'){
						 cantidad=exis.cantidad+row.cantidad
					}else{
						 cantidad=exis.cantidad-row.cantidad
					}
					
					int updated=target.executeUpdate(
						"UPDATE SX_EXISTENCIAS SET CANTIDAD=?,MODIFICADO=? WHERE INVENTARIO_ID=?"
						,[cantidad,new Date(),exis.INVENTARIO_ID])
					log.debug "Existencia actualizada por TRASLADO: "+updated
				
					def sucursalName=sucursal.get(exis.SUCURSAL_ID)
					int insert=target.executeUpdate(
						"INSERT INTO audit_log (entityId,entityName,action,tableName,ip,SUCURSAL_ORIGEN,SUCURSAL_DESTINO,dateCreated,lastUpdated,replicado,version) "+
						" VALUES (?,?,?,?,?,?,?,?,?,?,?)"
						,[exis.INVENTARIO_ID,"Existencia","UPDATE","SX_EXISTENCIAS","10.10.X.X",sucursalName,"OFICINAS",new Date(),new Date(),null,0])
								
			}
			
			
			
			}
		} catch (Exception e) {
			throw new RuntimeException("Error actualizando existencia posterior a TRASLADO ${row.INVENTARIO_ID} "+ExceptionUtils.getRootCauseMessage(e),e)
		}
		
	}
	
	def sucursal=[3L:'TACUBA',6L:'ANDRADE',9L:'CF5FEBRERO',5L:'BOLIVAR',2L:'CALLE4',11L:'VERTIZ',14L:'SOLIS']

}
