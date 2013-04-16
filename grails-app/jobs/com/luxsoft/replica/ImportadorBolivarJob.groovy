package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class ImportadorBolivarJob {
	
	def concurrent = false
	def replicaService
	
	
	def group = "Importadores"
	
	 
    
	static triggers = {
      simple repeatInterval: 5000l // execute job once in 5 seconds
    }

    def execute(context) {
		
		//println context 
		def dataMap= context.mergedJobDataMap
		//JobDataMap dataMap = context.getJobDetail()
		//println dataMap
		int count = dataMap.errorCount?:0;
		//println count
		
		// allow 5 retries
		if(count >= 5){
			log.info 'Errores reportados por importador sobre pasa el limite, se parara el proceso:'
			JobExecutionException e = new JobExecutionException("Intentos excedidos "+dataMap.errorMessage);
			//make sure it doesn't run again
			e.setUnscheduleAllTriggers(true);
			throw e; 
		}
		
        def oficinas=Sucursal.findByNombre('OFICINAS')
		def sucursal=Sucursal.findOrSaveWhere(nombre:"BOLIVAR",dataSourceName	:'bolivarDataSource')
		log.debug "Importando  de ${sucursal?.dataSourceName} "+new Date();
		try {
			replicaService.importarAuditLog(sucursal.dataSourceName,oficinas.dataSourceName)
			dataMap.errorCount=0;
		} catch (Exception th) {
			def errorMessage=ExceptionUtils.getRootCauseMessage(th)
			count++;
			dataMap.errorCount=count;
			dataMap.errorMessage=errorMessage
			JobExecutionException e2 = new JobExecutionException(errorMessage);
			//println 'Pausa en error: '+ExceptionUtils.getRootCauseMessage(th)
			Thread.sleep(3000); //sleep for some time
			//	fire it again
			e2.setRefireImmediately(true);
			throw e2;
		}
		
    }
}
