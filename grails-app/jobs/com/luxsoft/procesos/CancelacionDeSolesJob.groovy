package com.luxsoft.procesos



class CancelacionDeSolesJob {
	
	def concurrent = false
	
	def cancelacionDeSolesService
	
    static triggers = {
      //simple name:'PROC_ATRASO_MAX',repeatCount:0,startDelay:5000L
	  cron name:'_CANCELACION SOLES', cronExpression: '0 0 21 * * ?'
	//simple name:'CANCELACION SOLES',startDelay:60000l, repeatInterval: 10000l
    }

    def execute() {
		println 'Buscando Soles Para eliminar'
		cancelacionDeSolesService.borrarSolesOrigen()
    }
}
