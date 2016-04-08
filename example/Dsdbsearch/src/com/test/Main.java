package com.test;

import com.dasudian.cloud.API;
import com.test.Dsdbsearch;

public class Main {

	/**
	 * @param args
	 */
    public static void main(String[] args)
    {
        try
        {
            final int thread_count = API.thread_count();
            assert(thread_count == 1);
            Dsdbsearch t = new Dsdbsearch();
            t.run();
        }
        catch (API.InvalidInputException e)
        {
            e.printStackTrace(API.err);
        }
    }

}
