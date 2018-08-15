package com.ewing.order.core.cache.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.ewing.order.core.cache.AbstractCache;
import com.ewing.order.core.cache.annotation.BaseInfoCache;
import com.ewing.order.core.redis.CacheConfig;
import com.ewing.order.core.redis.CacheLogger;
import com.ewing.order.util.ReflectUtil;
import com.ewing.order.util.SpringContextHolderUtil;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 基础信息缓存的AOP类
 * 
 * @author tansonlam 2016年7月13日
 * 
 */
@Aspect
@Component
public class BaseInfoCacheAdvice {

	protected final static Logger logger = CacheLogger.logger;
	private static final String[] SHOPID_FIELDNAME = { "shopId" };
	private static final String[] ENTITYID_FIELDNAME = { "id", "ids" };
	private static final String SPLIT_SIGN = "&";

	@Around("(execution(* com.ewing.order.busi.*.dao.*.insert*(..))  "
			+ " or execution(* com.ewing.order.busi.*.dao.*.add*(..)) "
			+ " or execution(* com.ewing.order.busi.*.dao.*.save*(..))  "
			+ " or execution(* com.ewing.order.busi.*.dao.*.update*(..))  "
			+ " or execution(* com.ewing.order.busi.*.dao.*.delete*(..)) ) "
			+ "&& (  @target(methodAnnotation)) or @annotation(methodAnnotation)")
	public Object aroundMethod(ProceedingJoinPoint pjd, BaseInfoCache methodAnnotation)
			throws Throwable {
		return doRound(pjd, methodAnnotation);
	}

