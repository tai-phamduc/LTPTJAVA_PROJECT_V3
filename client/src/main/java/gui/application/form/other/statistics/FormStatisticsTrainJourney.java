package gui.application.form.other.statistics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;

import entity.Employee;
import entity.Month;
import entity.TrainJourneyRanking;
import entity.Year;
import net.miginfocom.swing.MigLayout;
import utils.ServerFetcher;

public class FormStatisticsTrainJourney extends JPanel {

	private JPanel superContainer;
	private JPanel container1;
	private JLabel titleLabel;
	private JComboBox<Month> monthCombobox;
	private JComboBox<Year> yearComboBox;
	private Month monthSelected;
	private Year yearSelected;
	private JPanel container;
	private JPanel container2;
	private JTable bangXepHangChuyenTauTable;
	private BangXepHangChuyenTauModel bangXepHangChuyenTauTableModel;
	private List<TrainJourneyRanking> trainJourneyRankingList;

	public FormStatisticsTrainJourney(Employee employee) {
		this.setLayout(new BorderLayout());
		superContainer = new JPanel(new MigLayout("wrap, fill, insets 20", "[fill]", "[fill]"));
		container = new JPanel(new MigLayout("wrap, fill", "[fill]", "[][]"));
		
		// state
		monthSelected = new Month(LocalDate.now().getMonthValue());
		yearSelected = new Year(LocalDate.now().getYear());
		HashMap<String, String> payload = new HashMap<>();
		payload.put("month", String.valueOf(LocalDate.now().getMonthValue()));
		payload.put("year", String.valueOf(LocalDate.now().getYear()));

		trainJourneyRankingList = (List<TrainJourneyRanking>) ServerFetcher.fetch(
				"trainjourneyranking",
				"getTop10TrainJourneyRanking",
				payload
		);		// state
		
		// render
		render();
		// render
		
		superContainer.add(container);
		this.add(superContainer);		
	}

	private void render() {
		container.removeAll();
		
		/*-----------------------------------------------------------------------------------*/
		container1 = new JPanel(new MigLayout("wrap, fill, insets 0", "[]push[][]", "[fill]"));
		titleLabel = new JLabel("Bảng xếp hạng chi tiêu khách hàng");
		titleLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
		monthCombobox = new JComboBox<Month>();
		monthCombobox.addItem(new Month(1, "Tháng 1"));
		monthCombobox.addItem(new Month(2, "Tháng 2"));
		monthCombobox.addItem(new Month(3, "Tháng 3"));
		monthCombobox.addItem(new Month(4, "Tháng 4"));
		monthCombobox.addItem(new Month(5, "Tháng 5"));
		monthCombobox.addItem(new Month(6, "Tháng 6"));
		monthCombobox.addItem(new Month(7, "Tháng 7"));
		monthCombobox.addItem(new Month(8, "Tháng 8"));
		monthCombobox.addItem(new Month(9, "Tháng 9"));
		monthCombobox.addItem(new Month(10, "Tháng 10"));
		monthCombobox.addItem(new Month(11, "Tháng 11"));
		monthCombobox.addItem(new Month(12, "Tháng 12"));
		monthCombobox.setSelectedItem(monthSelected);
		yearComboBox = new JComboBox<Year>();
		yearComboBox.addItem(new Year(2022, "Năm 2022"));
		yearComboBox.addItem(new Year(2023, "Năm 2023"));
		yearComboBox.addItem(new Year(2024, "Năm 2024"));
		yearComboBox.setSelectedItem(yearSelected);
		monthCombobox.addActionListener(e -> {
			monthSelected = (Month) monthCombobox.getSelectedItem();
			int selectedMonth = ((Month)monthCombobox.getSelectedItem()).getValue();
			int selectedYear = ((Year)yearComboBox.getSelectedItem()).getValue();
			HashMap<String, String> payload = new HashMap<>();
			payload.put("month", String.valueOf(selectedMonth));
			payload.put("year", String.valueOf(selectedYear));

			trainJourneyRankingList = (List<TrainJourneyRanking>) ServerFetcher.fetch(
					"trainjourneyranking",
					"getTop10TrainJourneyRanking",
					payload
			);			render();
		});
		yearComboBox.addActionListener(e -> {
			monthSelected = (Month) monthCombobox.getSelectedItem();
			int selectedMonth = ((Month)monthCombobox.getSelectedItem()).getValue();
			int selectedYear = ((Year)yearComboBox.getSelectedItem()).getValue();
			HashMap<String, String> payload = new HashMap<>();
			payload.put("month", String.valueOf(selectedMonth));
			payload.put("year", String.valueOf(selectedYear));

			trainJourneyRankingList = (List<TrainJourneyRanking>) ServerFetcher.fetch(
					"trainjourneyranking",
					"getTop10TrainJourneyRanking",
					payload
			);			render();
		});
		container1.add(titleLabel);
		container1.add(monthCombobox);
		container1.add(yearComboBox);
		container.add(container1, "dock north");
		/*-----------------------------------------------------------------------------------*/
		
		/*-----------------------------------------------------------------------------------*/
		container2 = new JPanel(new MigLayout("wrap, fill, insets 20 0 20 0", "[fill]", "[fill]"));
		bangXepHangChuyenTauTableModel = new BangXepHangChuyenTauModel(trainJourneyRankingList);
		bangXepHangChuyenTauTable = new JTable(bangXepHangChuyenTauTableModel);
		container2.add(new JScrollPane(bangXepHangChuyenTauTable));
		
		JScrollPane scroll = (JScrollPane) bangXepHangChuyenTauTable.getParent().getParent();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
		bangXepHangChuyenTauTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		bangXepHangChuyenTauTable.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
		
		bangXepHangChuyenTauTable.getTableHeader().setDefaultRenderer(
				getAlignmentCellRender(bangXepHangChuyenTauTable.getTableHeader().getDefaultRenderer(), true));
		bangXepHangChuyenTauTable.setDefaultRenderer(Object.class,
				getAlignmentCellRender(bangXepHangChuyenTauTable.getDefaultRenderer(Object.class), false));

		if (bangXepHangChuyenTauTable.getColumnModel().getColumnCount() > 0) {
			bangXepHangChuyenTauTable.getColumnModel().getColumn(1).setPreferredWidth(300);
		}
		
		container.add(container2, "dock center");
		/*-----------------------------------------------------------------------------------*/
		
		/*-----------------------------------------------------------------------------------*/
		/*-----------------------------------------------------------------------------------*/
		
		container.repaint();
		container.revalidate();
	}
	
	private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (com instanceof JLabel) {
					JLabel label = (JLabel) com;
					if (column == 2 || column == 3) {
						label.setHorizontalAlignment(SwingConstants.CENTER);
					} else {
						label.setHorizontalAlignment(SwingConstants.LEADING);
					}
					if (header == false) {
						if (isSelected) {
							com.setForeground(table.getSelectionForeground());
						} else {
							com.setForeground(table.getForeground());
						}
					}
				}
				return com;
			}
		};
	};
	
}
