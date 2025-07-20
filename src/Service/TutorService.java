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
    
    // To check if the choosen subject got schedule or not
    // param(subject): The choosen Subect
    // return(bool)  : True, if subject has no schedule
    public static boolean subjectHasNoSchedule (Subject subject) {
        List<ClassSchedule> allSchedule = DataManager.of(ClassSchedule.class).readFromFile();
        Set<String> allScheduleSubId = allSchedule.stream()
            .map(sch -> sch.getSubjectId())
            .collect(Collectors.toSet());
        
        return (!allScheduleSubId.contains(subject.getId()));
    }

    // To check if the new schedule time will conflict exist schedule time or not.
    // param(subject)        : The subject that u want to add the schedule
    // param(newScheduleDay) : The day of the new schedule will add
    // param(newScheduleTime): The time of the new schedule will add
    // return(bool)          : if no time conflict founded return true
    public static boolean noTimeConflictInSameLevel(Subject subject, String newScheduleDay, String newScheduleTime) {
        
        // Get all Schedule
        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
        
        // Get all the subject which having the schedule
        List<Subject> allSubjectWithSchedule = getAllSubjectWithSchedule();
        
        // The level of current subject
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

       // create a list to store the exist time of the same level subject for one day only.
        List<String> sameLvSubTimeInOneDay = getExistTimeOnSameDay(newScheduleDay, sameLevelSchedules);
        
        // check for the conflict
        int[] newTime = parseTimeRange(newScheduleTime);
        for (String time : sameLvSubTimeInOneDay) {
            int [] existTime = parseTimeRange(time);
            if (newTime[0] < existTime[1] && newTime[1] > existTime[0]) {
                    return false;
            }
        }
        return true;
    }
    
    // To check if the new schedule time will conflict with own schedule, not matter what level
    // param(tutorId)        : The id of tutor
    // param(newScheduleDay) : The day of the new schedule will add
    // param(newScheduleTime): The time of the new schedule will add
    // return(bool)          : if no time conflict founded return true
    public static boolean noTimeConflictWithOwnSchedule(String tutorId, String newScheduleDay, String newScheduleTime) {
        List<Subject> mySubjects = getMySubject(tutorId);

        // Get all the schedule by tutor himself no matter level
        Set<String> mySubjectIds = mySubjects.stream()
            .map(sub -> sub.getId())
            .collect(Collectors.toSet());

        List<ClassSchedule> allSchedules = DataManager.of(ClassSchedule.class).readFromFile();
        List<ClassSchedule> mySchedules = allSchedules.stream()
            .filter(sch -> mySubjectIds.contains(sch.getSubjectId()))
            .toList();

        // Create a List to store the same day's time of his own schedule
        List<String> existTimeOnSameDay = getExistTimeOnSameDay(newScheduleDay, mySchedules);

        // check if the new schedule will conflict to his own schedule
        int[] newTime = parseTimeRange(newScheduleTime);
        for (String time : existTimeOnSameDay) {
            int [] existTime = parseTimeRange(time);
            if (newTime[0] < existTime[1] && newTime[1] > existTime[0]) {
                    return false;
            }
        }
        return true;
    }
    
    // To get the same level day schedule map
    // param(subject) : The subject that will gonna add the schedule
    // return(Map)    : A map will store day as key and subject with corresponding schedule as value
    public static Map<String, Map<Subject, List<String>>> getSameLevelDayScheduleMap(Subject subject) {
        // create a map to put the day as key and empty map as value
        Map<String, Map<Subject, List<String>>> dayToSubjectsMap = new HashMap<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        for (String day : days) {
            dayToSubjectsMap.put(day, new LinkedHashMap<>());
        }

        String targetLevel = subject.getLevel();
        String thisSubjectId = subject.getId();
        
        // All Schedules
        DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
        List<ClassSchedule> allSchedules = scheduleManager.readFromFile();
        
        // All Subjects
        DataManager<Subject> subjectManager = DataManager.of(Subject.class);
        List<Subject> allSubjects = subjectManager.readFromFile();
        
        // A map which store same level Subject id as key and subject as value
        Map<String, Subject> sameLevelSubIdAndSubMap = allSubjects.stream()
            .filter(sub -> sub.getLevel().equals(targetLevel) && !sub.getId().equals(thisSubjectId))
            .collect(Collectors.toMap(Subject::getId, sub -> sub));
        
        // put the same level schedule's time into the list
        for (ClassSchedule schedule : allSchedules) {
            Subject sub = sameLevelSubIdAndSubMap.get(schedule.getSubjectId());
            if (sub == null) continue; // To get the subject's corresponding schedule

            String[] dayTimePairs = schedule.getScheduleInWeek().split("\\|");
            for (String pair : dayTimePairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    String day = parts[0];
                    String time = parts[1];
                    if (dayToSubjectsMap.containsKey(day)) {
                        dayToSubjectsMap.get(day)
                            .computeIfAbsent(sub, k -> new ArrayList<>())
                            .add(time);
                    }
                }
            }
        }

        return dayToSubjectsMap;
    }

    // This will store all the schedule info of the tutor in a Map.
    // param(tutorId): The id of the Tutor
    // return(Map)   : Return a map of schedule info (e.g. {"Monday": {Math: "1020-1120", Sceince: "0900-1000", ...}, "Tuesday": ...)
    public static Map<String, Map<Subject, List<String>>> getMyWeekScheduleMap(String tutorId) {
        // Initialize a Map which contains the day in each key and an empty map in value.
        Map<String, Map<Subject, List<String>>> myWeekScheduleMap = new HashMap<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        for (String day : days) {
            myWeekScheduleMap.put(day, new HashMap<>());
        }

        // Get a list of subject, then convert each subject to a set of subjectId.
        List<Subject> mySubject = getMySubject(tutorId);
        Set<String> mySubjectId = mySubject.stream()
            .map(Subject::getId)
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
                String[] parts = dayAndTime.split(":", 2);
                if (parts.length < 2) continue;

                String day = parts[0];
                String time = parts[1];

                if (myWeekScheduleMap.containsKey(day)) {
                    Map<Subject, List<String>> subjectMap = myWeekScheduleMap.get(day);
                    subjectMap.computeIfAbsent(thisSubject, k -> new ArrayList<>()).add(time);
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
        
        if (allSchedule.isEmpty()) {
            return Collections.emptyList();
        }
        
        Set<String> allSubjectIdWithSchedule = allSchedule.stream()
            .map(ClassSchedule::getSubjectId)
            .collect(Collectors.toSet());
        
        return allSubject.stream()
                    .filter(sub -> allSubjectIdWithSchedule.contains(sub.getId()))
                    .toList(); 
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
        try {
            String[] parts = timeStr.split("-");
            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeStr);
        }
    }
    
    // Get a list of time which is on the same day, used for check conflict time
    // param(newSchDay)    : The day of the new shcedule tutor want to register
    // param(scheckSchList): The List of classSchedule u want to check
    // return(List)        : Return a list of String time
    private static List<String> getExistTimeOnSameDay (String newSchDay, List<ClassSchedule> checkSchList) {
        List<String> existTimeOnSameDay = new ArrayList<>();
        
        for (ClassSchedule sch : checkSchList) {
            for (String dayTime : sch.getScheduleInWeek().split("\\|")) {
                String [] dayTimePairs = dayTime.split(":");
                String day = dayTimePairs[0];
                String time = dayTimePairs[1];
                
                if (day.equals(newSchDay)) {
                    existTimeOnSameDay.add(time);
                }
            }
        }
        
        return existTimeOnSameDay;
    }   
        

}
