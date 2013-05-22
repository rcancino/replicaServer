package com.luxsoft.replica

import groovy.sql.Sql

/**
 * SolicitudDeDepositosController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class SolicitudDeDepositosController {
	
	static defaultAction = "list"
	
	def dataSourceLookup
	
	def list(){
		def cmd=params.sucursalPeriodoCommand
		if(!cmd){
			def date=new Date()
			cmd=new SucursalPeriodoCommand(sucursal:Sucursal.findByNombre('OFICINAS'),fechaInicial:date,fechaFinal:date)
		}
		def ds=dataSourceLookup.getDataSource(cmd.sucursal.dataSourceName)
		def sql =new Sql(ds)
		def rows=sql.rows("select * from sx_solicitudes_deposito where date(fecha) between ? and ?",[cmd.fechaInicial,cmd.fechaFinal] ,0,500)
		[rows:rows,cmd:cmd,rowsTotal:0]
	}
	
	def cambiarSucursalPeriodo(){
		println 'Cambiando periodo: '+params
	}
	
	def mostrarSolicitudes(SucursalPeriodoCommand sucursalPeriodo){
		
	}
	
	
}
