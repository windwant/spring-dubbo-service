package org.windwant.registry;

/**
 * Created by Administrator on 18-4-9.
 */
public interface Registry {

    String doRegister(RegistryService service);

    void doUnRegister(String id);
}
