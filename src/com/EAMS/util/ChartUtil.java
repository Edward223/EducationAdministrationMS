package com.EAMS.util;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ChartUtil {

    public BarChart<String ,Number> setBarChartData(int week, Map<String ,Number> courseNumMap) {
        CategoryAxis categoryAxis = new CategoryAxis();
        NumberAxis numberAxis = new NumberAxis();
        // 设置条形图水平轴的标签名称
        categoryAxis.setLabel("第"+week+"周");
        categoryAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("周一","周二","周三","周四","周五")));
        // 设置条形图垂直轴的标签名称
        numberAxis.setLabel("课时数");

        BarChart<String ,Number> barChart = new BarChart<>(categoryAxis,numberAxis);
        // 设置条形图标题
        barChart.setTitle("各周每日课时数");

        // 设置条形数据
        XYChart.Series<String ,Number> courseNumSeries = new XYChart.Series<>();

//        // 清空集合日期标签中的内容
//        categoryAxis.getCategories().clear();

        // 填充数据
        Set<String> courseNum = courseNumMap.keySet();
        //
        Number maxCourseNum = courseNumMap.get("maxNum");
        Number minCourseNum = courseNumMap.get("minNUm");

        courseNumSeries.getData().add(new XYChart.Data<>("周一", courseNumMap.get("monday")));
        courseNumSeries.getData().add(new XYChart.Data<>("周二", courseNumMap.get("tuesday")));
        courseNumSeries.getData().add(new XYChart.Data<>("周三", courseNumMap.get("wednesday")));
        courseNumSeries.getData().add(new XYChart.Data<>("周四", courseNumMap.get("thursday")));
        courseNumSeries.getData().add(new XYChart.Data<>("周五", courseNumMap.get("friday")));

        // 填充条形图上的数据
        barChart.getData().add(courseNumSeries);

        return barChart;
    }

}
