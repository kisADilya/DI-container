package ru.nsu.kisadilya.diContainer;

import jakarta.inject.Inject;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ThreadFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createThreadScopedBean(Class<T> clazz, List<Object> arguments) throws Exception {
        ThreadLocal<T> threadLocalInstance = ThreadLocal.withInitial(() -> {
            try {
                List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                        .toList();

                if (constructors.size() > 1) {
                    throw new RuntimeException("Class " + clazz.getName()
                            + " should have exactly one constructor");
                }
                if (constructors.isEmpty()) {
                    return (T) clazz.getConstructors()[0].newInstance();
                }
                constructors.getFirst().setAccessible(true);

                return (T) constructors.getFirst().newInstance(arguments);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return new ByteBuddy()
                .subclass(clazz)
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object instance = threadLocalInstance.get();
                        return method.invoke(instance, args);
                    }
                }))
                .make()
                .load(ThreadFactory.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
