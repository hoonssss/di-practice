package org.example.di;

import java.lang.reflect.Constructor;
import java.util.Set;
import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

public class BeanFactoryUtils {

    //클래스타입의 모든 clazz타입 객체를 가져옴 단 Inject Annotation이 붙은거만
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectConstructors = ReflectionUtils.getAllConstructors(clazz,
            ReflectionUtils.withAnnotation(Inject.class));

        if (injectConstructors.isEmpty()) {
            return null;
        }
        return injectConstructors.iterator().next();
    }
}
