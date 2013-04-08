package com.luxsoft.replica



class ImportadorTacubaJob {
	
	def concurrent = false
	def dataSourceLookup
	def replicaService
	
	def group = "Replica-Importadores"
	
    static triggers = {
		simple name:'Tacuba-Normal', repeatInterval: 5000l // execute job once in 5 seconds
      //simple name:'importadorDeTacubaTrigger',startDelay:3000l,repeatInterval: 5000l,repeatCount:-1 // execute job once in 5 seconds
	  //simple name:'simpleTrigger', startDelay:10000, repeatInterval: 30000, repeatCount: 10
		
    }

    def execute() {
		
		def origen='tacubaDataSource'
		def destino='oficinasDataSource'
		
		//println "Importacion de ${origen} a ${destino} "+new Date()
		log.debug "Importacion de ${origen} a ${destino} "+new Date()
		replicaService.importarAuditLog(origen,destino)
    }
}
