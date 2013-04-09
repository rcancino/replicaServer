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
		def tacuba=Sucursal.findOrSaveWhere(nombre:"TACUBA",dataSourceName:'tacubaDataSource')
		def oficinas=Sucursal.findOrSaveWhere(nombre:'OFICINAS',dataSourceName:'oficinasDataSource')
		def andrade=Sucursal.findOrSaveWhere(nombre:"ANDRADE",dataSourceName:'andradeDataSource')
		
		
    }
	
    def destroy = {
    }
	
	//def insertarReplicaConfig(){}
}
