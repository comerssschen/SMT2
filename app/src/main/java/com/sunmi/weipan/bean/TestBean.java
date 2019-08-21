package com.sunmi.weipan.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者：create by comersss on 2019/3/20 17:38
 * 邮箱：904359289@qq.com
 */
@Entity
public class TestBean {
    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 197411597)
    public TestBean(Long id) {
        this.id = id;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
