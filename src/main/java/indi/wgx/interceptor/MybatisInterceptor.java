package indi.wgx.interceptor;


import indi.wgx.entity.InitTableVo;
import indi.wgx.entity.SqlVo;
import indi.wgx.service.ExplainService;
import indi.wgx.service.HttpService;
import indi.wgx.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 背景：虽然mybatis结合日志框架可以做到，但打印出来的通常都是sql和参数分开的。
 * 有时我们需要调试这条sql的时候，就需要把参数填进去，这样未免有些浪费时间。
 *
 * 通过实现mybatis拦截器来做到打印带参数的完整的sql，以及结果通过json输出到控制台
 */

/**
 * MyBatis拦截器打印不带问号的完整sql语句
 *
 * @author
 * @version
 * @see MybatisInterceptor
 * @since
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
                Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                Object.class, RowBounds.class, ResultHandler.class})})
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
public class MybatisInterceptor implements Interceptor {

    @Autowired
    ExplainService explainService;

    @Autowired
    HttpService httpService;

    @Autowired
    TableService tableService;

    static SqlVo sqlVo = new SqlVo();
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        try {
            // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
            MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
            Object parameter = null;
            // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
                log.info("sql传入的参数，parameter = " + parameter);
            }
            String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
            log.info("sqlId = " + sqlId);
            BoundSql boundSql = mappedStatement.getBoundSql(parameter); // BoundSql就是封装myBatis最终产生的sql类
            Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
            String sql = getSql(configuration, boundSql, sqlId); // 获取到最终的sql语句
            log.info("完整sql信息 = " + sql);
            String databaseName = tableService.getDatabaseName();
            sqlVo.setDatabaseName(databaseName);
            log.info("数据库名称 = " + databaseName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //记录耗时
        long start = System.currentTimeMillis();
        // 执行完上面的任务后，不改变原有的sql执行过程
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();
        // sql执行耗时
        long time = end - start;
        sqlVo.setTime(time);
        log.debug("time = " + time + "ms");

        // ExplainService
        InitTableVo initTableInformation = explainService.getAllInitTableInformation(sqlVo);

        System.out.println("待发送的数据===================");
        System.out.println(initTableInformation.getProjectVersion());
        System.out.println(initTableInformation.getSqlTemplate());
        System.out.println(initTableInformation.getSqlExecution());
        initTableInformation.getSqlDimensionList().forEach(System.out::println);

        // HTTP发送数据
        try {
            httpService.sendInitTableMessage(initTableInformation);
//            httpService.sendMessageByWebClient(initTableInformation);
            log.info("成功发送数据！！！！");
        } catch (Exception e) {
            log.info("未成功发送数据！！！！！！");
        }
        return result;
    }

    // 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
    public String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(":");
        str.append(sql);
        return str.toString();
    }

    // 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        }
        else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        }
        else {
            if (obj != null) {
                value = obj.toString();
            }
            else {
                value = "";
            }
        }
        return value;
    }

    // 进行？的替换
    public String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        // 将sql模板中的所有因为字母全部转化为小写
        sql = sql.toLowerCase(Locale.ROOT);
        sqlVo.setSqlTemplate(sql);
        log.info("sql模板 = " + sql);
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));

            }
            else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                    else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                    else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        // 将具体sql中的所有因为字母全部转化为小写
        sql = sql.toLowerCase(Locale.ROOT);
        sqlVo.setSqlContent(sql);
        log.info("具体sql = " + sql);
        // 获取表的名称
        String tableNames = tableService.getTableName(sql);
        if (tableNames.equals("null") || tableNames.length() == 0) {
            tableNames = "未能识别";
        }
        sqlVo.setTableName(tableNames);
        log.info("表的名称 = " + tableNames);
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
