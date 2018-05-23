package org.windwant.common.util;

import java.io.IOException;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

/**
 * RMI ∑˛ŒÒ◊¢≤·
 * Created by Administrator on 18-5-17.
 */
public class RMIUtils {

    private RMIUtils() {
    }

    private static RMIClientSocketFactory rmiClientSocketFactory;

    private static RMIClientSocketFactory getRmiClientSocketFactory() {
        if (rmiClientSocketFactory == null) {
            synchronized (RMIUtils.class) {
                rmiClientSocketFactory = new RMIClientSocketFactory() {
                    @Override
                    public Socket createSocket(String host, int port) throws IOException {
                        return new Socket(host, port);
                    }
                };
            }
        }
        return rmiClientSocketFactory;
    }

    /**
     * Ê≥®ÂÜåRMIÊúçÂä°
     *
     * @param host
     * @param port
     * @param impl
     * @throws RemoteException
     */
    public static String registerRMIService(String host, int port, Remote impl) throws RemoteException {
        Registry registry = null;
        registry = LocateRegistry.getRegistry(host, port);
        if (!testRegister(registry)) {
            registry = LocateRegistry.getRegistry(port);
            if (!testRegister(registry)) {
                registry = LocateRegistry.createRegistry(port);
            }
        }
        String url = null;
        if (registry != null) {
            try {
                url = impl.getClass().getInterfaces()[0].getName();
                registry.rebind(url, impl);
                url = NetUtil.formatRMIServiceName(host, port, url);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    private static boolean testRegister(Registry registry) {
        try {
            registry.list();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
