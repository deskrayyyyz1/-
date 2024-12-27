// Колмогорцев Павел 23Пинж
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SportsSchoolApp extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    private ArrayList<Training> trainings = new ArrayList<>();
    private ArrayList<ScheduleEntry> schedule = new ArrayList<>();

    private DefaultTableModel studentTableModel;
    private DefaultTableModel coachTableModel;
    private DefaultTableModel trainingTableModel;
    private DefaultTableModel scheduleTableModel;

    private JTable studentTable;
    private JTable coachTable;
    private JTable trainingTable;
    private JTable scheduleTable;

    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;

    public SportsSchoolApp() {
        setTitle("Спортивная школа");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel studentPanel = createStudentPanel();
        tabbedPane.addTab("Ученики", studentPanel);

        JPanel coachPanel = createCoachPanel();
        tabbedPane.addTab("Тренеры", coachPanel);

        JPanel trainingPanel = createTrainingPanel();
        tabbedPane.addTab("Занятия", trainingPanel);

        JPanel schedulePanel = createSchedulePanel();
        tabbedPane.addTab("Расписание", schedulePanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить в файл");
        styleButton(saveButton);
        saveButton.addActionListener(e -> saveToFile());

        JButton loadButton = new JButton("Загрузить из файла");
        styleButton(loadButton);
        loadButton.addActionListener(e -> loadFromFile());

        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        studentTableModel = new DefaultTableModel(new String[]{"Имя", "Возраст", "Вид спорта"}, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(studentTableModel);
        studentTable.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchColumnComboBox = new JComboBox<>(new String[]{"Имя", "Возраст", "Вид спорта"});
        JButton searchButton = new JButton("Поиск");
        styleButton(searchButton);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(sorter, new String[]{"Имя", "Возраст", "Вид спорта"});
            }
        });

        searchButton.addActionListener(e -> performSearch(sorter, new String[]{"Имя", "Возраст", "Вид спорта"}));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("в столбце:"));
        searchPanel.add(searchColumnComboBox);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        styleButton(addButton);
        addButton.addActionListener(e -> addStudent());

        JButton editButton = new JButton("Редактировать");
        styleButton(editButton);
        editButton.addActionListener(e -> editStudent());

        JButton deleteButton = new JButton("Удалить");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteStudent());

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCoachPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        coachTableModel = new DefaultTableModel(new String[]{"Имя", "Специализация"}, 0);
        coachTable = new JTable(coachTableModel);
        coachTable.setRowHeight(25);
        coachTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(coachTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(coachTableModel);
        coachTable.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchColumnComboBox = new JComboBox<>(new String[]{"Имя", "Специализация"});
        JButton searchButton = new JButton("Поиск");
        styleButton(searchButton);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(sorter, new String[]{"Имя", "Специализация"});
            }
        });

        searchButton.addActionListener(e -> performSearch(sorter, new String[]{"Имя", "Специализация"}));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("в столбце:"));
        searchPanel.add(searchColumnComboBox);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        styleButton(addButton);
        addButton.addActionListener(e -> addCoach());

        JButton editButton = new JButton("Редактировать");
        styleButton(editButton);
        editButton.addActionListener(e -> editCoach());

        JButton deleteButton = new JButton("Удалить");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteCoach());

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTrainingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        trainingTableModel = new DefaultTableModel(new String[]{"Название", "Вид спорта", "Тренер"}, 0);
        trainingTable = new JTable(trainingTableModel);
        trainingTable.setRowHeight(25);
        trainingTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(trainingTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(trainingTableModel);
        trainingTable.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchColumnComboBox = new JComboBox<>(new String[]{"Название", "Вид спорта", "Тренер"});
        JButton searchButton = new JButton("Поиск");
        styleButton(searchButton);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(sorter, new String[]{"Название", "Вид спорта", "Тренер"});
            }
        });

        searchButton.addActionListener(e -> performSearch(sorter, new String[]{"Название", "Вид спорта", "Тренер"}));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("в столбце:"));
        searchPanel.add(searchColumnComboBox);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        styleButton(addButton);
        addButton.addActionListener(e -> addTraining());

        JButton editButton = new JButton("Редактировать");
        styleButton(editButton);
        editButton.addActionListener(e -> editTraining());

        JButton deleteButton = new JButton("Удалить");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteTraining());

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        scheduleTableModel = new DefaultTableModel(new String[]{"День недели", "Время", "Занятие", "Тренер"}, 0);
        scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setRowHeight(25);
        scheduleTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(scheduleTableModel);
        scheduleTable.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchColumnComboBox = new JComboBox<>(new String[]{"День недели", "Время", "Занятие", "Тренер"});
        JButton searchButton = new JButton("Поиск");
        styleButton(searchButton);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(sorter, new String[]{"День недели", "Время", "Занятие", "Тренер"});
            }
        });

        searchButton.addActionListener(e -> performSearch(sorter, new String[]{"День недели", "Время", "Занятие", "Тренер"}));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Поиск:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("в столбце:"));
        searchPanel.add(searchColumnComboBox);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        styleButton(addButton);
        addButton.addActionListener(e -> addScheduleEntry());

        JButton editButton = new JButton("Редактировать");
        styleButton(editButton);
        editButton.addActionListener(e -> editScheduleEntry());

        JButton deleteButton = new JButton("Удалить");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteScheduleEntry());

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void performSearch(TableRowSorter<DefaultTableModel> sorter, String[] columnNames) {
        String text = searchField.getText();
        String selectedColumn = (String) searchColumnComboBox.getSelectedItem();

        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                int columnIndex = getColumnIndex(selectedColumn, columnNames);
                if (columnIndex != -1) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), columnIndex));
                }
            } catch (PatternSyntaxException ex) {
                JOptionPane.showMessageDialog(this, "Некорректный поисковый запрос.");
            }
        }
    }

    private int getColumnIndex(String selectedColumn, String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            if (selectedColumn.equals(columnNames[i])) {
                return i;
            }
        }
        return -1;
    }

    private void addStudent() {
        String name = showInputDialog("Введите имя ученика:", "");
        if (name == null || name.isEmpty()) return;

        String ageStr = showInputDialog("Введите возраст ученика:", "");
        if (ageStr == null || ageStr.isEmpty()) return;
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Некорректный возраст.");
            return;
        }

        String sport = showInputDialog("Введите вид спорта:", "");
        if (sport == null || sport.isEmpty()) return;

        Student student = new Student(name, age, sport);
        students.add(student);
        studentTableModel.addRow(new Object[]{name, age, sport});
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите ученика для редактирования.");
            return;
        }

        int modelRowIndex = studentTable.convertRowIndexToModel(selectedRow);
        Student student = students.get(modelRowIndex);

        String name = showInputDialog("Введите новое имя ученика:", student.getName());
        if (name == null || name.isEmpty()) return;

        String ageStr = showInputDialog("Введите новый возраст ученика:", String.valueOf(student.getAge()));
        if (ageStr == null || ageStr.isEmpty()) return;

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Некорректный возраст.");
            return;
        }

        String sport = showInputDialog("Введите новый вид спорта:", student.getSport());
        if (sport == null || sport.isEmpty()) return;

        student.setName(name);
        student.setAge(age);
        student.setSport(sport);

        studentTableModel.setValueAt(name, modelRowIndex, 0);
        studentTableModel.setValueAt(age, modelRowIndex, 1);
        studentTableModel.setValueAt(sport, modelRowIndex, 2);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите ученика для удаления.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить ученика?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRowIndex = studentTable.convertRowIndexToModel(selectedRow);
            students.remove(modelRowIndex);
            studentTableModel.removeRow(modelRowIndex);
        }
    }

    private void addCoach() {
        String name = showInputDialog("Введите имя тренера:", "");
        if (name == null || name.isEmpty()) return;

        String specialization = showInputDialog("Введите специализацию тренера:", "");
        if (specialization == null || specialization.isEmpty()) return;

        Coach coach = new Coach(name, specialization);
        coaches.add(coach);
        coachTableModel.addRow(new Object[]{name, specialization});
    }

    private void editCoach() {
        int selectedRow = coachTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите тренера для редактирования.");
            return;
        }

        int modelRowIndex = coachTable.convertRowIndexToModel(selectedRow);
        Coach coach = coaches.get(modelRowIndex);

        String name = showInputDialog("Введите новое имя тренера:", coach.getName());
        if (name == null || name.isEmpty()) return;

        String specialization = showInputDialog("Введите новую специализацию тренера:", coach.getSpecialization());
        if (specialization == null || specialization.isEmpty()) return;

        coach.setName(name);
        coach.setSpecialization(specialization);

        coachTableModel.setValueAt(name, modelRowIndex, 0);
        coachTableModel.setValueAt(specialization, modelRowIndex, 1);
    }

    private void deleteCoach() {
        int selectedRow = coachTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите тренера для удаления.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить тренера?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRowIndex = coachTable.convertRowIndexToModel(selectedRow);
            coaches.remove(modelRowIndex);
            coachTableModel.removeRow(modelRowIndex);
        }
    }

    private void addTraining() {
        String name = showInputDialog("Введите название занятия:", "");
        if (name == null || name.isEmpty()) return;

        String sport = showInputDialog("Введите вид спорта:", "");
        if (sport == null || sport.isEmpty()) return;

        String coachName = showInputDialog("Введите имя тренера:", "");
        if (coachName == null || coachName.isEmpty()) return;

        Coach coach = findCoachByName(coachName);
        if (coach == null) {
            JOptionPane.showMessageDialog(this, "Тренер не найден.");
            return;
        }

        Training training = new Training(name, sport, coach);
        trainings.add(training);
        trainingTableModel.addRow(new Object[]{name, sport, coach.getName()});
    }

    private void editTraining() {
        int selectedRow = trainingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите занятие для редактирования.");
            return;
        }

        int modelRowIndex = trainingTable.convertRowIndexToModel(selectedRow);
        Training training = trainings.get(modelRowIndex);

        String name = showInputDialog("Введите новое название занятия:", training.getName());
        if (name == null || name.isEmpty()) return;

        String sport = showInputDialog("Введите новый вид спорта:", training.getSport());
        if (sport == null || sport.isEmpty()) return;

        String coachName = showInputDialog("Введите новое имя тренера:", training.getCoach().getName());
        if (coachName == null || coachName.isEmpty()) return;

        Coach coach = findCoachByName(coachName);
        if (coach == null) {
            JOptionPane.showMessageDialog(this, "Тренер не найден.");
            return;
        }

        training.setName(name);
        training.setSport(sport);
        training.setCoach(coach);

        trainingTableModel.setValueAt(name, modelRowIndex, 0);
        trainingTableModel.setValueAt(sport, modelRowIndex, 1);
        trainingTableModel.setValueAt(coach.getName(), modelRowIndex, 2);
    }

    private void deleteTraining() {
        int selectedRow = trainingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите занятие для удаления.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить занятие?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRowIndex = trainingTable.convertRowIndexToModel(selectedRow);
            trainings.remove(modelRowIndex);
            trainingTableModel.removeRow(modelRowIndex);
        }
    }

    private void addScheduleEntry() {
        String dayOfWeek = showInputDialog("Введите день недели (например, Понедельник):", "");
        if (dayOfWeek == null || dayOfWeek.isEmpty()) return;

        String time = showInputDialog("Введите время занятия (например, 18:00):", "");
        if (time == null || time.isEmpty()) return;

        String trainingName = showInputDialog("Введите название занятия:", "");
        if (trainingName == null || trainingName.isEmpty()) return;

        Training training = findTrainingByName(trainingName);
        if (training == null) {
            JOptionPane.showMessageDialog(this, "Занятие не найдено.");
            return;
        }

        ScheduleEntry entry = new ScheduleEntry(dayOfWeek, time, training);
        schedule.add(entry);
        scheduleTableModel.addRow(new Object[]{dayOfWeek, time, training.getName(), training.getCoach().getName()});
    }

    private void editScheduleEntry() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите запись в расписании для редактирования.");
            return;
        }

        int modelRowIndex = scheduleTable.convertRowIndexToModel(selectedRow);
        ScheduleEntry entry = schedule.get(modelRowIndex);

        String dayOfWeek = showInputDialog("Введите новый день недели:", entry.getDayOfWeek());
        if (dayOfWeek == null || dayOfWeek.isEmpty()) return;

        String time = showInputDialog("Введите новое время занятия:", entry.getTime());
        if (time == null || time.isEmpty()) return;

        String trainingName = showInputDialog("Введите новое название занятия:", entry.getTraining().getName());
        if (trainingName == null || trainingName.isEmpty()) return;

        Training training = findTrainingByName(trainingName);
        if (training == null) {
            JOptionPane.showMessageDialog(this, "Занятие не найдено.");
            return;
        }

        entry.setDayOfWeek(dayOfWeek);
        entry.setTime(time);
        entry.setTraining(training);

        scheduleTableModel.setValueAt(dayOfWeek, modelRowIndex, 0);
        scheduleTableModel.setValueAt(time, modelRowIndex, 1);
        scheduleTableModel.setValueAt(training.getName(), modelRowIndex, 2);
        scheduleTableModel.setValueAt(training.getCoach().getName(), modelRowIndex, 3);
    }

    private void deleteScheduleEntry() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите запись в расписании для удаления.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить запись из расписания?", "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRowIndex = scheduleTable.convertRowIndexToModel(selectedRow);
            schedule.remove(modelRowIndex);
            scheduleTableModel.removeRow(modelRowIndex);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sports_school.dat"))) {
            saveListToFile(oos, students);
            saveListToFile(oos, coaches);
            saveListToFile(oos, trainings);
            saveListToFile(oos, schedule);
            JOptionPane.showMessageDialog(this, "Данные сохранены в файл.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sports_school.dat"))) {
            students = loadListFromFile(ois);
            coaches = loadListFromFile(ois);
            trainings = loadListFromFile(ois);
            schedule = loadListFromFile(ois);

            refreshTable(studentTableModel, students, new String[]{"Имя", "Возраст", "Вид спорта"});
            refreshTable(coachTableModel, coaches, new String[]{"Имя", "Специализация"});
            refreshTable(trainingTableModel, trainings, new String[]{"Название", "Вид спорта", "Тренер"});
            refreshTable(scheduleTableModel, schedule, new String[]{"День недели", "Время", "Занятие", "Тренер"});

            JOptionPane.showMessageDialog(this, "Данные загружены из файла.");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private void saveListToFile(ObjectOutputStream oos, List<?> list) throws IOException {
        oos.writeObject(list);
    }

    private <T> List<T> loadListFromFile(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (List<T>) ois.readObject();
    }

    private <T> void refreshTable(DefaultTableModel tableModel, List<T> list, String[] columnNames) {
        tableModel.setRowCount(0);
        for (T item : list) {
            if (item instanceof Student) {
                Student student = (Student) item;
                tableModel.addRow(new Object[]{student.getName(), student.getAge(), student.getSport()});
            } else if (item instanceof Coach) {
                Coach coach = (Coach) item;
                tableModel.addRow(new Object[]{coach.getName(), coach.getSpecialization()});
            } else if (item instanceof Training) {
                Training training = (Training) item;
                tableModel.addRow(new Object[]{training.getName(), training.getSport(), training.getCoach().getName()});
            } else if (item instanceof ScheduleEntry) {
                ScheduleEntry entry = (ScheduleEntry) item;
                tableModel.addRow(new Object[]{entry.getDayOfWeek(), entry.getTime(), entry.getTraining().getName(), entry.getTraining().getCoach().getName()});
            }
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
    }

    private String showInputDialog(String message, String initialValue) {
        return JOptionPane.showInputDialog(this, message, initialValue);
    }

    private Coach findCoachByName(String name) {
        for (Coach coach : coaches) {
            if (coach.getName().equalsIgnoreCase(name)) {
                return coach;
            }
        }
        return null;
    }

    private Training findTrainingByName(String name) {
        for (Training training : trainings) {
            if (training.getName().equalsIgnoreCase(name)) {
                return training;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Авторизация");
            loginFrame.setSize(500, 260);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLayout(new BorderLayout());
            loginFrame.setLocationRelativeTo(null);

            JPanel loginPanel = new JPanel(new GridLayout(3, 2));
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JButton loginButton = new JButton("Войти");

            loginPanel.add(new JLabel("Логин:"));
            loginPanel.add(usernameField);
            loginPanel.add(new JLabel("Пароль:"));
            loginPanel.add(passwordField);
            loginPanel.add(new JLabel(""));
            loginPanel.add(loginButton);

            loginFrame.add(loginPanel, BorderLayout.CENTER);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    loginFrame.dispose();
                    new SportsSchoolApp().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Неверный логин или пароль.");
                }
            });

            loginFrame.setVisible(true);
        });
    }

    private static boolean authenticate(String username, String password) {
        return username.equals("1111") && password.equals("1111");
    }
}

class Student implements Serializable {
    private String name;
    private int age;
    private String sport;

    public Student(String name, int age, String sport) {
        this.name = name;
        this.age = age;
        this.sport = sport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}

class Coach implements Serializable {
    private String name;
    private String specialization;

    public Coach(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

class Training implements Serializable {
    private String name;
    private String sport;
    private Coach coach;

    public Training(String name, String sport, Coach coach) {
        this.name = name;
        this.sport = sport;
        this.coach = coach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}

class ScheduleEntry implements Serializable {
    private String dayOfWeek;
    private String time;
    private Training training;

    public ScheduleEntry(String dayOfWeek, String time, Training training) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.training = training;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}
