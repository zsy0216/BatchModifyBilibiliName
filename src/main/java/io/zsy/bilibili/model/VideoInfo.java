package io.zsy.bilibili.model;

import lombok.Data;

/**
 * @author: zhangshuaiyin
 * @date: 2021/1/29 15:28
 */
@Data
@SuppressWarnings("ALL")
public class VideoInfo {
    private int MediaType;
    private int CodecId;
    private String CodecName;
    private int VideoWidth;
    private int VideoHeight;
    private long Bandwidth;
    private double FrameRate;
}
