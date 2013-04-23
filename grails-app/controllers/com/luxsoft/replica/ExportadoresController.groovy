package com.luxsoft.replica

import groovy.sql.Sql

/**
 * ExportadoresController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ExportadoresController {
	
	def sincronizacionService
	def dataSourceLookup

	def index(){
		
	}
	
	def exportarClientes(){
		def ds=dataSourceLookup.getDataSource("tacubaDataSource")
		def sql=new Sql(ds)
		//def rows=sql.rows("select fecha,DOCTO,clave,nombre from sx_ventas where date(fecha)=?",[new Date()], 0, 10)
		
		render sql.firstRow("select * from sx_ventas where cargo_id=?",['8a8a8199-3e188574-013e-18a6098b-0006'])
		//def fecha=params.fecha?:new Date()
		//sincronizacionService.exportarClientesFaltantes(fecha);
	}
	
	def pruebas(){
		def ds=dataSourceLookup.getDataSource("tacubaDataSource")
		def sql=new Sql(ds)
		def rows=sql.rows("select fecha,DOCTO,clave,nombre from sx_ventas where date(fecha)=?",[new Date()], 0, 10)
		render rows
	}

}
