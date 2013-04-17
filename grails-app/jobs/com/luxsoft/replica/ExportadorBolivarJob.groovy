package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class ExportadorBolivarJob {
	
	def concurrent = false
	def replicaService
	
	
	def group = "Exportadores"
	
	 
    
	static triggers = {
      simple name:'Bolivar-Exp',repeatInterval: 5000l, startDelay:10000 // execute job once in 5 seconds
    }

    def execute(context) {
		
		
		def dataMap= context.mergedJobDataMap
		int count = dataMap.errorCount?:0;
		
		// allow 5 retries
		if(count >= 5){
			log.info 'Errores reportados por exportador sobre pasa el limite, se parara el proceso:'
			JobExecutionException e = new JobExecutionException("Intentos excedidos "+dataMap.errorMessage);
			//make sure it doesn't run again
			e.setUnscheduleAllTriggers(true);
			throw e; 
		}
        def oficinas=Sucursal.findByNombre('OFICINAS')
		def sucursal=Sucursal.findOrSaveWhere(nombre:"BOLIVAR",dataSourceName	:'bolivarDataSource')
		log.debug "Exportando a ${sucursal?.dataSourceName} "+new Date();
		try { 
			//replicaService.exportarAuditLog(oficinas,sucursal)
			dataMap.errorCount=0;
		} catch (Exception th) {
			def errorMessage=ExceptionUtils.getRootCauseMessage(th)
			count++;
			dataMap.errorCount=count;
			dataMap.errorMessage=errorMessage
			JobExecutionException e2 = new JobExecutionException(errorMessage);
			Thread.sleep(3000); //sleep for some time
			//	fire it again
			e2.setRefireImmediately(true);
			throw e2;
		}
		
    }
}
