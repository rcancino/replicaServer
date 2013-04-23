package com.luxsoft.procesos



class ActualizadorVentasPorFacturistaJob {
	
	def procesosProgramadosService
	
     static triggers = {
      //simple name:'PROC_ATRASO_MAX',repeatCount:0,startDelay:5000L
	  cron name:'PROC_VTAS_FACT', startDelay:10000, cronExpression: '0 30 2 * * ?'
    }

    def execute() {
		println 'Actualizando ventas por facturista'
		procesosProgramadosService.actualizarVentasPorFacturista(null)
    }
}
