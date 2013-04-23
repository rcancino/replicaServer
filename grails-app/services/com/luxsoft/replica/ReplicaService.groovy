package com.luxsoft.replica

import groovy.sql.Sql

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert

/**
 * ReplicaService
 * A service class encapsulates the core business logic of a Grails application
 */
class ReplicaService {

    static transactional = false
	
	def dataSourceLookup
	
	def grailsApplication
	
	def exportadorDeClientes=new ExportadorDeClientes()
	def exportadorDeProveedoes=new ExportadorDeProveedores()
	def exportadorTPE=new ExportadorTPE()
	
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
		""",['produccion',table]).COLUMN_NAME
		return pk
	}

	def resolverUdateQuery(def sql,String table) {
		def pk=sql.firstRow("""
			SELECT COLUMN_NAME FROM information_schema.COLUMNS
			WHERE (TABLE_SCHEMA = ?)
			AND (TABLE_NAME = ?)
			AND (COLUMN_KEY = 'PRI');
		""",['produccion',table]).COLUMN_NAME
		
		def columns=sql.rows("""
			SELECT COLUMN_NAME FROM information_schema.COLUMNS
			WHERE (TABLE_SCHEMA = ?)
			AND (TABLE_NAME = ?)
			AND (COLUMN_KEY <> 'PRI');
		""",['produccion',table])
			
		def res="UPDATE $table SET "
		res+=columns.collect {it.COLUMN_NAME+"=:"+it.COLUMN_NAME}.join(",")
		
		res+=" WHERE $pk=:$pk"
		
		return res;
	}
	
    def importarAuditLog(String origen,String destino){
		
		def sourceDataSource=dataSourceLookup.getDataSource(origen)
		def targetDataSource=dataSourceLookup.getDataSource(destino)
		Sql sourceSql=new Sql(sourceDataSource)
		Sql targetSql=new Sql(targetDataSource)
		
		//log.debug("Importando  $origen a $destino")
		
		def rows=sourceSql.rows("select * from audit_log where replicado is null order by id")
		//println "Importando logs De $origen a $destino Audits: ${rows.size()}"
		log.info("Importando logs De $origen a $destino Audits: ${rows.size()}")
		
		sourceSql.eachRow("select * from audit_log where replicado is null order by id") {
		
			//println 'Importando con audit: '+it
			def config=EntityConfiguration.findByName(it.entityName)
			if(!config){
				//println 'Generando un config nuevo:' 
				config=crearConfiguracion(it.entityName, it.tableName, sourceDataSource)
			}
			
			if(config){
				//println 'Usando Config: '+config
				
				def origenSql="select * from $config.tableName where $config.pk=?"
				//println origenSql
				def row=sourceSql.firstRow(origenSql, [it.entityId])
				if(it.action=='DELETE' || row){
					//println origenSql + "Row: "+row
					try {
						switch (it.action) {
							case 'INSERT':
								//println 'Insertando '+row
								SimpleJdbcInsert insert=new SimpleJdbcInsert(targetDataSource).withTableName(config.tableName)
								if(config.excludeInsertColumns){
									//println 'Exlude: '+config.excludeInsertColumns
									def cols=config.excludeInsertColumns.split(',')
									cols.each{
										row.put(it,null)
									}
									
								}
								
								def res=insert.execute(row)
								//println 'Registros importados: '+res
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
								//if(updated)
									sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["ACTUALIZADO: "+updated,it.id])
								break
							case 'DELETE':
								targetSql.execute("DELETE FROM ${config.tableName} WHERE ${config.pk}=?",[it.entityId])
								def res=targetSql.firstRow("SELECT *  FROM ${config.tableName} WHERE ${config.pk}=?",[it.entityId])
								if(!res)
									sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
								break;
							default:
								break;
						}
						trasladarCollecciones(config, row, it, sourceSql, targetSql)
						afterImport(config,row,it,sourceSql)
						log.info "Importado OK:  "+row
					} catch (DuplicateKeyException dk) {
						println dk.getMessage()
						log.info "Registro duplicado"
						sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["Registri duplicado",it.id])
					
					}catch (Exception e){
						//e.printStackTrace()
						log.error(e)
						String err="Error imporando a oficinas: "+ExceptionUtils.getRootCauseMessage(e)
						sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=?,REPLICADO=null WHERE ID=? ", [err,it.id])
					}
					
				}else{
					//println 'Registro inexistente para Audit: '+it
					sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", ['REGISTRO INEXISTENTE',it.id])
				}
				
				
			}else
				sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", ['NO REPLICABLE POR FALTA DE CONFIGURACION',it.id])
		}
	}
	
	
	private trasladarCollecciones(def config,def dataRow,def auditRow,Sql sourceSql,Sql targetSql){
		if(config.name=='SolicitudDeTraslado'){
			//Se eliminan los registros originales por tratarse de una relacion de composicion
			log.debug 'Trasladando partidas de SOL: '+auditRow.entityId
			targetSql.execute("DELETE FROM sx_solicitud_trasladosdet where SOL_ID=?",[auditRow.entityId])
			if(auditRow.action!='DELETE'){
				def partidas=sourceSql.rows("SELECT * FROM sx_solicitud_trasladosdet WHERE SOL_ID=?",[auditRow.entityId])
				log.debug "Importando  $partidas.size partidas para SOL: "+auditRow.entityId
				SimpleJdbcInsert insert=new SimpleJdbcInsert(targetSql.dataSource).withTableName("sx_solicitud_trasladosdet")
				partidas.each{
					insert.execute(it);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param config
	 * @return
	 */
	private afterImport(def config,def dataRow,def auditRow,def sourceSql){
		
		
		if(auditRow.sucursal_destino!='OFICINAS'){
			log.debug 'Dispersando a :'+auditRow.sucursal_destino
			def auditLog=new AuditLog(
				action: auditRow.action
				,entityId: auditRow.entityId
				,entityName: auditRow.entityName
				,tableName: auditRow.tableName
				,sucursalOrigen: auditRow.sucursal_origen
				,sucursalDestino: auditRow.sucursal_destino
				,replicado:null
				,ip: auditRow.ip,)
				.save(failOnError:true)
		}
		
		switch (config.name) {
		case 'Existencia':
			log.info("Dispersando existencia")
			dispersar(config, dataRow,auditRow)
			break;
		case 'Cliente':
			log.info("Dispersando clientes")
			//auditRow.action='INSERT'
			dispersar(config, dataRow,auditRow)
			break;
		default:
			break;
		}
		
	}
	
	def dispersar(def config,def dataRow,def auditRow){
		def destinos=getDestinos();
		//log.debug 'Dispersando movimiento a las sucursales..'+destinos
		def action=auditRow.action
		if(config.name=='Cliente')
			action='INSERT'
		destinos.each{destino->
			if(destino!=auditRow.sucursal_origen){
				log.debug 'Dispersando a :'+destino
				def auditLog=new AuditLog(
					action: action
					,entityId: auditRow.entityId
					,entityName: auditRow.entityName
					,tableName: auditRow.tableName
					,sucursalOrigen: auditRow.sucursal_origen
					,sucursalDestino: destino
					,ip: auditRow.ip,)
					.save(failOnError:true)
			}else{
				//log.debug 'Ignorando dispersion a :'+destino
			}
		}
			
	}
	
	def exportarAuditLog(Sucursal sucOrigen,Sucursal sucDestino){
		def origen=sucOrigen.dataSourceName
		def destino=sucDestino.dataSourceName
		def sourceDataSource=dataSourceLookup.getDataSource(origen)
		def targetDataSource=dataSourceLookup.getDataSource(destino)
		
		
		Sql sourceSql=new Sql(sourceDataSource)
		Sql targetSql=new Sql(targetDataSource)
		
		def rows=sourceSql.rows("select * from audit_log where replicado is null  and sucursal_destino=? order by id",[sucDestino.nombre])
		log.info("Exportando   a $destino Audits: "+rows.size())
		
		rows.each{
			def config=EntityConfiguration.findByName(it.entityName)
			if(!config){
				config=crearConfiguracion(it.entityName, it.tableName, sourceDataSource)
			}
			
			def origenSql="select * from $config.tableName where $config.pk=?"
			def row=sourceSql.firstRow(origenSql, [it.entityId])
			
			try {
				log.debug("Exportando log: ${it} Row: ${row}")
				switch (it.action) {
					case 'INSERT':
						SimpleJdbcInsert insert=new SimpleJdbcInsert(targetDataSource).withTableName(config.tableName)
						if(config.excludeInsertColumns){
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
						targetSql.execute("DELETE FROM ${config.tableName} WHERE ${config.pk}=?",[it.entityId.toLong()])
						sourceSql.execute("UPDATE AUDIT_LOG SET REPLICADO=NOW(),MESSAGE=? WHERE ID=? ", ["",it.id])
						break;
					default:
						break;
				}
				afterExport(config,row,sourceSql,targetSql)
				trasladarCollecciones(config, row, it, sourceSql, targetSql)
			} catch (Exception e) {
				//e.printStackTrace()
				String err=ExceptionUtils.getRootCauseMessage(e)
				sourceSql.execute("UPDATE AUDIT_LOG SET MESSAGE=? WHERE ID=? ", [err,it.id])
			}
		}
	}
	
	/**
	 * 
	 * @param config
	 * @param row
	 * @param targetSql
	 * @return
	 */
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
				exportadorTPE.acutalizarExistencias(row,targetSql)
			}
			break
		default: 
			break;
		}
		 
	}
	
	/**
	 * Mover a ConfigurationController
	 * 
	 * @return
	 */
	def instalarConfiguraciones(){
		
		def ds=dataSourceLookup.getDataSource('tacubaDataSource')
		
		def sql=new Sql(ds)
		EntityModelFactory.entidades.each { key, value ->
			println "Instalando configuracion $key - $value"
			
			def config=EntityConfiguration.findByName(key)
			
			if(!config){
				config=new EntityConfiguration(name:key,tableName:value)
				config.updateSql=resolverUdateQuery(sql,config.tableName)
				SimpleJdbcInsert insert=new SimpleJdbcInsert(ds).withTableName(value)
				insert.compile()
				config.insertSql=insert.getInsertString()
				config.pk=resolverPk(sql, config.tableName)
				config.save(failOnError:true)
			}
		}
	}
	
	
	def crearConfiguracion(def name,def table,def ds){
		SimpleJdbcInsert insert=new SimpleJdbcInsert(ds).withTableName(table)
		def sql=new Sql(ds)
		insert.compile()
		def config=new EntityConfiguration(name:name,tableName:table)
		config.updateSql=resolverUdateQuery(sql,config.tableName)
		config.insertSql=insert.getInsertString()
		config.pk=resolverPk(sql, config.tableName)
		if("SX_VENTAS"==table){
			config.excludeInsertColumns="PEDIDO_ID"
			config.excludeUpdateColumns="PEDIDO_ID"
		}
		config.save(failOnError:true)
	}
	
	//def destinos
	
	def getDestinos(){
		/*
		if(!destinos){
			log.debug 'Cargando destinos'
			destinos=Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS').collect({it.nombre})
		}
		return destinos
		*/
		return  Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS').collect({it.nombre})
	}
}
