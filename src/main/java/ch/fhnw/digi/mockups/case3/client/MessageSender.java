package ch.fhnw.digi.mockups.case3.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobMessage;
import ch.fhnw.digi.mockups.case3.JobRequestMessage;

@Component
public class MessageSender {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MessageConverter jacksonJmsMessageConverter;
	public void sendNewJob(JobMessage job) {
		jmsTemplate.convertAndSend("dispo.jobs.new", job);
	}

	public void requestJobFromDispo(JobMessage job) {
		JobRequestMessage request = new JobRequestMessage();
		request.setJobnumber(job.getJobnumber());
		// You may set the requesting employee here if needed
		// request.setRequestingEmployee("EmployeeName");
		jmsTemplate.convertAndSend("dispo.jobs.requestAssignment", request);
	}
}
