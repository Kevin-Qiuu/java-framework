package com.bitejiuyeke.bitecommoncore.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcelUtils {

    /**
     * 表格列名校验，判断是否与类的变量名一致
     *
     * @param file  表格文件
     * @param clazz 校验类
     * @return 校验成功 = true ； 校验失败 = false
     */
    public static Boolean columNameValidate(MultipartFile file, Class<?> clazz) {
        // 获取类的字段名
        Set<String> fieldNames = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldNames.add(field.getName());
        }

        // 读取 Excel 文件
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // 获取第一行（列名）
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return false;
            }

            // 校验列名是否匹配
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                String columnName = headerRow.getCell(i).getStringCellValue();
                if (!fieldNames.contains(columnName)) {
                    return false; // 列名不匹配
                }
            }
        } catch (Exception ignored) {
            return false;
        }

        return true; // 列名匹配
    }

    /**
     * 将列表内容转换为一个 list
     *
     * @param file   表格文件
     * @param tClass 转换的对象
     * @param <T>    对象类型
     * @return 对象 list
     */
    public static <T> List<T> toList(MultipartFile file, Class<T> tClass) {

        // 校验列名是否匹配
        if (!columNameValidate(file, tClass)) {
            return null;
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // 获取第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            List<T> resultList = new ArrayList<>();

            // 获取列名
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return null;
            }
            List<String> columnNames = new ArrayList<>();
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                columnNames.add(headerRow.getCell(i).getStringCellValue());
            }

            // 遍历数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                // 创建对象实例
                T instance = tClass.getDeclaredConstructor().newInstance();

                // 通过反射设置字段值
                for (int j = 0; j < columnNames.size(); j++) {
                    String columnName = columnNames.get(j);
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }

                    Field field = tClass.getDeclaredField(columnName);
                    field.setAccessible(true);

                    // 使用 DataFormatter 格式化单元格内容
                    String cellValue = new DataFormatter().formatCellValue(cell);

                    // 根据字段类型设置值
                    if (field.getType().equals(String.class)) {
                        field.set(instance, cellValue);
                    } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                        field.set(instance, Integer.parseInt(cellValue));
                    } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                        field.set(instance, Double.parseDouble(cellValue));
                    } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                        field.set(instance, Boolean.parseBoolean(cellValue));
                    }
                }
                resultList.add(instance);
            }
            return resultList;
        } catch (Exception ignore) {
        }
        return null;
    }


}
