package com.jslsolucoes.nginx.admin.scheduler.task;

import javax.enterprise.inject.Vetoed;

import org.apache.http.HttpStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jslsolucoes.nginx.admin.http.HttpClientBuilder;

@Vetoed
public class CollectLogTask implements Job {

	private static final Logger logger = LoggerFactory.getLogger(CollectLogTask.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		new HttpClientBuilder().client()
				.get(jobExecutionContext.getMergedJobDataMap().getString("url_base") + "/task/collect/log")
				.onNotStatus(HttpStatus.SC_OK,
						closeableHttpResponse -> logger.error("Job cannot be executed : status code => "
								+ closeableHttpResponse.getStatusLine().getStatusCode()))
				.onError(exception -> exception.printStackTrace()).close();
	}
}