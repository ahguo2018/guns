package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

/**
 * @date 2019/6/2 11:30
 */
@Data
public class ResponseVO<M> {
    //返回状态[0-成功,1-业务失败,999-系统异常]
    private int status;
    //返回信息
    private String msg;
    //返回数据实体
    private M data;
    //图片前缀
    private String imgPre;
    //当前页数
    private int nowPage;
    //总页数
    private int totalPage;

    public static<M> ResponseVO success(M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        return responseVO;
    }

    public static<M> ResponseVO success(M m,String imgPre){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static<M> ResponseVO success(M m,int nowPage,int totalPage){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setTotalPage(totalPage);
        responseVO.setNowPage(nowPage);
        return responseVO;
    }

    public static<M> ResponseVO success(M m,String imgPre,int nowPage,int totalPage){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        responseVO.setTotalPage(totalPage);
        responseVO.setNowPage(nowPage);
        return responseVO;
    }

    public static<M> ResponseVO success(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static<M> ResponseVO serviceFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static<M> ResponseVO appFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

}
