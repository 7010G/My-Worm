package cn.yumben.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzg
 */
public class ListDeal {

    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */

    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        //数据量不足count指定的大小
        if (size <= count) {
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int I = 0; I < pre; I++) {
                List<T> itemList = new ArrayList<T>();
                for (int J = 0; J < count; J++) {
                    itemList.add(resList.get(I * count + J));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int I = 0; I < last; I++) {
                    itemList.add(resList.get(pre * count + I));
                }
                ret.add(itemList);
            }

        }

        return ret;
    }
}