package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import com.dasudian.cloud.API;
import com.dasudian.cloud.API.InvalidInputException;
import com.dasudian.cloud.API.MessageDecodingException;
import com.dasudian.cloud.API.Response;
import com.dasudian.cloud.API.TerminateException;
import com.dasudian.cloud.API.TransId;
import com.dasudian.cloud.DsdException;
import com.dasudian.cloud.Dsd_dsdb;
import com.dasudian.cloud.Dsd_log;
import com.dasudian.dac.DacPid;
                           

public class Dsdbsearch implements Runnable {

	/**
	 * @param args
	 */
	private File test=null;
	private API m_api=null;
	private PrintStream out=null;
	private Dsd_log m_log=null;
	public Dsdbsearch()
	{

		String dir=System.getProperty("java.class.path");
		String log_name=System.getProperty("user.name");
		String log_path;
		if(log_name ==null)
		{
			log_path=dir+"/"+"jlog.log";
		}
		else{
			log_path=dir+"/"+log_name+"1.log";
		}

		try {
			m_api =new API(0);
		} catch (InvalidInputException | MessageDecodingException
				| TerminateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_log =Dsd_log.getLog();
		try {
			m_log.set_level("Debug");
		} catch (DsdException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		m_log.error("===========haha,start search=========\n");
	}
	private void subscribe()
	{
	    m_api.subscribe("jdbslog/post", this, "hello_world_post" );
	    //m_api.subscribe("testB1", this, "hello_world_post1" );	
	}
	
	public Object hello_world_post(Integer command, String name, String pattern,
			byte[] request_info, byte[] request, Integer timeout,
			Byte priority, byte[] trans_id, DacPid pid) 
	{
		m_log.info("pattern is "+pattern);
		m_log.info("name is "+name);
		m_log.info("pid is "+pid.toString());
		System.out.flush();
		TransId id=null;
		Response res=null;
		String req_s=new String(request);
		int pslash1= req_s.indexOf('|');
		String req_key=req_s.substring(0, pslash1);
		String req_val=req_s.substring(pslash1+1);
		m_log.info("the key is"+req_key+"val is "+req_val);
	    Dsd_dsdb mdsdb=new Dsd_dsdb(this.m_api);
	    int flag=mdsdb.search("/db/dsdb/ocean/my_db", req_key, req_val);
	    if(flag == Dsd_dsdb.ERROR)
	    {
	    	return ("search error").getBytes();
	    }
	    else
	    {
	    	List<String> result = mdsdb.get_result();
	    	for(Iterator<String> i = result.iterator(); i.hasNext();i.next() ){
	    		m_log.info(i.toString());
	    	}
 	    }
        	
		return ("success").getBytes();
	}
	
	
	public void run(){
		subscribe();
		try {
			m_api.poll();
		} catch (InvalidInputException | MessageDecodingException
				| TerminateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_log.error("try to poll failed");
		}
	}	

}
