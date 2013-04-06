import com.luxor.security.*
import com.luxsoft.replica.Sucursal
import com.luxsoft.replica.*

class BootStrap {

    def init = { servletContext ->
    	development{
    		def admin=User.findByUsername('admin')
			if(!admin){
					admin=new User(username:"admin"
						,password:"admin"
						,enabled:true).save(flush:true);
			}
    	}

    	production{
    		def admin=User.findByUsername('admin')
			if(!admin){
				admin=new User(username:"admin"
				,password:"siipapweb"
				,enabled:true).save(flush:true);
			}
    	}
		def tacuba=Sucursal.findOrSaveWhere(nombre:"tacuba",dataSourceName:'tacubaDataSource')
		def oficinas=Sucursal.findOrSaveWhere(nombre:'oficinas',dataSourceName:'oficinasDataSource')
		
		
    }
	
    def destroy = {
    }
	
	//def insertarReplicaConfig(){}
}
