package test.sandbox;

import java.lang.reflect.ReflectPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

public class SandboxSecurityPolicy extends Policy {

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        if (isPlugin(domain)) {
            return pluginPermissions();
        }
        else {
            return applicationPermissions();
        }
    }


    private boolean isPlugin(ProtectionDomain domain) {
        return domain.getClassLoader() instanceof PluginClassLoader;
    }

    private PermissionCollection pluginPermissions() {
        Permissions permissions = new Permissions();
        return permissions;
    }

    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new ReflectPermission("suppressAccessChecks"));
        permissions.add(new RuntimePermission("modifyThread"));
        permissions.add(new RuntimePermission("getStackTrace"));
        return permissions;
    }
}