package view.AdminView;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class AnalyticsPanel extends JPanel {
    private JLabel lblTotalRevenue;
    private JComboBox<String> cmbDateFilter;
    private JButton btnViewReport;
    private JPanel chartDisplayPanel;

    public AnalyticsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. CONTROL PANEL (Bộ lọc) - WEST
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc & Điều khiển"));
        controlPanel.setPreferredSize(new Dimension(250, 600));

        JLabel lblFilter = new JLabel("Chọn thời gian thống kê:");
        lblFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(lblFilter);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        cmbDateFilter = new JComboBox<>(new String[] {
        "Tháng hiện tại",
        "Năm hiện tại",
        "Xem theo năm"
        });
        cmbDateFilter.setMaximumSize(new Dimension(200, 30));
        cmbDateFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(cmbDateFilter);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        btnViewReport = new JButton("Xem Báo Cáo");
        btnViewReport.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(btnViewReport);
        controlPanel.add(Box.createVerticalGlue());

        add(controlPanel, BorderLayout.WEST);

        // 2. STATS PANEL (Tổng quan tài chính) - NORTH
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Tổng quan Doanh thu"));
        lblTotalRevenue = new JLabel("Tổng Doanh thu Đã hoàn thành: Đang tính...");
        lblTotalRevenue.setFont(new Font("Tahoma", Font.BOLD, 18));
        statsPanel.add(lblTotalRevenue);
        add(statsPanel, BorderLayout.NORTH);

        // 3. CHART DISPLAY PANEL (Khu vực Biểu đồ) - CENTER
        chartDisplayPanel = new JPanel(new BorderLayout());
        chartDisplayPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ Doanh thu theo thời gian"));
        chartDisplayPanel.setBackground(Color.WHITE);
        JLabel chartPlaceholder = new JLabel("Chọn bộ lọc và nhấn 'Xem Báo Cáo' để tải biểu đồ.", SwingConstants.CENTER);
        chartDisplayPanel.add(chartPlaceholder, BorderLayout.CENTER);
        add(chartDisplayPanel, BorderLayout.CENTER);
    }

    // Phương thức gắn Controller
    public void addController(ActionListener listener) {
        btnViewReport.addActionListener(listener);
        btnViewReport.setActionCommand("ANALYTICS_VIEW_REPORT");
    }

    // Getters và Setters cho Controller
    public String getDateFilterSelection() { return cmbDateFilter.getSelectedItem().toString(); }
    public JPanel getChartDisplayPanel() { return chartDisplayPanel; }
    public void setTotalRevenue(double revenue) {
        lblTotalRevenue.setText(String.format("Tổng Doanh thu Đã hoàn thành: %,.0f VNĐ", revenue));
    }
}