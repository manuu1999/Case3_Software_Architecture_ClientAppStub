package ch.fhnw.digi.mockups.case3.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobMessage;
import ch.fhnw.digi.mockups.case3.JobRequestMessage;

@Component
public class MessageSender {

	private static final Logger logger = LogManager.getLogger(MessageSender.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MessageConverter jacksonJmsMessageConverter;

	public void requestJobFromDispo(JobMessage job, String employeeName) {
		logger.info("Requesting job from dispo: " + job.getJobnumber());
		JobRequestMessage request = new JobRequestMessage();
		request.setJobnumber(job.getJobnumber());
		request.setRequestingEmployee(employeeName);
		jmsTemplate.convertAndSend("dispo.jobs.requestAssignment", request);
	}
}
