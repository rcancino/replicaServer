package com.luxsoft.procesos



class CancelacionDeSolesNoAtenderJob {
	
	def concurrent = false
	
	def cancelacionDeSolesNoAtenderService
	
    static triggers = {
      //simple name:'PROC_ATRASO_MAX',repeatCount:0,startDelay:5000L
	  cron name:'CANCELACION SOLES NO ATENDER', startDelay:10000, cronExpression: '0 0/20 * * * ?'
	 // simple name:'CANCELACION SOLES',startDelay:60000l, repeatInterval: 12000000l
	//	simple name:'CANCELACION SOLES',startDelay:6000l, repeatInterval: 6000l
	  
	  
    }

    def execute() {
		println 'Buscando Soles Etiquetados Para eliminar'
		cancelacionDeSolesNoAtenderService.borrarSolesOrigen()
    }
}
