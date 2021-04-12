package com.th.bean.vo;

import lombok.Data;

/**
 * @author TanHaooo
 * @date 2021/3/30 20:17
 */
@Data
public class SelectStatusMessage {
    private String resultSort;
    //显示状态 1 全部 2未观看 3 已观看
    private int display;
    private String[] type;
    private int[] rating;
    private int vote;
    private int[] time;
    private int currentPage;
    private int size;
}