	private Object doRound(ProceedingJoinPoint pjd, BaseInfoCache methodAnnotation)
			throws Throwable {
		if (CacheConfig.redisOpen) {

			BaseInfoCache cacheAnnotation = null;
			BaseInfoCache classAnnotation = pjd.getTarget().getClass()
					.getAnnotation(BaseInfoCache.class);
			if (null != classAnnotation)
				cacheAnnotation = classAnnotation;
			if (null != methodAnnotation)
				cacheAnnotation = methodAnnotation;

			if (cacheAnnotation == null)
				return pjd.proceed();

			String batchId = UUID.randomUUID().toString();
			// 分析方法名称
			InvokeInfo invokeInfo = analyseMethodInfo(pjd);
			// 缩短缓存时间
			shortCacheTime(invokeInfo, pjd, cacheAnnotation, batchId);
			// 执行原来的方法
			Object returnObj = pjd.proceed();
			// 更新缓存
			updateCache(invokeInfo, pjd, cacheAnnotation, batchId);
			return returnObj;
		} else {
			return pjd.proceed();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void shortCacheTime(InvokeInfo invokeInfo, final ProceedingJoinPoint pjd,
			final BaseInfoCache cacheAnnotation, String batchId) {
		AbstractCache cache = null;
		try {
			Class<AbstractCache> abstractCache = (Class<AbstractCache>) cacheAnnotation
					.cacheClass();
			String className = abstractCache.getSimpleName();
			String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
			cache = (AbstractCache) SpringContextHolderUtil.getBean(beanName);

			if (cache != null) {
				List<Long> entityIds = getEntityIds(invokeInfo, pjd,
						cacheAnnotation.entityIdFieldName(), false);
				if (entityIds != null) {
					cache.shortCacheTime(getShopId(invokeInfo, pjd,
							cacheAnnotation.shopIdFieldName(), false), entityIds, batchId);
				}
			}
		} catch (Exception e1) { 
			logger.error(e1.getMessage(), e1);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateCache(InvokeInfo invokeInfo, final ProceedingJoinPoint pjd,
			final BaseInfoCache cacheAnnotation, String batchId) {
		AbstractCache cache = null;
		try {
			Class<AbstractCache> abstractCache = (Class<AbstractCache>) cacheAnnotation
					.cacheClass();
			String className = abstractCache.getSimpleName();
			String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
			cache = (AbstractCache) SpringContextHolderUtil.getBean(beanName);
			if (cache != null) {
				cache.updateCache(
						getShopId(invokeInfo, pjd, cacheAnnotation.shopIdFieldName(),
								cacheAnnotation.needShopId()),
						getEntityIds(invokeInfo, pjd, cacheAnnotation.entityIdFieldName(), true),
						batchId);
			}
		} catch (Exception e1) { 
			logger.error(e1.getMessage(), e1);
		}
	}

	/**
	 * 分析方法的参数
	 * 
	 * @param pjd
	 */
	private InvokeInfo analyseMethodInfo(ProceedingJoinPoint pjd) {
		try {
			String className = pjd.getTarget().getClass().getName();
			MethodSignature signature = (MethodSignature) pjd.getSignature();
			Method method = signature.getMethod();
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(this.getClass()));
			CtClass ctClass = null;
			CtMethod ctMethod = null;
			/**
			 * 入参参数名称数组
			 */
			String[] paramNames;

			ctClass = pool.get(className);
			Class<?>[] paramTypes = method.getParameterTypes();
			List<CtClass> ctParamTypes = new ArrayList<CtClass>();
			for (Class<?> paramClass : paramTypes) {
				CtClass ctParamClass = pool.get(paramClass.getName());
				ctParamTypes.add(ctParamClass);
			}
			for (CtMethod classMethod : ctClass.getMethods()) {
				if (!classMethod.getName().equals(method.getName())) {
					continue;
				}
				CtClass[] methodParamTypes = classMethod.getParameterTypes();
				if (ReflectUtil.isSameParamTypes(
						ctParamTypes.toArray(new CtClass[ctParamTypes.size()]), methodParamTypes)) {
					ctMethod = classMethod;
					break;
				}
			}
			if (ctMethod == null)
				throw new NotFoundException("not found match method[" + method.getName() + "]");
			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);

			paramNames = new String[ctMethod.getParameterTypes().length];
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(i + pos);
			return new InvokeInfo(ctClass, ctMethod, paramNames);
		} catch (NotFoundException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取仓库ID
	 * 
	 * @param pjd
	 * @param shopIdFiledName
	 *            指定的仓库ID属性名称
	 * @return
	 */
	private Long getShopId(InvokeInfo invokeInfo, ProceedingJoinPoint pjd,
			String shopIdFiledName, boolean needShopId) {
		String[] searchFields = StringUtils.isEmpty(shopIdFiledName) ? SHOPID_FIELDNAME
				: new String[] { shopIdFiledName };

		for (String filedName : searchFields) {
			ParamValue paramValue = getOneParamVale(invokeInfo.getParamNames(), pjd, filedName,
					true);
			if (paramValue != null && paramValue.getValue() != null)
				return Long.valueOf(paramValue.getValue());
		}
		if (needShopId)
			logger.warn(
					"fail to found shopId ,method[" + invokeInfo.getCtMethod().getLongName()
							+ ",searchFields[" + Arrays.toString(searchFields) + "]");
		return null;
	}

	/**
	 * 获取更新对象主键ID
	 * 
	 * @param pjd
	 * @param primaryIdFieldName
	 *            指定的更新對象ID属性名称
	 * @return
	 */
	private List<Long> getEntityIds(InvokeInfo invokeInfo, ProceedingJoinPoint pjd,
			String primaryIdFieldName, Boolean needLog) {
		String[] searchFields = StringUtils.isEmpty(primaryIdFieldName) ? ENTITYID_FIELDNAME
				: new String[] { primaryIdFieldName };

		for (String filedName : searchFields) {
			ParamValue paramValue = getOneParamVale(invokeInfo.getParamNames(), pjd, filedName,
					false);
			if (paramValue != null && paramValue.getValue() != null) {
				String[] valueArr = paramValue.getValue().split(SPLIT_SIGN);
				List<Long> valueList = new ArrayList<Long>();
				for (String value : valueArr) {
					valueList.add(Long.valueOf(value));
				}
				return valueList;
			}
		}
		if (needLog)
			logger.warn("fail to found entityIds ,method[" + invokeInfo.getCtMethod().getLongName()
					+ ",searchFields[" + Arrays.toString(searchFields) + "] args:["
					+ args2String(pjd) + "]");
		return null;
	}

	private String args2String(ProceedingJoinPoint pjd) {
		StringBuffer sb;
		try {
			sb = new StringBuffer();
			Object[] args = pjd.getArgs();
			if (args == null || args.length == 0)
				return StringUtils.EMPTY;
			for (Object object : args) {
				sb.append(ToStringBuilder.reflectionToString(object));
			}
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
		return sb.toString();
	}

	/**
	 * 获取单个入参名称的参数值
	 * 
	 * @param pjd
	 * @param keyParamName
	 *            参数名称
	 * @return
	 */
	private ParamValue getOneParamVale(String[] paramNames, ProceedingJoinPoint pjd,
			String keyParamName, Boolean isSingleValue) {
		List<ParamValue> list = getMultiParamValues(paramNames, pjd, new String[] { keyParamName },
				isSingleValue);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * 获取多个入参名称的参数值
	 * 
	 * @param pjd
	 * @param keyParamNames
	 *            参数名称数组
	 * @param isSingleValue
	 *            在数组重复出现的属性是否只获取一次
	 * @return
	 */
	private List<ParamValue> getMultiParamValues(String[] paramNames, ProceedingJoinPoint pjd,
			String[] keyParamNames, Boolean isSingleValue) {
		if (paramNames == null || pjd.getArgs().length == 0)
			return Collections.emptyList();
		List<ParamValue> paramValues = new ArrayList<ParamValue>();
		Object[] args = pjd.getArgs();

		for (String name : keyParamNames) {
			boolean firstFound = false;
			for (int i = 0; i < paramNames.length; i++) {
				if (name.equalsIgnoreCase(paramNames[i])) {
					if (args[i] == null)
						continue;
					paramValues.add(new ParamValue(name, args[i].toString()));
					firstFound = true;
					break;
				}
			}
			if (firstFound)
				continue;
			for (Object arg : args) {
				// 处理数组
				if (arg instanceof List) {
					List<?> innerArgs = (List<?>) arg;
					String values = "";
					if (innerArgs == null || innerArgs.isEmpty())
						continue;

					if (innerArgs.size() > 0
							&& !ReflectUtil.hasRelflectField(innerArgs.get(0).getClass(), name))
						continue;

					if (isSingleValue) {
						try {
							Object value = wrapParamValue(innerArgs.get(0), name);
							paramValues.add(new ParamValue(name,
									value == null ? null : String.valueOf(value)));
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					} else {
						for (Object innerArg : innerArgs) {
							try {
								Object value = wrapParamValue(innerArg, name);
								if (value != null)
									values += String.valueOf(value) + SPLIT_SIGN;
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
						if (!StringUtils.isEmpty(values)) {
							paramValues.add(new ParamValue(name, values));
							break;
						}
					}
				}
				// 处理单个对象
				else {
					try {
						if (!ReflectUtil.hasRelflectField(arg.getClass(), name))
							continue;
						Object value = wrapParamValue(arg, name);
						paramValues.add(
								new ParamValue(name, value == null ? null : String.valueOf(value)));
						break;
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}

			}
		}

		return paramValues;
	}

	private String wrapParamValue(Object obj, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		Object result = ReflectUtil.getReflectValue(obj, fieldName);
		if (result == null || StringUtils.isEmpty(result.toString()))
			return null;
		String values = "";
		if (result instanceof Collection) {
			if (((Collection<?>) result).isEmpty())
				return null;
			for (Object value : (Collection<?>) result) {
				values += String.valueOf(value) + SPLIT_SIGN;
			}
			return values;
		} else
			return result.toString();
	}

	/**
	 * 解析方法参数值的封装对象
	 */
	class ParamValue {
		/**
		 * 参数名称
		 */
		private String name;
		/**
		 * 参数值
		 */
		private String value;

		public ParamValue(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	class InvokeInfo {
		private CtClass ctClass;

		private CtMethod ctMethod;
		/**
		 * 入参参数名称数组
		 */
		private String[] paramNames;

		public InvokeInfo(CtClass ctClass, CtMethod ctMethod, String[] paramNames) {
			super();
			this.ctClass = ctClass;
			this.ctMethod = ctMethod;
			this.paramNames = paramNames;
		}

		public CtClass getCtClass() {
			return ctClass;
		}

		public void setCtClass(CtClass ctClass) {
			this.ctClass = ctClass;
		}

		public CtMethod getCtMethod() {
			return ctMethod;
		}

		public void setCtMethod(CtMethod ctMethod) {
			this.ctMethod = ctMethod;
		}

		public String[] getParamNames() {
			return paramNames;
		}

		public void setParamNames(String[] paramNames) {
			this.paramNames = paramNames;
		}
	}
}
