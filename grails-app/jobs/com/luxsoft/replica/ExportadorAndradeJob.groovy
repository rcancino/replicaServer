package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;



class ExportadorAndradeJob {
	
	def concurrent = true
	def replicaService
	
	
	def group = "Replica-Exportadores"
	
    static triggers = {
		simple name:'Exportacion_Andrade',repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
		def oficinas=Sucursal.findByNombre('OFICINAS')
		def sucursal=Sucursal.findOrSaveWhere(nombre:"ANDRADE",dataSourceName:'andradeDataSource')
		//log.debug("Exportacion de ${oficinas.dataSourceName} a ${sucursal.dataSourceName} "+new Date())
		
		replicaService.exportarAuditLog(oficinas,sucursal)
		/*
		try{
			replicaService.exportarAuditLog(oficinas,sucursal)
		}catch(Exception th){
			def msg="Error exportand a tacuba "+ExceptionUtils.getRootCauseMessage(th)			
			log.info(msg)
			log.error(msg,ExceptionUtils.getRootCause(th))
		}
		*/  
    }
}
