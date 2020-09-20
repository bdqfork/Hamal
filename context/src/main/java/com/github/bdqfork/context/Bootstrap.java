package com.github.bdqfork.context;

import com.github.bdqfork.core.Node;
import com.github.bdqfork.core.URL;
import com.github.bdqfork.core.constant.ProtocolProperty;
import com.github.bdqfork.core.exception.ConfilictServiceException;
import com.github.bdqfork.core.extension.ExtensionLoader;
import com.github.bdqfork.rpc.Invoker;
import com.github.bdqfork.rpc.InvokerFactory;
import com.github.bdqfork.context.config.ApplicationConfig;
import com.github.bdqfork.context.config.ReferenceConfig;
import com.github.bdqfork.context.config.RegistryConfig;
import com.github.bdqfork.context.config.ServiceConfig;
import com.github.bdqfork.rpc.container.ServiceContainer;
import com.github.bdqfork.rpc.protocol.Protocol;
import com.github.bdqfork.rpc.registry.exporter.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bdq
 * @since 2020/2/27
 */
public class Bootstrap {
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
    private Protocol protocol;
    private ApplicationConfig applicationConfig;
    private ServiceContainer serviceContainer;
    private Exporter exporter;

    public Bootstrap(ApplicationConfig applicationConfig) {
        this(applicationConfig, null);
    }

    public Bootstrap(ApplicationConfig applicationConfig, RegistryConfig registryConfig) {
        init(applicationConfig);
        if (registryConfig != null) {
            URL exportUrl = new URL(ProtocolProperty.EXPORTER, "127.0.0.1", 0, "rpc");
            exportUrl.addParam(ProtocolProperty.REGISTRY, registryConfig.toURL());
            this.exporter = protocol.export(exportUrl);
        }
    }

    private void init(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.protocol = ExtensionLoader.getExtensionLoader(Protocol.class)
                .getExtension(applicationConfig.getProtocol());
        this.serviceContainer = ExtensionLoader.getExtensionLoader(ServiceContainer.class)
                .getExtension(applicationConfig.getContainer());
    }

    public void open() {
        try {
            protocol.open(applicationConfig.toURL());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        if (log.isInfoEnabled()) {
            log.info("open protocol successful!");
        }
    }

    public void close() {
        serviceContainer.getAll().values().forEach(Node::destroy);
        if (log.isInfoEnabled()) {
            log.info("destroyed all service!");
        }
        protocol.destroy();
        if (log.isInfoEnabled()) {
            log.info("closed context!");
        }
    }

    public <T> T getProxy(ReferenceConfig<T> referenceConfig) {
        URL url = referenceConfig.toURL();
        url.addParam(ProtocolProperty.LOAD_BALANCER, applicationConfig.getLoadbalancer());
        url.addParam(ProtocolProperty.SERIALIZER, applicationConfig.getSerilizer());
        if (applicationConfig.isDirect()) {
            url.addParam(ProtocolProperty.DIRECT, true);
            url.addParam(ProtocolProperty.HOST, applicationConfig.getHost());
            url.addParam(ProtocolProperty.PORT, applicationConfig.getPort());
            url.addParam(ProtocolProperty.SERVER, applicationConfig.getServer());
            url.addParam(ProtocolProperty.PROTOCOL, applicationConfig.getProtocol());
        }
        Invoker<T> invoker = protocol.refer(referenceConfig.getServiceInterface(), url);
        if (!applicationConfig.isDirect()) {
            exporter.export(invoker);
        }
        return InvokerFactory.getProxy(invoker);
    }

    public <T> void registerService(T instance, ServiceConfig<T> serviceConfig) {
        serviceConfig.setHost(applicationConfig.getHost());
        serviceConfig.setPort(applicationConfig.getPort());
        Class<T> serviceInterface = serviceConfig.getServiceInterface();
        URL serviceUrl = serviceConfig.toURL();
        serviceUrl.addParam(ProtocolProperty.PROTOCOL, applicationConfig.getProtocol());
        serviceUrl.addParam(ProtocolProperty.SERIALIZER, applicationConfig.getSerilizer());
        Invoker<T> invoker = InvokerFactory.getServiceInvoker(instance, serviceInterface, serviceUrl);
        if (!applicationConfig.isDirect()) {
            exporter.export(invoker);
        }
        try {
            serviceContainer.regsiter(serviceInterface.getCanonicalName(), invoker);
        } catch (ConfilictServiceException e) {
            throw new IllegalStateException(e);
        }
        if (log.isInfoEnabled()) {
            log.info("register service {}!", serviceInterface.getCanonicalName());
        }
    }

}
