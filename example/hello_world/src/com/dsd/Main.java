package com.dsd;

import com.dasudian.cloud.API;
import com.dasudian.cloud.Dsd_log;

public class Main
{
    public static void main(String[] args)
    {

//            final int thread_count = API.thread_count();
      //      assert(thread_count == 1);
            TestA t = new TestA();
            t.run();

    }
}	
