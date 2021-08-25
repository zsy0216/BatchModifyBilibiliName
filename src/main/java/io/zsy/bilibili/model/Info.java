package io.zsy.bilibili.model;

import lombok.Data;

/**
 * 视频基本信息
 *
 * @author: zhangshuaiyin
 * @date: 2021/1/29 15:10
 */
@Data
@SuppressWarnings("ALL")
public class Info {
    // private int Type;
    // private String Aid;
    private String Bid;
    // private String Cid;
    // private String SeasonId;
    // private String EpisodeId;
    private String Title;
    // private String Uploader;
    // private String Description;
    // private String CoverURL;
    // private String Tag;
    // private String From;
    // 视频序号
    private String PartNo;
    // 视频名
    private String PartName;
    // private int Format;
    // private int TotalParts;
    // private long DownloadTimeRelative;
    // private Date CreateDate;
    // private String TotalTime;
    // private List<Long> PartTime;
    // private long TotalSizeByte;
    // private boolean IsSinglePart;
    // private boolean IsDash;
    // private boolean IsMerged;
    // private VideoInfo VideoInfo;
    // private List<AudioInfo> AudioInfo;
}
