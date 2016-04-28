
package com.jee.async.common.handler ;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jee.async.common.annotation.RequestJsonParam;
import com.jee.async.common.util.MapWapper;


public class RequestJsonParamHanlder implements HandlerMethodArgumentResolver{

    private ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestJsonParam.class)) {
            return true;
        }
        return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		RequestJsonParam info = parameter.getParameterAnnotation(RequestJsonParam.class);
		if(null == info || StringUtils.isBlank(info.value())){
			
		}
		String name = info.value() ;
		String[] paramValues = webRequest.getParameterValues(name);
		Class<?> paramType = parameter.getParameterType();
        if (paramValues == null) {
            return null;
        } 
        
        try {
            if(paramValues.length == 1) {
                String text = paramValues[0]; 
                Type type = parameter.getGenericParameterType();

                if(MapWapper.class.isAssignableFrom(paramType)) {
                    MapWapper<?, ?> jsonMap = (MapWapper<?, ?>) paramType.newInstance();
                    
                    MapType mapType = (MapType) getJavaType(HashMap.class);
                    
                    if(type instanceof ParameterizedType) {
                        mapType = (MapType) mapType.narrowKey((Class<?>)((ParameterizedType)type).getActualTypeArguments()[0]);
                        mapType = (MapType) mapType.narrowContentsBy((Class<?>)((ParameterizedType)type).getActualTypeArguments()[1]); 
                    }
                    jsonMap.setInnerMap(mapper.<Map>readValue(text, mapType));
                    return jsonMap;
                }
                
                JavaType javaType = getJavaType(paramType);


                if(Collection.class.isAssignableFrom(paramType)) {
                    javaType = javaType.narrowContentsBy((Class<?>)((ParameterizedType)type).getActualTypeArguments()[0]);                        
                }

                return mapper.readValue(paramValues[0], javaType);
            }
            
        } catch (Exception e) {
            throw new JsonMappingException("Could not read request json parameter", e);
        }

        throw new UnsupportedOperationException(
                "too many request json parameter '" + name + "' for method parameter type [" + paramType + "], only support one json parameter");
	}
    
	protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.type(clazz);
    }
}