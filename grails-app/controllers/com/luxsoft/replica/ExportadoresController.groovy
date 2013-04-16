package com.luxsoft.replica

/**
 * ExportadoresController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class ExportadoresController {
	
	def sincronizacionService

	def index(){
		
	}
	
	def exportarClientes(){
		def fecha=params.fecha?:new Date()
		sincronizacionService.exportarClientesFaltantes(fecha);
	}

}
