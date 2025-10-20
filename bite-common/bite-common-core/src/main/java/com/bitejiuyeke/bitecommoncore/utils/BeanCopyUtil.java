package com.bitejiuyeke.bitecommoncore.utils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BeanCopyUtil extends BeanUtils {

    /**
     * 对装有 Bean 的列表进行拷贝
     * BeanUtils.copyProperties 不支持列表拷贝，默认只支持 java bean
     * 这里使用了 Supplier<T>, 可以间接获取泛型类的类型并进行对象的创建
     *
     * @param sourceList 源 bean 列表
     * @param target 目标 bean 类型
     * @return 目标 bean 列表
     * @param <S> 源 bean 类型
     * @param <T> 目标 bean 类型
     *
     */
    public static <S, T> List<T> copyListProperties(List<S> sourceList, Supplier<T> target) {

        if (null == sourceList || sourceList.isEmpty()) {
            return null;
        }

        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S sourceBean : sourceList) {
            T targetBean = target.get();
            copyProperties(sourceBean, targetBean);
            targetList.add(targetBean);
        }

        return targetList;
    }

}
