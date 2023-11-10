package Lab6;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;

public interface CRUD extends Remote{
	public Connection connectMysql() throws RemoteException;
	public void add( String name) throws RemoteException;
	public String getData() throws RemoteException;
}
