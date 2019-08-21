package com.sunmi.weipan.bean;

/**
 * L3自定义配置类
 *
 * @author:sunhengzhi
 * @date:2018-12-06
 */
public class Config {

    /**
     * 交易过程中是否显示UI界面(不包括结果页)
     */
    public Boolean processDisplay;
    /**
     * 是否展示交易结果页
     */
    public Boolean resultDisplay;
    /**
     * 是否打印小票
     */
    public Boolean printTicket;
    /**
     * 指定签购单上的订单号类型
     */
    public String printIdType;
    /**
     * 备注
     */
    public String remarks;

    @Override
    public String toString() {
        return "Config{" +
                "processDisplay=" + processDisplay +
                ", resultDisplay=" + resultDisplay +
                ", printTicket=" + printTicket +
                ", printIdType='" + printIdType + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
