/**
 *
 */
package org.alessios18.jserversmanager.datamodel.wrapper;

import org.alessios18.jserversmanager.baseobjects.Server;

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
