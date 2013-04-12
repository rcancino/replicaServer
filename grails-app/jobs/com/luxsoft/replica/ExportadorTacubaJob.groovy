package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;



class ExportadorTacubaJob {
	
	def concurrent = false
	def replicaService
	
	def group = "Replica-Exportadores"
	
    static triggers = {
		simple name:'Exportacion_Tacuba',repeatInterval: 5000l // execute job once in 5 seconds
      
    }

    def execute() {
		
		def oficinas=Sucursal.findByNombre('oficinas')
		def sucursal=Sucursal.findByNombre('tacuba')
		//log.debug("Exportacion de ${oficinas.dataSourceName} a ${sucursal.dataSourceName} "+new Date())
		
		try{
			replicaService.exportarAuditLog(oficinas,sucursal)
		}catch(Exception th){
			def msg="Error exportand a tacuba "+ExceptionUtils.getRootCauseMessage(th)			
			log.info(msg)
			log.error(msg,ExceptionUtils.getRootCause(th))
		}
		
    }
}
