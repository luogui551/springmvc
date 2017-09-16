package com.aires.base.dao.orm.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
/**
 * 
 * @author aires
 * 2017年1月17日 下午3:06:58
 * 描述：通过指定的别名来映射bean属性
 */
@SuppressWarnings("serial")
class AliasToBeanTransformer extends AbstractTransformer  {

	private final Class<?> resultClass;
	private Setter[] setters;//属性访问器
	private PropertyAccessor propertyAccessor;
	/**
	 * @param resultClass
	 */
	public AliasToBeanTransformer(Class<?> resultClass, String[]aliases) {
		super(aliases);
		if(resultClass == null) throw new IllegalArgumentException("resultClass cannot be null");
		this.resultClass = resultClass;
		propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] { PropertyAccessorFactory.getPropertyAccessor(resultClass,null), PropertyAccessorFactory.getPropertyAccessor("field")}); 
		
		setters = new Setter[aliases.length];
		for (int i = 0; i < aliases.length; i++) {
			String alias = aliases[i];
			if(alias != null) {
				setters[i] = propertyAccessor.getSetter(resultClass, alias);
			}
		}
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		aliases = this.getAliases(aliases);
		try {
			if(setters == null) {
				setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; i++) {
					String alias = aliases[i];
					if(alias != null) {
						setters[i] = propertyAccessor.getSetter(resultClass, alias);
					}
				}
			}
			result = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				Setter setter = setters[i];
				if(setter != null) {
					Class<?>type = setter.getMethod().getParameterTypes()[0];
					//值为null时，使用默认值
					if(tuple[i] != null)setter.set(result, numberResolve(type, tuple[i]), null);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}
		
		return result;
	}
	
	//将hibernate返回的Number数据转换为正确的基本类型
	private Object numberResolve(Class<?>targetType, Object v){
		if(v instanceof Number && targetType != v.getClass()){
			Number val = (Number) v;
			if(targetType == int.class || targetType == Integer.class){
				return val.intValue();
			}else if(targetType == long.class || targetType == Long.class){
				return val.longValue();
			}else if(targetType == byte.class || targetType == Byte.class){
				return val.byteValue();
			}else if(targetType == short.class || targetType == Short.class){
				return val.shortValue();
			}else if(targetType == double.class || targetType == Double.class){
				return val.doubleValue();
			}else if(targetType == float.class || targetType == Float.class){
				return val.floatValue();
			}
		}
		return dealClob(v);
	}	
}
