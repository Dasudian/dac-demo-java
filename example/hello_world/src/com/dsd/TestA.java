package com.dsd;

import com.dasudian.cloud.API;
import com.dasudian.cloud.API.InvalidInputException;
import com.dasudian.cloud.API.MessageDecodingException;
import com.dasudian.cloud.API.TerminateException;
import com.dasudian.cloud.DsdException;
import com.dasudian.cloud.Dsd_log;
import com.dasudian.dac.DacPid;

public class TestA {
	private Dsd_log m_log;
	public TestA(){

		m_log=Dsd_log.getLog();
		try {
			m_log.set_level("DEBUG");
		} catch (DsdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run(){
		int index;
		try {
			index =API.thread_count();
			m_log.info("the thread count is "+Integer.toString(index));
			
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_log.error("get the thread count failed");
			System.exit(0);
			return;
		}
		for(int i=0; i<index; i++){

			worker m_work= new worker(i,"/db/dsdb/ocean/load");
			m_log.info("start to sub");
			m_work.subs("hello_world/post");
			Thread t=new Thread(m_work);
			m_log.info("create thread ok");
			t.start();	
		}
		
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				m_log.info("main thread sleep failed ");
			}
		}
	}
	
	public class worker implements Runnable{
		
		private API m_api;
		private int thread_index;
		private String dsdb_server;
		@Override
		public void run() {
			try {
				m_log.info("to loop in thread 1");
				m_api.poll();
			} catch (InvalidInputException | MessageDecodingException
					| TerminateException e) {
				e.printStackTrace();
				m_log.error("poll in thread"+ Integer.toString(thread_index));
			}
			
		}

		public worker(int index,String dsdb_ser) {
			thread_index=index;
			this.dsdb_server =dsdb_ser;
			try {
				m_api = new API(index);
				m_log.info("success to init thread with "+Integer.toString(index));
//				m_log.info("end init");
			} catch (InvalidInputException | MessageDecodingException
					| TerminateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				m_log.error("init the api instance failed");
			}			
		}
		
		public void subs(String topic )
		{
			m_log.info("subscribe "+topic);
			m_api.subscribe(topic, this, "doCall");
			m_log.info("subscribe successfully");
		}
		
		
		public Object doCall(Integer command, String name, String pattern,
				byte[] request_info, byte[] request, Integer timeout,
				Byte priority, byte[] trans_id, DacPid pid) 
		{	

			return request;
		}
		
	}
}
