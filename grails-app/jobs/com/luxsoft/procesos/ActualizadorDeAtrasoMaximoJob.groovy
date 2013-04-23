package com.luxsoft.procesos



class ActualizadorDeAtrasoMaximoJob {
	
	def concurrent = false
	
	def procesosProgramadosService
	
    static triggers = {
      //simple name:'PROC_ATRASO_MAX',repeatCount:0,startDelay:5000L
	  cron name:'_PROC_ATRASO_MAX', startDelay:10000, cronExpression: '0 30 1 * * ?'
    }

    def execute() {
		println 'Actualizando atraso maximo'
		procesosProgramadosService.actualizarAtrasoMaximo()
    }
}
