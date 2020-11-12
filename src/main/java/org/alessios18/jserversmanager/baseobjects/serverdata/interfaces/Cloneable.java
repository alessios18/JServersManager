package org.alessios18.jserversmanager.baseobjects.serverdata.interfaces;

public interface Cloneable<T> {
  T getCloneObject();

  void setFromSameObject(T clone);
}
