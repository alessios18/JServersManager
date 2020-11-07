/**
 *
 */
package org.alessios18.jserversmanager.baseobjects.serverdata.wrapper;

import org.alessios18.jserversmanager.baseobjects.serverdata.Server;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/** @author alessio */
@XmlRootElement(name = "servers")
public class ServersDataWrapper {
	 private List<Server> servers;

	 @XmlElement(name = "server")
	 public List<Server> getServers() {
		  return servers;
	 }

	 public void setServers(List<Server> servers) {
		  this.servers = servers;
	 }
}
