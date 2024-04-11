package ch.fhnw.digi.mockups.case3.client;

import javax.jms.ConnectionFactory;

import ch.fhnw.digi.mockups.case3.JobAssignmentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	@Autowired
	private UI ui;

	@Autowired
	private ConnectionFactory connectionFactory;

	@Bean
	public DefaultJmsListenerContainerFactory myFactory(DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}

	@Bean // Serialize message content to json/from using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	// Listen for messages from "dispo.jobs.assignments" topic
	@org.springframework.jms.annotation.JmsListener(destination = "dispo.jobs.assignments", containerFactory = "myFactory")
	public void receiveAssignmentMessage(JobAssignmentMessage assignment) {
		ui.assignJob(assignment);
	}
}
