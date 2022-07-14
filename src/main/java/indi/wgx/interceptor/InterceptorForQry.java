package indi.wgx.interceptor;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;


/**
 * 打印结果拦截器 〈功能详细描述〉
 *
 * @author
 * @version
 * @see InterceptorForQry
 * @since
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class})})
public class InterceptorForQry implements Interceptor
{

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object intercept(Invocation invocation)
            throws Throwable
    {
        Object result = invocation.proceed(); // 执行请求方法，并将所得结果保存到result中
        String str = JSON.toJSONString(result);
        log.debug("sql执行的结果：" + str);
        return result;
    }

    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties arg0)
    {}
}