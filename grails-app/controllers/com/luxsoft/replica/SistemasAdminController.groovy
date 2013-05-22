package com.luxsoft.replica

import grails.validation.Validateable;

/**
 * SistemasAdminController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class SistemasAdminController {

	
	def index = { 
		
	}
	
	
}

@Validateable
class SucursalPeriodoCommand{
	
	Sucursal sucursal
	Date fechaInicial
	Date fechaFinal
	
}
