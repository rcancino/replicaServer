package com.luxsoft.replica

import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class Calle4ImportadorJob {
	
	def concurrent = false
	def dataSourceLookup
	def replicaService
	
	def group = "CALLE4-REPLICA"
	static sucursalName="CALLE4"
	
	static triggers = {
		simple name:sucursalName+'-IMPORTADOR',startDelay:6000l, repeatInterval: 10000l // execute job once in 5 seconds
	  //simple name:'importadorDeTacubaTrigger',startDelay:3000l,repeatInterval: 5000l,repeatCount:-1 // execute job once in 5 seconds
	  //simple name:'simpleTrigger', startDelay:10000, repeatInterval: 30000, repeatCount: 10
		
	}

	def execute(context) {
		//println context
		def dataMap= context.mergedJobDataMap
		int count = dataMap.errorCount?:0;
		
		// allow 5 retries
		if(count >= 5){
			log.info 'Errores reportados por importador sobre pasa el limite, se parara el proceso:'
			JobExecutionException e = new JobExecutionException("Intentos excedidos "+dataMap.errorMessage);
			//make sure it doesn't run again
			e.setUnscheduleAllTriggers(true);
			throw e;
		}
		
		def oficinas=Sucursal.findByNombre('OFICINAS')
		def sucursal=Sucursal.findByNombre(sucursalName)
		if(!sucursal){
			JobExecutionException e = new JobExecutionException("No esta dada de alta la sucursal: "+sucursalName);
			e.setUnscheduleAllTriggers(true);
			throw e;
		}
			
		//println "Importando  de ${sucursal?.dataSourceName} "+new Date();
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
