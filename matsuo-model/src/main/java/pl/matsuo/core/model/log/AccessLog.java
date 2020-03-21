package pl.matsuo.core.model.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import pl.matsuo.core.model.AbstractEntity;

/** Created by tunguski on 22.01.14. */
@Entity
public class AccessLog extends AbstractEntity {

  @Column(length = 512)
  String request;

  String method;

  @Column(length = 4096)
  String parameters;

  String ip;
  Integer status;
  Integer idUser;

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getIdUser() {
    return idUser;
  }

  public void setIdUser(Integer idUser) {
    this.idUser = idUser;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }
}
