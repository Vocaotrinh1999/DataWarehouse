package Schedule;


import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import LoadDataStaging.InsertData2;

public class JobClass implements Job {

	InsertData2 i = new InsertData2();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Run my Job: " + LocalDateTime.now());
		i.mainProcessStaging(2);
	}
}
