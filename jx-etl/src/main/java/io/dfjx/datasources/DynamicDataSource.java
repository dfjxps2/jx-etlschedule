/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjx.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源
 *
 * @author Mark sunlightcs@gmail.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }

    public static void setDataSource(String dataSource) {
        DynamicContextHolder.push(dataSource);
    }

    public static String getDataSource() {
        return DynamicContextHolder.peek();
    }

    public static void clearDataSource() {
        DynamicContextHolder.poll();
    }

}
