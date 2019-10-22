package au.gov.api.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URLDecoder

import au.gov.api.config.*

import khttp.get
import khttp.structures.authorization.BasicAuthorization

@Controller
class ConsoleController {

    private val log = LoggerFactory.getLogger(this.javaClass)

   @RequestMapping("/console")
    fun services(model:MutableMap<String, Any?>): String{

        model.put("has_boostrap_credentials", has_boostrap_credentials())
        if(has_boostrap_credentials()){
            val registrationURL = Config.get("AuthURI") + "api/new?email=admin@localhost&spaces=admin" 
            val bootstrapCredentials = System.getenv("BootstrapCredentials")
            var admin_key = khttp.get(registrationURL,auth=BasicAuthorization(bootstrapCredentials.split(":")[0], bootstrapCredentials.split(":")[1])).text
            model.put("admin_key", admin_key)
        }
        return "console"

    }

    private fun has_boostrap_credentials():Boolean{
        val bootstrapCredentials = System.getenv("BootstrapCredentials")
        if(bootstrapCredentials == null) return false
        if(bootstrapCredentials == "") return false
        return true
    }
}
