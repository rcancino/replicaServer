package com.luxsoft.replica



class ImportadorAndradeJob {
	
	def concurrent = false
	def dataSourceLookup
	def replicaService
	
	def group = "IMPORTADORES"
	
    static triggers = {
		simple name:'ANDRADE_TRG', startDelay:3000l,repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute() {
		def oficinas=Sucursal.findByNombre('OFICINAS')
		def sucursal=Sucursal.findOrSaveWhere(nombre:"ANDRADE",dataSourceName:'andradeDataSource')
		//log.debug("Importacion de ${sucursal.dataSourceName} a ${oficinas.dataSourceName} "+new Date())
		replicaService.importarAuditLog(sucursal.dataSourceName,oficinas.dataSourceName)
		/*
		try{
			replicaService.exportarAuditLog(oficinas,sucursal)
		}catch(Exception th){
			def msg="Error exportand a tacuba "+ExceptionUtils.getRootCauseMessage(th)
			log.info(msg)
			log.error(msg,ExceptionUtils.getRootCause(th))
		}*/
		//replicaService.importarAuditLog(origen,destino)
    }
}
