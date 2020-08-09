package Schedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import LoadDataStaging.ConnectDataConfig;

public class Schedule {
	Statement stm;
	ConnectDataConfig connectDataConfig;
	Connection connection;
	ResultSet result;

	public Schedule() {
		connectDataConfig = new ConnectDataConfig();
	}

	public Trigger getTrigger() {
		Trigger trigger = null;
		try {
			trigger = TriggerBuilder.newTrigger().withIdentity("triggerName", "group05")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/55 1-55 * * * ?")).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trigger;
	}

	public JobDetail getJob(int id) {
		JobDetail job = null;
		if (id == 1) {
			job = JobBuilder.newJob(StudentJob.class).withIdentity("jobName", "group05").build();
		} else if (id == 2) {
			job = JobBuilder.newJob(JobClass.class).withIdentity("jobName", "group05").build();
		} else if (id == 3) {
			job = JobBuilder.newJob(JobSubject.class).withIdentity("jobName", "group05").build();
		} else if (id == 4) {
			job = JobBuilder.newJob(JobRegister.class).withIdentity("jobName", "group05").build();
		}
		return job;
	}

	public void runProcessWithSchedule(int id) {// truyen vao id cua process
		int count = 4;
		String sql = "select count(*) from datawarehouse_configuration.`control.data_file` as c where file_status = 'DL'"
				+ " and c.data_config_id = " + id;
		try {
			Trigger trigger = getTrigger();
			JobDetail job = getJob(id);
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			connection = connectDataConfig.connectConfigDatabase();
			stm = connection.createStatement();
			result = stm.executeQuery(sql);
			while (result.next()) {
				count = result.getInt(1);
			}
			System.out.println("Count invalid status file : " + count);

			if (count > 0) {
				System.out.println("count " + count);
				scheduler.start();
				scheduler.scheduleJob(job, trigger);
			} else if (count == 0) {
				System.out.println("count " + count);
				System.out.println("End --------------- process");
				TriggerKey triggerKey = trigger.getKey();
				scheduler.pauseTrigger(triggerKey);
				scheduler.unscheduleJob(triggerKey);
				scheduler.deleteJob(job.getKey());
				scheduler.shutdown();
				System.out.println("Shut dow : " + scheduler.isShutdown());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SchedulerException {
		int id = Integer.parseInt(args[0]);
		Schedule s = new Schedule();
		s.runProcessWithSchedule(id);

	}
}
