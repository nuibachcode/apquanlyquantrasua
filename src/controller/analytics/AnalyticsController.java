package controller.analytics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import model.Bill;
import repository.BillRepositoryImpl;
import repository.IRepository.IBillRepository;
import view.AdminView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

public class AnalyticsController implements ActionListener {
    
    private AdminView adminView;
    private IBillRepository billRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AnalyticsController(AdminView adminView) {
        this.adminView = adminView;
        this.billRepository = new BillRepositoryImpl(); // Giả định BillRepositoryImpl đã tồn tại
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "ANALYTICS_VIEW_REPORT": generateReport(); break;
        }
    }
    
    // --- HÀM CHI TIẾT ---
    
    private void generateReport() {
        String filter = adminView.getDateFilterSelection();
        String chartTitle = "";
        String xAxisLabel = "";
        CategoryDataset dataset = new DefaultCategoryDataset();
        
        List<Bill> allBills = billRepository.findAll();
        List<Bill> completedBills = allBills.stream()
            .filter(Bill::getIsCompleted)
            .collect(Collectors.toList());

        if (completedBills.isEmpty()) {
            JOptionPane.showMessageDialog(adminView, "Không có dữ liệu doanh thu để tạo báo cáo.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 1. LỌC VÀ CHỌN CHẾ ĐỘ HIỂN THỊ
        if (filter.equals("Tháng hiện tại")) {
            LocalDate now = LocalDate.now();
            List<Bill> currentMonthBills = filterBillsByMonth(completedBills, now.getYear(), now.getMonthValue());
            dataset = createDailyDataset(currentMonthBills, now.getYear(), now.getMonthValue());
            chartTitle = String.format("Doanh thu theo NGÀY (Tháng %02d/%d)", now.getMonthValue(), now.getYear());
            xAxisLabel = "Ngày trong tháng";

        } else if (filter.equals("Năm hiện tại")) {
            LocalDate now = LocalDate.now();
            List<Bill> currentYearBills = filterBillsByYear(completedBills, now.getYear());
            dataset = createMonthlyDataset(currentYearBills, now.getYear());
            chartTitle = String.format("Doanh thu theo THÁNG (Năm %d)", now.getYear());
            xAxisLabel = "Tháng";
            
        } else if (filter.equals("Xem theo năm")) {
            dataset = createYearlyDataset(completedBills);
            chartTitle = "Tổng Doanh thu theo NĂM";
            xAxisLabel = "Năm";
            
        } else {
            dataset = createYearlyDataset(completedBills);
            chartTitle = "Tổng Doanh thu theo NĂM";
            xAxisLabel = "Năm";
        }
        
        // 2. TẠO VÀ HIỂN THỊ BIỂU ĐỒ
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle,
            xAxisLabel,
            "Doanh thu (VNĐ)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Cập nhật giao diện
        JPanel displayPanel = adminView.getChartDisplayPanel();
        displayPanel.removeAll();
        displayPanel.add(chartPanel, BorderLayout.CENTER);
        displayPanel.revalidate();
        displayPanel.repaint();
    }
    
    // --- HÀM HỖ TRỢ LỌC VÀ TẠO DATASET ---
    
    private List<Bill> filterBillsByMonth(List<Bill> bills, int year, int month) {
        return bills.stream()
            .filter(bill -> {
                try {
                    LocalDate date = LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER);
                    return date.getYear() == year && date.getMonthValue() == month;
                } catch (Exception e) { return false; }
            })
            .collect(Collectors.toList());
    }
    
    private List<Bill> filterBillsByYear(List<Bill> bills, int year) {
        return bills.stream()
            .filter(bill -> {
                try {
                    LocalDate date = LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER);
                    return date.getYear() == year;
                } catch (Exception e) { return false; }
            })
            .collect(Collectors.toList());
    }

    private CategoryDataset createDailyDataset(List<Bill> bills, int year, int month) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Double> dailyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getDayOfMonth(),
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        
        for (int day = 1; day <= daysInMonth; day++) {
            double revenue = dailyRevenueMap.getOrDefault(day, 0.0);
            dataset.addValue(revenue, "Doanh thu", String.valueOf(day));
        }
        return dataset;
    }

    private CategoryDataset createMonthlyDataset(List<Bill> bills, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Double> monthlyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getMonthValue(),
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        
        for (int month = 1; month <= 12; month++) {
            double revenue = monthlyRevenueMap.getOrDefault(month, 0.0);
            String displayKey = LocalDate.of(year, month, 1).format(monthFormatter);
            dataset.addValue(revenue, "Doanh thu", displayKey);
        }
        return dataset;
    }
    
    private CategoryDataset createYearlyDataset(List<Bill> bills) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Double> yearlyRevenueMap = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> LocalDate.parse(bill.getNgayDat(), DATE_FORMATTER).getYear(),
                TreeMap::new,
                Collectors.summingDouble(Bill::getTongTien)
            ));
        
        yearlyRevenueMap.forEach((year, revenue) -> {
            dataset.addValue(revenue, "Doanh thu", year.toString());
        });
        
        if (yearlyRevenueMap.isEmpty()) {
             dataset.addValue(0.0, "Doanh thu", String.valueOf(LocalDate.now().getYear()));
        }
        return dataset;
    }
}