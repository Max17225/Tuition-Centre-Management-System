/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

/**
 *
 * @author nengz
 */


import java.util.*;
import DataModel.*;
import Util.*;
import java.util.stream.Collectors;

public class TutorService {
    
    // Update the choosen subject fee
    // param(subjectId): The id of the choosen subject
    // param(newFee)   : New fee of the subject
    // return(bool)    : True if change success
    public static boolean updateSubjectFee(String subjectId, String newFee) {
        
        DataManager<Subject> subjectManager = DataManager.of(DataModel.Subject.class);
        List<Subject> allRecord = subjectManager.readFromFile();
        
        for (Subject record : allRecord) {
            if (record.getId().equals(subjectId)) {
                record.setFee(newFee);
                subjectManager.overwriteFile(allRecord);
                return true;
            }
        } 
        
        return false;
    }
    
    // To get tutor's subject
    // param(tutorId): The id of the chosen tutor
    // return(List)  : A list of subject 
    public static List<Subject> getMySubject(String tutorId) {
        Util.DataManager<Subject> subjectManager = DataManager.of(Subject.class);
        List<Subject> allSubjects = subjectManager.readFromFile();
        
        // Filter out tutor's own subject and save it into a List
        List<Subject> userSubjects = allSubjects.stream()
            .filter(subjectRecord -> subjectRecord.getTutorId().equals(tutorId))
            .toList();

        return userSubjects;
    }
    
    // To get the subject which without having schedule
    // param(tutorId): The id of the choosen Tutor
    // return(List)  : A list of subject
    public static List<Subject> getMySubjectWithoutSchedule(String tutorId) {
        
        List<Subject> mySubjects = getMySubject(tutorId);

        List<ClassSchedule> mySchedules = getMyClassSchedule(tutorId);

        Set<String> scheduledSubjectIds = mySchedules.stream()
            .map(ClassSchedule::getSubjectId)
            .collect(Collectors.toSet());

        // if the subject id is not inside the scheduledSubjectIds, it will store into a list
        List<Subject> unscheduledSubjects = mySubjects.stream()
            .filter(subject -> !scheduledSubjectIds.contains(subject.getId()))
            .toList();

        return unscheduledSubjects; // return the list
    }
    
    // To get the subject which already having schedule
    // param(tutorId): The id of the choosen Tutor
    // return (List) : A list of subject
    public static List<Subject> getMySubjectWithSchedule(String tutorId) {
        
        Set<String> mySubjectIdWithSchedule = getMyClassSchedule(tutorId).stream()
            .map(ClassSchedule::getSubjectId)
            .collect(Collectors.toSet());

        return getMySubject(tutorId).stream()
            .filter(sub -> mySubjectIdWithSchedule.contains(sub.getId()))
            .toList();
    }
    
    // To get the schedule of his own subject
    // param(tutorId): The id of the choosen Tutor
    // return(List)  : A list of ClassSchedule
    public static List<ClassSchedule> getMyClassSchedule(String tutorId) {
        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
        
        // Get his own subject id in a set
        Set<String> subjectIds = getMySubject(tutorId).stream()
            .map(Subject::getId)
            .collect(Collectors.toSet());

        // Filter out the schedule if the shcedule's subject id was in his subject set.     
        List<ClassSchedule> mySchedule = allSchedule.stream()
            .filter(sch -> subjectIds.contains(sch.getSubjectId()))
            .toList();
        
        return mySchedule;
    }
    
    // A new schedule creatin interface, after user finish the input, return a map of info
    // return(Map) : A map which contain schedule info (e.g. {"Monday": "1200-1300", "Tuesday": "1400-1530", ...})
    public static Map<String, String> newScheduleCreation() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> scheduleMap = new LinkedHashMap<>();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        
        for (String day : days) { // start the day in week one by one
            while (true) {
                System.out.print(day + ": ");
                String input = scanner.nextLine().trim(); // let user input the time

                if (input.isEmpty()) {
                    break; // skip this day
                }

                if (!InputValidator.isValidTime(input)) { // if their input time is not valid, print warning
                    System.out.println("Invalid Time Format: " + input + " (Use format HHMM-HHMM like 0900-1100)");
                    continue; // ask again for this day
                }

                scheduleMap.put(day, input); // if valid then put it into map
                break;
            }
        }
        
