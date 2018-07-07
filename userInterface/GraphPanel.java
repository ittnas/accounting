package userInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;

import coreClasses.Account;
import coreClasses.AccountData;
import coreClasses.GraphState;
import coreClasses.Note;
import coreClasses.Trace;
import coreClasses.AccountData.ValueType;
import coreClasses.GraphState.StepType;
import coreClasses.Trace.MarkerType;
import coreClasses.Trace.TraceType;
import dataStructures.SortedList;

public class GraphPanel extends JPanel implements ActionListener{

	private AccountingGUI main;
	private ChartPanel chartPanel;
	private JPanel optionsPanel;
	private GraphState currentState;
	private GraphState originalState;

	private int position = 0;
	private int nbrTraces = 0;

	private JButton addTraceButton;

	private JPanel basicOptions;

	private JButton readyButton;

	private JFormattedTextField dateStartField;

	private JFormattedTextField dateEndField;

	private Java2sAutoComboBox intervalSelector;

	private JSpinner otherIntervalField;

	private ArrayList<TracePanel> tracePanelList;

	private JScrollPane imageScroll;
	private JSplitPane split;
	
	private Trace defaultTrace;

	public GraphPanel(final AccountingGUI main) {
		this.main = main;

		//this.setLayout(new GridLayout(0,1));
		this.setLayout(new BorderLayout());
		tracePanelList = new ArrayList<TracePanel>();
		currentState = main.getDefaultGraphState();
		originalState = main.getDefaultGraphState();
		if(main.getDefaultGraphState().getTraceList() != null && main.getDefaultGraphState().getTraceList().size() > 0) {
			defaultTrace = main.getDefaultGraphState().getTraceList().get(0);
		} else {
			main.updateStatus("Oletusuraa ei lˆydy");
		}
		imageScroll = new JScrollPane(null);

		optionsPanel = createOptionsPanel();
		JScrollPane optionsScroll = new JScrollPane(optionsPanel);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,imageScroll,optionsScroll);
		split.setOneTouchExpandable(true);
		split.setPreferredSize(new Dimension(-1,(int)this.getPreferredSize().getWidth() - 30));
		JMenuItem removeItem = new JMenuItem(TracePanel.removeIcon);
		removeItem.setPreferredSize(new Dimension((int)1.5*TracePanel.removeIcon.getIconWidth()+10,(int)1.1*TracePanel.removeIcon.getIconHeight()));
		removeItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem)e.getSource();
				GraphPanel parent = (GraphPanel)(item.getParent().getParent());
				parent.main.removeGraphPanel(parent);
			}
		});
		JMenuItem newPanelItem = new JMenuItem(TracePanel.addIcon);
		newPanelItem.setPreferredSize(new Dimension((int)1.5*TracePanel.addIcon.getIconWidth()+10,(int)1.1*TracePanel.addIcon.getIconHeight()));
		newPanelItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.addGraphPanel();
			}
		});
		JMenuBar menu = new JMenuBar();
		menu.setLayout(new FlowLayout(FlowLayout.LEADING));
		menu.add(newPanelItem);
		menu.add(removeItem);

		this.add(menu,BorderLayout.PAGE_START);

		this.add(split, BorderLayout.CENTER);
		int preferredSize = 500;
		if(this.getPreferredSize().getWidth() < preferredSize) {
			this.setPreferredSize(new Dimension(0,500));
		}
	}
	
	public GraphPanel(final AccountingGUI main, GraphState state) {
		this(main);
		intervalSelector.setSelectedItem(state.getStepType().toString());
		otherIntervalField.setValue(state.getStepInverval());
		for(Trace trace : state.getTraceList()) {
			addTrace(trace);
		}
		
	}

	private JPanel createOptionsPanel() {
		//Set the layout to the correct value
		JPanel optionsPanel = new JPanel(new FlowLayout());
		optionsPanel.setOpaque(false);
		optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(AccountingGUI.lineColor), "Valinnat",
				TitledBorder.LEFT, TitledBorder.TOP, AccountingGUI.boldFont, AccountingGUI.fontColor));
		GridBagLayout basicLayout = new GridBagLayout();
		GridBagConstraints constraintsL = new GridBagConstraints(0, 0, 1, 
				1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0);
		GridBagConstraints constraintsR = new GridBagConstraints(1, 0, 1, 
				1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0);
		basicOptions = new JPanel(basicLayout);
		basicOptions.setOpaque(false);
		basicOptions.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AccountingGUI.lineColor));
		constraintsR.gridwidth = 1;
		readyButton = new JButton("Valmis");
		readyButton.setForeground(AccountingGUI.fontColor);
		readyButton.setFont(AccountingGUI.font);
		readyButton.addActionListener(this);
		constraintsR.gridx = 0;
		constraintsR.gridwidth = 1;
		basicOptions.add(readyButton,constraintsR);
		constraintsR.gridx = 1;
		constraintsL.gridy++;
		constraintsR.gridy++;
		JLabel intervalLabel = new JLabel("Aikav‰li");
		intervalLabel.setFont(AccountingGUI.font);
		intervalLabel.setOpaque(false);
		intervalLabel.setForeground(AccountingGUI.fontColor);
		basicOptions.add(intervalLabel,constraintsL);

		MaskFormatter mask = AccountingGUI.createMaskFormatter();
		dateStartField = new JFormattedTextField(mask);
		dateStartField.setOpaque(true);
		dateStartField.setForeground(AccountingGUI.fontColor);
		dateStartField.setFont(AccountingGUI.font);
		dateStartField.setColumns(7);
		dateStartField.addFocusListener(main);
		basicOptions.add(dateStartField,constraintsR);
		constraintsR.gridx++;
		JLabel shortLine = new JLabel("-");
		shortLine.setForeground(AccountingGUI.fontColor);
		shortLine.setFont(AccountingGUI.font);
		shortLine.setOpaque(false);
		basicOptions.add(shortLine,constraintsR);
		constraintsR.gridx++;
		dateEndField = new JFormattedTextField(mask);
		dateEndField.setOpaque(true);
		dateEndField.setForeground(AccountingGUI.fontColor);
		dateEndField.setFont(AccountingGUI.font);
		dateEndField.setColumns(7);
		dateEndField.addFocusListener(main);
		basicOptions.add(dateEndField,constraintsR);

		constraintsR.gridx++;
		JLabel intervalSelectorLabel = new JLabel("Askelv‰li");
		intervalSelectorLabel.setForeground(AccountingGUI.fontColor);
		intervalSelectorLabel.setFont(AccountingGUI.font);
		intervalSelectorLabel.setOpaque(false);
		basicOptions.add(intervalSelectorLabel,constraintsR);
		constraintsR.gridx++;
		StepType[] intArr = StepType.values();
		List<StepType> intList = Arrays.asList(intArr);
		intervalSelector = new Java2sAutoComboBox(intList);

		NumberFormat format = NumberFormat.getIntegerInstance();
		SpinnerNumberModel model = new SpinnerNumberModel(1,1,1000,1);
		otherIntervalField = new JSpinner(model);
		//otherIntervalField = new JFormattedTextField(format);
		otherIntervalField.setForeground(AccountingGUI.fontColor);
		otherIntervalField.setFont(AccountingGUI.font);
		//otherIntervalField.setColumns(4);
		otherIntervalField.setValue(1);
		basicOptions.add(otherIntervalField,constraintsR);
		constraintsR.gridx++;
		basicOptions.add(intervalSelector,constraintsR);
		constraintsR.gridy++;
		constraintsR.gridx++;

		JLabel accountSelectorLabel = new JLabel("Tilin valitsin");
		accountSelectorLabel.setForeground(AccountingGUI.fontColor);
		accountSelectorLabel.setOpaque(false);
		accountSelectorLabel.setFont(AccountingGUI.boldFont);
		constraintsL.gridy++;
		constraintsR.gridy++;
		basicOptions.add(accountSelectorLabel,constraintsL);

		addTraceButton = new JButton("Lis‰‰ ura");
		addTraceButton.setForeground(AccountingGUI.fontColor);
		addTraceButton.setFont(AccountingGUI.font);
		addTraceButton.addActionListener(this);
		constraintsL.gridy++;
		constraintsR.gridy++;
		basicOptions.add(addTraceButton,constraintsL);

		position = constraintsL.gridy;

		optionsPanel.add(basicOptions);
		return optionsPanel;

	}
	
	private void addTrace(Trace trace) {
		nbrTraces++;
		GridBagConstraints constraints = new GridBagConstraints(0, position+nbrTraces, 8, 1, 0, 0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);

		TracePanel tracePanel = new TracePanel(main,nbrTraces,this,trace);
		constraints.gridx = 0;
		basicOptions.add(tracePanel,constraints);
		tracePanelList.add(tracePanel);
		basicOptions.revalidate();

	}

	private void addTrace() {
		addTrace(defaultTrace);
		//GridBagConstraints traceConst = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		//Java2sAutoComboBox accountSelector = new Java2sAutoComboBox(main.getAccountMap().getAccountNames(false));
		//main.addComboboxToUpdateList(accountSelector);

		//constraints.gridx++;

	}

	public void removeTrace(TracePanel removable) {
		int index = removable.getNumber();
		for(int i = index-1; i< tracePanelList.size(); i++) {
			tracePanelList.get(i).reduceTraceNumber();
		}
		basicOptions.remove(removable);
		tracePanelList.remove(removable);

		//		int compNbr = basicOptions.getComponentCount();
		//		TracePanel last = (TracePanel)basicOptions.getComponent(compNbr-1);
		//		int lastNbr = last.getNumber();
		//		int compIndex = compNbr -lastNbr + removable.getNumber();
		//		
		//		for(int i = compIndex; i < compNbr;i++) {
		//			TracePanel currPanel = (TracePanel)(basicOptions.getComponent(i));
		//			currPanel.reduceTraceNumber();
		//		}
		//		basicOptions.remove(removable);
		nbrTraces--;
		position++; //The gridbaglayout position increases
		basicOptions.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addTraceButton)) {
			addTrace();
		}
		if(e.getSource().equals(readyButton)) {
			prepareGraph();
			// Check the divider bar location. There is no method for reading the current
			//relative location, so this cumbersome procedure is performed.
			//If the current location of splitbar is less than 15% from the left, it is moved
			//completely to the right. Otherwise it stays where it is.
			int loc = split.getDividerLocation();
			split.setDividerLocation(1.0);
			int max = split.getDividerLocation();
			split.setDividerLocation(0.0);
			int min = split.getDividerLocation();
			if((double)loc/(max-min) < 0.15) {
				split.setDividerLocation(1.0);
			} else {
				split.setDividerLocation(loc);
			}
		}
	}
	
	public void fireReadyEvent() {
		readyButton.doClick(0);
	}

	private void prepareGraph() {
		GraphState state = collectGraphInformation();
		ChartPanel chart = createChart(state);
		//Update the view. The previous chart will be garbage-collected I guess.
		imageScroll.setViewportView(chart);
	}

	//public ArrayList<Date> countIntervalAxis(Date start, Date end,StepType stepType, int stepValue) {
	//	
	//}

	@SuppressWarnings("unchecked")
	private ChartPanel createChart(GraphState state) {
		ChartPanel cp = null;
		JFreeChart chart = null;
		
		for(int traceNbr = 0; traceNbr < state.getTraceList().size(); traceNbr++) {
			//Time dep or not
			if(state.getTraceList().get(traceNbr).getTraceType().timeDep()) {
				StepType stepType = state.getStepType();
				LinkedList<Date> dateList = new LinkedList<Date>();

				LinkedList<Double> valueList[];
				ArrayList<AccountData> accounts= state.getTraceList().get(traceNbr).getAccountData();
				DateFormat dateFormat = new SimpleDateFormat(stepType.getFormat());
				int size = accounts.size();
				if(size == 0) {
					// Nothing to display
					return null;
				}
				// Actually not used for anything
				valueList = (LinkedList<Double>[]) new LinkedList[size];
				XYSeries series[] = new XYSeries[size];       
				for(int i = 0; i < size;i++) {
					valueList[i] = new LinkedList<Double>();
					series[i] = new XYSeries(accounts.get(i).getTargetAccount().getName(),false,false);
				}
				Date currDate = null;
				if(state.getStartDate() != null) {
					currDate = stepType.firstDay(state.getStartDate());
				}
				Date end = null;
				if(state.getEndDate() != null) {
					end = stepType.lastDay(state.getEndDate());
				}

				SortedList<Note> noteList[];
				noteList = (SortedList<Note>[]) new SortedList[size];
				for(int i = 0; i < size;i++) {
					// Again, remember the reversed order
					noteList[i] = main.getAccountMap().getNotesInRange(accounts.get(i).getTargetAccount(), end,currDate);
				}
				double currValue[] = new double[size];
				if(currDate == null) {
					Date minDate = null;
					for(int i = 0; i < size;i++) {
						if(noteList[i].size() != 0) {
							Date tempDate = noteList[i].getLast().getDate();
							if(minDate == null || tempDate.before(minDate)) {
								minDate = tempDate;
							}
						}
					}
					if(minDate == null) {
						currDate = new Date(0);
					} else {
					currDate = stepType.firstDay(minDate);
					}
				} else {
					for(int i = 0; i < size; i++) {
						//Notes in reversed order, with nulls the order is not switched
						SortedList<Note> notes = main.getAccountMap().getNotesInRange(accounts.get(i).getTargetAccount(), currDate, null);
						if(state.getTraceList().get(traceNbr).getAccountData().get(i).getValueType().equals(ValueType.ACCUMULATE)) {
							currValue[i] = main.countValue(accounts.get(i).getTargetAccount(), notes);
						}
					}
				}

				if(end == null) {
					Date maxDate = null;
					for(int i = 0; i < size;i++) {
						if(noteList[i].size() != 0) {
							Date tempDate = noteList[i].getFirst().getDate();
							if(maxDate == null || tempDate.after(maxDate)) {
								maxDate = tempDate;
							}
						}
					}
					if(maxDate == null) {
						end = new Date(0);
					} else {	
						end = stepType.lastDay(maxDate);
					}
				}	

				ListIterator iteratorList[] = new ListIterator[size];
				for(int i = 0; i < size; i++) {
					iteratorList[i] = noteList[i].listIterator(noteList[i].size());
				}

				do {
					currDate = stepType.advance(currDate,state.getStepInverval());
					dateList.add(currDate);
					for(int i = 0; i < size; i++) {
						ListIterator itr = iteratorList[i];
						//currValue[i] = nextValue[i];
						while(itr.hasPrevious()) {
							Note element  = (Note) itr.previous();
							if(element.getDate().before(currDate)) {
								currValue[i] += element.getSignedValue(accounts.get(i).getTargetAccount());
							} else {
								itr.next();
								valueList[i].add(currValue[i]);
								series[i].add(currDate.getTime(), currValue[i]);
								switch(state.getTraceList().get(traceNbr).getAccountData().get(i).getValueType()) {
								case ACCUMULATE:
									//nextValue[i] = currValue[i];
									break;
								case DIFFERENCE:
									currValue[i] = 0;
									break;
								}
								break;
							}
						}
						if(!itr.hasPrevious()) {
							valueList[i].add(currValue[i]);
							series[i].add(currDate.getTime(), currValue[i]);
							switch(state.getTraceList().get(traceNbr).getAccountData().get(i).getValueType()) {
							case ACCUMULATE:
								//nextValue[i] = currValue[i];
								break;
							case DIFFERENCE:
								currValue[i] = 0;
								break;
							}
						}
					}

				} while(currDate.before(end));
				XYSeriesCollection xyDataset = new XYSeriesCollection();
				for(int i = 0; i < size; i++) {
					xyDataset.addSeries(series[i]);
				}
				try {
				switch(state.getTraceList().get(traceNbr).getTraceType()) {
				case LINE:
					if(chart == null) {
						chart = ChartFactory.createTimeSeriesChart("jee", "time", "value", xyDataset, true, false, false);
						DateAxis axis = (DateAxis)chart.getXYPlot().getDomainAxis();
						axis.setDateFormatOverride(dateFormat);
					} else {
						XYPlot plot = chart.getXYPlot();
						XYItemRenderer renderer = new StandardXYItemRenderer();
						plot.setRenderer(traceNbr,renderer);
						plot.setDataset(traceNbr,xyDataset);
					}
					break;
				case BAR:
					
					DefaultTableXYDataset tableSet = new DefaultTableXYDataset();
					for(int i = 0; i < series.length;i++) {
						tableSet.addSeries(series[i]);
					}
					tableSet.setIntervalWidth(stepType.getPeriodLength()*state.getStepInverval());
					
					StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.2);
					if(chart == null) {
						DateAxis axis = new DateAxis();
						axis.setDateFormatOverride(dateFormat);
						XYPlot mainPlot = new XYPlot(tableSet, axis, new NumberAxis("Value"), renderer);
					chart = new JFreeChart(mainPlot);
					} else {
						XYPlot plot = chart.getXYPlot();
						plot.setRenderer(traceNbr,renderer);
						plot.setDataset(traceNbr,tableSet);
					}
					/*
					DefaultCategoryDataset set = new DefaultCategoryDataset();
					for(int i = 0; i < series.length;i++) {
						for(int j = 0; j < series[i].getItemCount();j++) {
							set.addValue(series[i].getY(j),series[i].getKey(),new Date(series[i].getX(j).longValue()));
						}
					}
					chart = ChartFactory.createStackedBarChart("jee", "time", "value", set, PlotOrientation.VERTICAL, true, false, false);
					*/
					break;
				case FILLED:
					if(chart == null) {
					chart = ChartFactory.createXYAreaChart("jee", "time", "value", xyDataset, PlotOrientation.VERTICAL, true, false, false);
					XYPlot plot = chart.getXYPlot();
					DateAxis axis = new DateAxis();
					axis.setDateFormatOverride(dateFormat);
					plot.setDomainAxis(axis);
					} else {
						XYPlot plot = chart.getXYPlot();
						plot.setRenderer(traceNbr,new XYAreaRenderer());
						plot.setDataset(traceNbr,xyDataset);
					}
					break;
				case AREA:
					tableSet = new DefaultTableXYDataset();
					for(int i = 0; i < series.length;i++) {
						tableSet.addSeries(series[i]);
					}
					if(chart == null) {
					chart = ChartFactory.createStackedXYAreaChart("jee", "time", "value", tableSet, PlotOrientation.VERTICAL, true, false, false);
					XYPlot plot = chart.getXYPlot();
					DateAxis axis = new DateAxis();
					axis.setDateFormatOverride(dateFormat);
					plot.setDomainAxis(axis);
					} else {
						XYPlot plot = chart.getXYPlot();
						plot.setRenderer(traceNbr,new StackedXYAreaRenderer());
						plot.setDataset(traceNbr,tableSet);
					}
					break;
				}
				} catch(ClassCastException e) {
					// There was an incompatible chart already. Dunno what to do about that.
				}
				cp = new ChartPanel(chart);

			} else {
				// Only the first trace is used
				// Relations are not used
				if(traceNbr > 0) {
					break;
				}
				Trace trace = state.getTraceList().get(0);
				ArrayList<Account> data = trace.getAccounts();
				switch (trace.getTraceType()) {
				case PIE:
					DefaultPieDataset pie = new DefaultPieDataset();
					for(Account acc : data) {
						double value = Math.abs(main.countValue(acc, main.getAccountMap().getNotesInRange(acc, state.getEndDate(), state.getStartDate())));
						pie.setValue(acc.getName(), value);
					}
					chart = ChartFactory.createPieChart("Test", pie,true,true,false);
					break;
				}
				cp = new ChartPanel(chart);
			}
		}
		return cp;
	}

	public GraphState collectGraphInformation() {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = AccountingGUI.parseDate(dateStartField.getText());
		} catch (NullPointerException e) {
		} catch (IllegalDateException e) {
			//dateStartField.setForeground(AccountingGUI.errorColor);
			//main.updateStatus("Alkup‰iv‰m‰‰r‰ ei kelpaa");
		}
		try {
			endDate = AccountingGUI.parseDate(dateEndField.getText());
		} catch (NullPointerException e) {
		} catch (IllegalDateException e) {
			//dateEndField.setForeground(AccountingGUI.errorColor);
			//main.updateStatus("Loppup‰iv‰m‰‰r‰ ei kelpaa");
		}
		if(startDate != null && endDate != null) {
			if(startDate.after(endDate)) {
				Date temp = startDate;
				startDate = endDate;
				endDate = temp;
				main.updateStatus("P‰iv‰m‰‰r‰t olivat v‰‰rinp‰in. Virhe korjattu");
			}
		}
		String stepTypeString = intervalSelector.getSelectedItem().toString();
		StepType stepType = StepType.valueOf(stepTypeString);
		int step = 1; 
		try {
			step = (Integer)otherIntervalField.getValue();
		} catch (NumberFormatException e) {

		}

		ArrayList<Trace> traces = new ArrayList<Trace>();
		for(TracePanel iterator : tracePanelList) {
			traces.add(iterator.getTrace());
		}

		GraphState state = new GraphState(startDate, endDate, step, stepType, traces);
		return state;
	}
	
	public void setState(GraphState state) {
		String endDateString = AccountingGUI.dateFormat.format(state.getEndDate());
		String startDateString = AccountingGUI.dateFormat.format(state.getStartDate());
		
		//TODO do this.
	}

}
