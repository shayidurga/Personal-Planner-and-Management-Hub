package view;

import com.toedter.calendar.JDateChooser;
import dao.ScheduleDAO;
import model.Schedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ScheduleManagementUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private ScheduleDAO scheduleDAO;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private int userId;

    public ScheduleManagementUI(ScheduleDAO scheduleDAO, int userId) {
        this.scheduleDAO = scheduleDAO;
        this.userId = userId;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Schedule Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed default close operation
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Date Chooser
        JPanel datePanel = new JPanel();
        JLabel dateLabel = new JLabel("Select Date: ");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        datePanel.add(dateLabel);
        datePanel.add(dateChooser);
        add(datePanel, BorderLayout.NORTH);

        // Schedule Table Panel
        JPanel schedulePanel = new JPanel(new BorderLayout());
        scheduleTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        schedulePanel.add(scrollPane, BorderLayout.CENTER);
        add(schedulePanel, BorderLayout.CENTER);

        // CRUD Operations Panel
        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addSchedule(dateChooser.getDate()));
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateSchedule());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSchedule());

        crudPanel.add(addButton);
        crudPanel.add(updateButton);
        crudPanel.add(deleteButton);
        add(crudPanel, BorderLayout.SOUTH);

        updateScheduleTable(); // Display existing schedule
        setVisible(true);
    }

    private void addSchedule(Date selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String day = dateFormat.format(selectedDate);
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        String description = JOptionPane.showInputDialog(this, "Enter Description:");
        String priority = JOptionPane.showInputDialog(this, "Enter Priority (Low/Medium/High):");
        String status = JOptionPane.showInputDialog(this, "Enter Status (Pending/Completed):");

        Schedule schedule = new Schedule(0, userId, day, title, description, priority, status);
        try {
            scheduleDAO.addSchedule(schedule);
            updateScheduleTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            String day = (String) tableModel.getValueAt(selectedRow, 1);
            String newTitle = JOptionPane.showInputDialog(this, "Enter New Title:");
            String newDescription = JOptionPane.showInputDialog(this, "Enter New Description:");
            String newPriority = JOptionPane.showInputDialog(this, "Select New Priority (Low/Medium/High):");
            String newStatus = JOptionPane.showInputDialog(this, "Select New Status (Pending/Completed):");

            // Retrieve the schedule object from the table model
            Schedule selectedSchedule = getScheduleFromRow(selectedRow);

            // Ensure the schedule object is not null before proceeding
            if (selectedSchedule != null) {
                try {
                    // Update the schedule
                    Schedule updatedSchedule = new Schedule(selectedSchedule.getId(), userId, day, newTitle, newDescription, newPriority, newStatus);
                    scheduleDAO.updateSchedule(updatedSchedule);
                    updateScheduleTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: Unable to retrieve the selected schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a schedule to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            // Retrieve the schedule object from the table model
            Schedule selectedSchedule = getScheduleFromRow(selectedRow);

            // Ensure the schedule object is not null before proceeding
            if (selectedSchedule != null) {
                try {
                    // Delete the schedule
                    scheduleDAO.deleteSchedule(selectedSchedule.getId());
                    updateScheduleTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: Unable to retrieve the selected schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a schedule to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateScheduleTable() {
        String[] columnNames = {"ID", "Day", "Title", "Description", "Priority", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        try {
            Map<Integer, Schedule> scheduleMap = scheduleDAO.getAllSchedules(userId);
            for (Map.Entry<Integer, Schedule> entry : scheduleMap.entrySet()) {
                Schedule schedule = entry.getValue();
                String priority = schedule.getPriority().toString(); // Convert enum to string
                String status = schedule.getStatus().toString(); // Convert enum to string
                String[] rowData = {String.valueOf(schedule.getId()), schedule.getDay(), schedule.getTitle(), schedule.getDescription(),
                        priority, status};
                tableModel.addRow(rowData);
            }
            scheduleTable.setModel(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Schedule getScheduleFromRow(int row) {
        int scheduleId = Integer.parseInt((String) tableModel.getValueAt(row, 0));
        try {
            Map<Integer, Schedule> scheduleMap = scheduleDAO.getAllSchedules(userId);
            return scheduleMap.get(scheduleId);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
