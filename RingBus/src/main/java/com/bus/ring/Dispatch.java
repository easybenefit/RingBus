package com.bus.ring;

/**
 * Created by handgunbreak@gmail.com on 16/7/9.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description: Bus 分发
 */
public interface Dispatch {

    void dispatch(Runnable runnable);
}
