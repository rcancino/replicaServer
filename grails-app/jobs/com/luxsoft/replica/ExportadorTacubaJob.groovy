package com.luxsoft.replica



class ExportadorJob {
	
	def concurrent = false
	def dataSourceLookup
	def replicaService
	def log
	def group = "Replica-Importadores"
	
    static triggers = {
      //simple name:'importadorDeTacubaTrigger',startDelay:3000l,repeatInterval: 5000l // execute job once in 5 seconds
	  //simple name:'simpleTrigger', startDelay:10000, repeatInterval: 30000, repeatCount: 10
    }

    def execute() {
		def oficinas=Sucursal.findByNombre('oficinas')
		def sucursal=Sucursal.findByNombre('tacuba')
		println "Exportando registros de ${oficinas.dataSourceName} a ${sucursal.dataSourceName}"  
    }
}
