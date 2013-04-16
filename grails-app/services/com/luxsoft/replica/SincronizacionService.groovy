package com.luxsoft.replica

import groovy.sql.Sql

/**
 * SincronizacionService
 * A service class encapsulates the core business logic of a Grails application
 */
class SincronizacionService {

    static transactional = false
	def dataSourceLookup
	
	def exportacionService
	
	def destinos
	
	def entityConfigurationService

    def exportarClientesFaltantes(def fecha){
		log.debug "Exportando clientes modificados el: $fecha"
		def dataSource=dataSourceLookup.getDataSource("oficinasDataSource")
		
		def sourceSql=new Sql(dataSource)
		def sql="""
			select * from sx_clientes where clave in(select distinct(clave) from sx_clientes where date(modificado)=?)
		"""
		def rows=sourceSql.rows(sql, [fecha])
		def sucursales=Sucursal.findAllByActivaAndNombreNotEqual('true','OFICINAS')
		sucursales.each{sucursal->
			
			def targetSql=new Sql(dataSourceLookup.getDataSource(sucursal.dataSourceName))
			def config=entityConfigurationService.crearConfiguracion('Cliente', 'SX_CLIENTES',targetSql.dataSource)
			exportacionService.exportar(sourceSql, targetSql, config, rows)
			
		}
		
	}
	
	
	
}
