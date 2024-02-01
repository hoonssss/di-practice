package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BeanFactory {

    private Map<Class<?>, Object> been = new HashMap<>();
    private final Set<Class<?>> preInstantiatedClass;

    public BeanFactory(Set<Class<?>> preInstantiatedClass) {
        this.preInstantiatedClass = preInstantiatedClass;
        initialize();
    }

    ///been 초기화
    private void initialize() {
        for (Class<?> clazz : preInstantiatedClass){
            Object instance = createInstance(clazz);
            been.put(clazz,instance);
        }
    }

    private Object createInstance(Class<?> clazz) {
        //생성자
        Constructor<?> constructor = findConstructor(clazz);

        //파라미터 정보
        List<Object> parameters = new ArrayList<>();
        for(Class<?> typeClass : constructor.getParameterTypes()){
            parameters.add(getParameterByClass(typeClass));
        }

        //인스턴스 생성
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if(Objects.nonNull(constructor)){
            return constructor;
        }

        return clazz.getConstructors()[0];
    }

    private Object getParameterByClass(Class<?> typeClass) {
        Object instanceBean = getBean(typeClass);
        if(Objects.nonNull(instanceBean)){
            return instanceBean;
        }

        return createInstance(typeClass);
    }

    public <T> T getBean(Class<T> requiredType) {
        return (T) been.get(requiredType);
    }
}
