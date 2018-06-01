package com.geostar.gfstack.jdbc.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class TestMain {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        test(args);
    }

    private static boolean test(String[] args) throws IOException, SQLException, ClassNotFoundException {
        InputStream is = TestMain.class.getClassLoader().getResourceAsStream("test.sql");
        List<String> list = IOUtils.readLines(is);
        String sql = list.get(0);
        return executeSql(sql, args);
    }

    private static boolean executeSql(String sql, String[] args) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("jdbc测试开始");
        System.out.println("测试SQL：" + sql);
        if (args.length > 0) {
            System.out.println("测试参数：");
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + 1 + "、" + args[i]);
            }
        }

        Properties properties = new Properties();
        properties.load(TestMain.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        Class.forName(properties.getProperty("jdbc.driver"));
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        String printColumn = properties.getProperty("printColumn");
        Connection conn = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            ps.setString(i + 1, args[i]);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("结果集输出***************************start***************************");
            System.out.println(printColumn + ":" + rs.getString(printColumn));
            while (rs.next()) {
                System.out.println(printColumn + ":" + rs.getString(printColumn));
            }
            System.out.println("结果集输出***************************end***************************");
        } else {
            System.out.println("本次未查询到匹配的记录，请检查SQL语句及参数");
        }
        rs.close();
        ps.close();
        conn.close();
        System.out.println("jdbc测试结束");
        return true;
    }

}
