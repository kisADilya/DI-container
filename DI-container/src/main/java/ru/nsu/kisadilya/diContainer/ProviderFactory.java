package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class ProviderFactory {

    public static <T> Provider<T> createProvider(Class<T> clazz, Object[] args) {
        return (Provider<T>) Proxy.newProxyInstance(
                Provider.class.getClassLoader(),
                new Class[]{Provider.class},
                new ProviderInvocationHandler<>(clazz, args)
        );
    }

    private static class ProviderInvocationHandler<T> implements InvocationHandler {
        private final Class<T> clazz;
        private final Object[] args;

        public ProviderInvocationHandler(Class<T> clazz, Object[] args) {
            this.clazz = clazz;
            this.args = args;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] methodArgs) throws Throwable {
            if (method.getName().equals("get")) {
                return createInstance(clazz, args);
            }
            return method.invoke(proxy, methodArgs);
        }
    }

    private static <T> T createInstance(Class<T> clazz, Object[] args) throws Exception {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == args.length) {
                return (T) constructor.newInstance(args);
            }
        }
        throw new IllegalArgumentException("No suitable constructor found for " + clazz.getName());
    }
}
