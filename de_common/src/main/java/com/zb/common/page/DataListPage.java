package com.zb.common.page;

import io.vertx.core.json.JsonArray;

import java.util.Arrays;

/**
 * Created by zhangbo on 17-9-12.
 */
public class DataListPage {

    /**
     * 计算显示的标签页数
     *
     * @param pages 总页码数 navigatePages 想要展示的页码数 pageNum 当前页码数
     */
    public static int[] calcNavigatepageNums(int pages, int navigatePages, int pageNum) {
        int startNum;
        int[] navigatepageNums;
        if (pages <= navigatePages) {
            navigatepageNums = new int[pages];

            for (startNum = 0; startNum < pages; ++startNum) {
                navigatepageNums[startNum] = startNum + 1;
            }
        } else {
            navigatepageNums = new int[navigatePages];
            startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;
            int i;
            if (startNum < 1) {
                startNum = 1;

                for (i = 0; i < navigatePages; ++i) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;

                for (i = navigatePages - 1; i >= 0; --i) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                for (i = 0; i < navigatePages; ++i) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
        return navigatepageNums;
    }

}
