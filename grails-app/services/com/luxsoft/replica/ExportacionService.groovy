package com.luxsoft.replica

import groovy.sql.Sql

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

/**
 * 
 */
class ExportacionService {

    static transactional = false
	def dataSourceLookup
	
	def exportadorDeClientes=new ExportadorDeClientes()
	def exportadorDeProveedoes=new ExportadorDeProveedores()
	def exportadorTPE=new ExportadorTPE()
	
	
	
	def exportar(Sql sourceSql,Sql targetSql,def config,def rows){
		
		def targetDataSource=targetSql.dataSource
		rows.each { row->
			
			try {
				if(config.excludeUpdateColumns){
					def cols=config.excludeUpdateColumns.split(',')
					cols.each{
						row.put(it,null)
					}
				}
				int updated=targetSql.executeUpdate(row, config.updateSql)
				if(!updated){
					SimpleJdbcInsert insert=new SimpleJdbcInsert(targetDataSource).withTableName(config.tableName)
					if(config.excludeInsertColumns){
						def cols=config.excludeInsertColumns.split(',')
						cols.each{
							row.put(it,null)
						}
					}
					insert.execute(row)
				}
				afterExport(config,row,sourceSql,targetSql)
			} catch (Exception e) {
				String msg="Error exportando registro $row: "ExceptionUtils.getRootCauseMessage(e)
				log.error(msg,e)
			}
			
		}
	}
	
	
	private afterExport(def config,def row,def sourceSql,def targetSql){
		
		switch (config.name) {
		case "Cliente":
			log.debug 'Exportando colecciones del cliente'
			//println 'Exportando existencias...'
			exportadorDeClientes.exportarCollecciones(row.CLIENTE_ID,sourceSql,targetSql)
			break
		case "Proveedor":
			exportadorDeProveedoes.exportarCollecciones(row.PROVEEDOR_ID,sourceSql,targetSql)
			break
			case "TrasladoDet":
			if(row.TIPO=='TPE'){
				//exportadorTPE.acutalizarExistencias(row,targetSql)
			}
			break
		default: 
			break;
		}
		 
	}
	
	
}