        return scheduleMap;
    }

    // To check if the new schedule time will conflict exist schedule time or not.
    // param(subject)       : The subject that u want to add the schedule
    // param(newScheduleMap): The map of the new schedule create by the newScheduleCreation method
    // return(bool)         : if no time conflict founded return true
    public static boolean noTimeConflictInSameLevel(Subject subject, Map<String, String> newScheduleMap) {
        boolean conflictFound = false;

        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
        List<Subject> allSubjectWithSchedule = getAllSubjectWithSchedule();
        
        String thisSubjectLevel = subject.getLevel();

        // get the subject id of all the schedule that in the same level 
        Set<String> sameLevelSubjectIds = allSubjectWithSchedule.stream()
            .filter(sub -> sub.getLevel().equals(thisSubjectLevel))
            .map(Subject::getId)
            .collect(Collectors.toSet());

       // get the schedule record which is in the same level
        List<ClassSchedule> sameLevelSchedules = allSchedule.stream()
            .filter(sch -> sameLevelSubjectIds.contains(sch.getSubjectId()))
            .toList();

       // create a map for storing the exist schedule info (e.g.{"Monday":["1200-1300", "1500-1600:, ...], ...})
        Map<String, List<String>> existScheduleMap = new HashMap<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        for (String day : days) {
            existScheduleMap.put(day, new ArrayList<>());
        }
        
        // put the time info to the map
        for (ClassSchedule schedule : sameLevelSchedules) {
            String[] dayTimePairs = schedule.getScheduleInWeek().split("\\|");
            for (String pair : dayTimePairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    String day = parts[0];
                    String time = parts[1];
                    if (existScheduleMap.containsKey(day)) {
                        existScheduleMap.get(day).add(time);
                    }
                }
            }
        }

        
        // check for the conflict
        for (Map.Entry<String, String> entry : newScheduleMap.entrySet()) {
            String day = entry.getKey();
            String newTimeStr = entry.getValue();
            int[] newTime = parseTimeRange(newTimeStr);

            List<String> existTimes = existScheduleMap.getOrDefault(day, new ArrayList<>());
            for (String existTimeStr : existTimes) {
                int[] existTime = parseTimeRange(existTimeStr);
                
                // if conflict founded print out the conflict
                if (newTime[0] < existTime[1] && newTime[1] > existTime[0]) {
                    System.out.printf("Conflict on %s: %s overlaps with existing %s%n",
                            day, timeRangeToStr(newTime), timeRangeToStr(existTime));
                    conflictFound = true;
                }
            }
        }

        return !conflictFound; 
    }
    
    // To check if the new schedule time will conflict with own schedule, not matter what level
    // param(tutorId)       : The id of tutor
    // param(newScheduleMap): The map of the new schedule create by the newScheduleCreation method
    // return(bool)         : if no time conflict founded return true
    public static boolean noTimeConflictWithOwnSchedule(String tutorId, Map<String, String> newScheduleMap) {
        List<Subject> mySubjects = getMySubject(tutorId);

        // Get all the schedule by tutor himself no matter level
        Set<String> mySubjectIds = mySubjects.stream()
            .map(sub -> sub.getId())
            .collect(Collectors.toSet());

        List<ClassSchedule> allSchedules = DataManager.of(ClassSchedule.class).readFromFile();
        List<ClassSchedule> mySchedules = allSchedules.stream()
            .filter(sch -> mySubjectIds.contains(sch.getSubjectId()))
            .toList();

        // Create a map to store his own schedule, day will be key, time will be value
        Map<String, List<String>> existScheduleMap = new HashMap<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        for (String day : days) {
            existScheduleMap.put(day, new ArrayList<>());
        }
        for (ClassSchedule schedule : mySchedules) {
            String[] dayTimePairs = schedule.getScheduleInWeek().split("\\|");
            for (String pair : dayTimePairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    String day = parts[0];
                    String time = parts[1];
                    existScheduleMap.get(day).add(time);
                }
            }
        }

        // check if the new schedule will conflict to his own schedule
        for (Map.Entry<String, String> entry : newScheduleMap.entrySet()) {
            String day = entry.getKey();
            String newTimeStr = entry.getValue();
            int[] newTime = parseTimeRange(newTimeStr);

            for (String existTimeStr : existScheduleMap.get(day)) {
                int[] existTime = parseTimeRange(existTimeStr);
                if (newTime[0] < existTime[1] && newTime[1] > existTime[0]) {
                    System.out.printf("Tutor Time Conflict on %s: %s overlaps with existing %s%n", day, timeRangeToStr(newTime), timeRangeToStr(existTime));
                    return false;
                }
            }
        }

        return true;
    }
    
    // Format the scheduleMap to a string for saving it to the file 
    // param(scheduleMap): The map of the schedule info
    // return(String)    : A formated string line
    public static String formatScheduleMapToString(Map<String, String> scheduleMap) {
    return scheduleMap.entrySet().stream()
        .map(entry -> entry.getKey() + ":" + entry.getValue())
        .collect(Collectors.joining("|"));
    }
    
    // This will store all the schedule info of the tutor in a Map.
    // param(tutorId): The id of the Tutor
    // return(Map)   : Return a map of schedule info (e.g. {"Monday": {Math: "1020-1120", Sceince: "0900-1000", ...}, "Tuesday": ...)
    public static Map<String, Map<Subject, String>> getMyWeekScheduleMap (String tutorId) {
        
        // Initialize a Map which contain the day in each key and empty map in value.
        Map<String, Map<Subject, String>> myWeekScheduleMap = new HashMap<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };   
        for (String day : days) {
            myWeekScheduleMap.put(day, new HashMap<>());
        }
        
        // Get a list of subject, then convert each subject to a set of subjectId.
        List<Subject> mySubject = getMySubject(tutorId);
        Set<String> mySubjectId = mySubject.stream()
            .map(sub -> sub.getId())
            .collect(Collectors.toSet());
        
        // Get the schedule record of the tutor
        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
        List<ClassSchedule> mySchedule = allSchedule.stream()
            .filter(sch -> mySubjectId.contains(sch.getSubjectId()))
            .toList();
        
        // Loop the tutor's schedule one by one and put the info into the map.
        for (ClassSchedule schedule : mySchedule) {
            Subject thisSubject = getMySubjectById(schedule.getSubjectId(), tutorId);
            if (thisSubject == null) continue;
            
            String[] allDayAndTime = schedule.getScheduleInWeek().split("\\|");
            for (String dayAndTime : allDayAndTime) {
                String[] parts =  dayAndTime.split(":", 2);
                if (parts.length < 2) continue;
                String day = parts[0];
                String time = parts[1];
                
                if (myWeekScheduleMap.containsKey(day)) {
                    myWeekScheduleMap.get(day).put(thisSubject, time);
                }
            }
        }
        
        return myWeekScheduleMap;       
    }
    
    // This will return a Map with Subject(Object) as key and a List of student(Object) as value.
    // param(tutorId): The id of tutor
    // return(Map)   : A map will look like {Subject: [Student, Student2], Subject2:[...], ...}
    public static Map<Subject, List<Student>> getMySubjectStudentMap (String tutorId) {
        
        // Get all enrollment
        DataManager<Enrollment> enrollmentManager = DataManager.of(Enrollment.class);
        List<Enrollment> allEnrollment = enrollmentManager.readFromFile();

        
        // Get a Map which save student enrollmentId as key and student(Object) as value
        DataManager<Student> studentManager = DataManager.of(Student.class);
        List<Student> allStudent = studentManager.readFromFile();
        Map<String, Student> enrollmentIdToStudent = allStudent.stream()
            .collect(Collectors.toMap(stu -> stu.getEnrollmentId(), stu -> stu));
        
        // Get a Map which save tutor's subject(object) as key and a list of student(object) as value
        List<Subject> allMySubject = getMySubject(tutorId);
        Map<Subject, List<String>> allMySubjectWithEnrollmentId = new HashMap<>();
        for (Subject subject : allMySubject) {
            allMySubjectWithEnrollmentId.put(subject, new ArrayList<>());
            for (Enrollment enrollment : allEnrollment) {
                String[] studentSubjectIds = {enrollment.getSubjectId1(), enrollment.getSubjectId2(), enrollment.getSubjectId3()};
                for (String subjectId : studentSubjectIds) {
                    if (subjectId.equals(subject.getId())) {
                        allMySubjectWithEnrollmentId.get(subject).add(enrollment.getId());
                        break;
                    }
                }
            }
        }
        
        // Get a Map which save Subject(object) as key and a list of student(object) as value
        Map<Subject, List<Student>> subjectStudentsMap = new HashMap<>();
        for (Map.Entry<Subject, List<String>> entry : allMySubjectWithEnrollmentId.entrySet()) {
            Subject subject = entry.getKey();
            List<String> enrollmentIds = entry.getValue();
            // Convert enrollmentId to Student (if found)
            List<Student> studentsInThisSubject = enrollmentIds.stream()
                .map(enrollmentId -> enrollmentIdToStudent.get(enrollmentId))
                .toList();

            subjectStudentsMap.put(subject, studentsInThisSubject);
        }
        
        return subjectStudentsMap;
    }

    
    // -----------------------------------------Private Method-----------------------------------
    
    // get all the subject which already having the schedule
    // return(List): A list of subject
    private static List<Subject> getAllSubjectWithSchedule () {
        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
        
        DataManager<Subject> subjectManager = DataManager.of(Subject.class);
        List<Subject> allSubject = subjectManager.readFromFile();
        
        List<Subject> allSubjectWithSchedule = new ArrayList<>();
        
        
        if (allSchedule.isEmpty()) {
            return null;
        }
        
        Set<String> allSubjectIdWithSchedule = allSchedule.stream()
            .map(ClassSchedule::getSubjectId)
            .collect(Collectors.toSet());
        
        for (Subject record : allSubject) {
            if (allSubjectIdWithSchedule.contains(record.getId())) {
                allSubjectWithSchedule.add(record);
            }
        }
        
        return allSubjectWithSchedule;   
    }
    
    // Convert subjectId to a subject(Object)
    // param(subjectId): The string id of subject
    // param(tutorId)  : The string id of tutor
    // return(subject) : Return the subject Object
    private static Subject getMySubjectById(String subjectId, String tutorId) {
        return getMySubject(tutorId).stream()
            .filter(subject -> subject.getId().equals(subjectId))
            .findFirst()
            .orElse(null); 
    }
    
    // To convert "1100-1200" -> int[]{1100, 1200}
    private static int[] parseTimeRange(String timeStr) {
        String[] parts = timeStr.split("-");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    // To convert int[]{1100, 1200} -> "1100-1200"
    private static String timeRangeToStr(int[] range) {
        return String.format("%04d-%04d", range[0], range[1]);
    }
    
}
