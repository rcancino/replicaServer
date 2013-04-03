import com.luxor.security.*

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
    }
	
    def destroy = {
    }
	
	//def insertarReplicaConfig(){}
}
