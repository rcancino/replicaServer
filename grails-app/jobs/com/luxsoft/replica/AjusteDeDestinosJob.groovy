package com.luxsoft.replica

import com.luxsoft.replica.AuditLog;
import groovy.sql.Sql


class AjusteDeDestinosJob {
	
	def dataSourceLookup
	
	def concurrent = false
	
	static transactional = false
	
    static triggers = {
      simple repeatInterval: 15000l // execute job once in 5 seconds
    }

    def execute() {
		def pendientes=AuditLog.findAllByEntityNameAndSucursalDestino('AutorizacionDeAbono','ND')
		pendientes.each{audit->
			println 'Ajustando :'+audit
			def ds=dataSourceLookup.getDataSource('oficinasDataSource')
			def sql=new Sql(ds)
			def destino=sql.firstRow("select z.nombre from sx_cxc_abonos x join sw_sucursales z on x.sucursal_id=z.sucursal_id  where x.AUTORIZACION_ID=?"
					,[audit.entityId])
			println '********* DESTINO: '+destino
			if("CALLE 4".trim()==destino?.nombre) 
				audit.sucursalDestino='CALLE4'
			else if("VERTIZ 176"==destino?.nombre){
				destino="VERTIZ"
				audit.sucursalDestino='VERTIZ'
			}else{
				audit.sucursalDestino=destino?.nombre
			}
			audit.save(flush:true)
		}
       ajusteCalle4()
	   ajusteVertiz()
    }
	
	private ajusteCalle4(){
		def errores=AuditLog.findAllBySucursalDestino('CALLE 4')
		errores.each{audit->
			println 'Ajustando :'+audit
			audit.sucursalDestino='CALLE4'
		}
	}
	
	private ajusteVertiz(){
		def errores=AuditLog.findAllBySucursalDestino('VERTIZ 176')
		errores.each{audit->
			println 'Ajustando :'+audit
			audit.sucursalDestino='VERTIZ'
		}
	}
}
