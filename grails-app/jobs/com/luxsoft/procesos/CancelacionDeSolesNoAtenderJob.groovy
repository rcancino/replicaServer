package com.luxsoft.procesos



class CancelacionDeSolesNoAtenderJob {
	
	def concurrent = false
	
	def cancelacionDeSolesNoAtenderService
	
    static triggers = {
      //simple name:'PROC_ATRASO_MAX',repeatCount:0,startDelay:5000L
	 // cron name:'CANCELACION SOLES NO ATENDER', cronExpression: '0 20,40,60 * * * ?'
	simple name:'CANCELACION SOLES',startDelay:60000l, repeatInterval: 1200000l
    }

    def execute() {
		println 'Buscando Soles Etiquetados Para eliminar'
		cancelacionDeSolesNoAtenderService.borrarSolesOrigen()
    }
}
