package com.luxsoft.procesos



class ActualizadorVentasPorFacturistaJob {
	
	def procesosProgramadosService
	def concurrent = false
	
     static triggers = {
      //simple name:'PROC_VTAS_FACT',repeatCount:0,startDelay:5000L
	  cron name:'PROC_VTAS_FACT', startDelay:10000, cronExpression: '0 30 2 * * ?'
    }

    def execute() {
		println 'Actualizando ventas por facturista'
		procesosProgramadosService.actualizarVentasPorFacturista(null)
    }
}
