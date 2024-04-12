package ch.fhnw.digi.mockups.case3.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.digi.mockups.case3.JobAssignmentMessage;
import ch.fhnw.digi.mockups.case3.JobMessage;

@SuppressWarnings("serial")
@Component
public class UI extends JFrame {

	private static final Logger logger = LogManager.getLogger(UI.class);

	@Autowired
	private MessageSender messageSender;

	private JList<JobMessage> m_list_jobs;
	DefaultListModel<JobMessage> m_list_jobs_model;
	DefaultListModel<String> m_list_assignments_model;

	private JTextField m_txt_employeeName;

	@PostConstruct
	void init() {

		m_list_jobs_model = new DefaultListModel<JobMessage>();
		m_list_jobs = new JList<JobMessage>(m_list_jobs_model);

		m_list_assignments_model = new DefaultListModel<String>();
		JList<String> m_list_assignments = new JList<String>(m_list_assignments_model);

		m_txt_employeeName = new JTextField(20);
		JButton m_btn_assign = new JButton("An Mitarbeiter zuweisen");
		m_btn_assign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        requestSelectedJob();
			}
		});

		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("Name Mitarbeiter:"));
		namePanel.add(m_txt_employeeName);
		namePanel.add(m_btn_assign);

		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel listsPanel = new JPanel();
		listsPanel.setLayout(new BoxLayout(listsPanel, BoxLayout.X_AXIS));

		JPanel jobsPanel = new JPanel(new BorderLayout());
		jobsPanel.add(new JLabel("Offene Jobs:"), BorderLayout.NORTH);
		jobsPanel.add(new JScrollPane(m_list_jobs), BorderLayout.CENTER);
		jobsPanel.setMinimumSize(new Dimension(200, 200));

		JPanel assignmentsPanel = new JPanel(new BorderLayout());
		assignmentsPanel.add(new JLabel("Zugewiesene Jobs:"), BorderLayout.NORTH);
		assignmentsPanel.add(new JScrollPane(m_list_assignments), BorderLayout.CENTER);
		assignmentsPanel.setMinimumSize(new Dimension(200, 200));

		listsPanel.add(jobsPanel);
		listsPanel.add(assignmentsPanel);

		rootPanel.add(namePanel, BorderLayout.NORTH);
		rootPanel.add(listsPanel, BorderLayout.CENTER);

		getContentPane().add(rootPanel);

		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		getContentPane().add(rootPanel);

		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		logger.debug("------ UI initialized ------");
	}

	protected void requestSelectedJob() {
		logger.info("------ Requesting selected job ------");
		String employeeName = m_txt_employeeName.getText().trim();
		if (!employeeName.isEmpty()) {
			logger.debug("Employee name set to: " + employeeName);
			// Optionally store or use the name for other operations
		} else {
			JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JobMessage selectedJob = m_list_jobs.getSelectedValue();
		if (selectedJob != null) {
			messageSender.requestJobFromDispo(selectedJob, employeeName);
		} else {
			// Optionally show a message if no job is selected
			JOptionPane.showMessageDialog(this, "No job selected. Please select a job to request.", "No Selection", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void addJobToList(JobMessage j) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (m_list_jobs_model) {
					m_list_jobs_model.add(0, j);
				}
			}
		});
	}

	public void assignJob(JobAssignmentMessage c) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (m_list_jobs_model) {
					for (int x = 0; x < m_list_jobs_model.getSize(); ++x) {
						if (m_list_jobs_model.get(x).getJobnumber().equals(c.getJobnumber())) {
							m_list_jobs_model.remove(x);
							break;
						}
					}
				}

				synchronized (m_list_assignments_model) {
					m_list_assignments_model.add(0,
							"Job " + c.getJobnumber() + " wurde an \"" + c.getAssignedEmployee() + "\" vergeben");
				}
			}
		});
		logger.info("Job " + c.getJobnumber() + " wurde an \"" + c.getAssignedEmployee() + "\" vergeben");
	}
}
