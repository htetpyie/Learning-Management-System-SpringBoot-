package com.ace.ai.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ace.ai.admin.datamodel.Chapter;
import com.ace.ai.admin.datamodel.ChapterFile;
import com.ace.ai.admin.datamodel.Course;
import com.ace.ai.admin.dtomodel.AdminChapterDTO;
import com.ace.ai.admin.dtomodel.AdminCourseDTO;
import com.ace.ai.admin.dtomodel.ChapterFileDTO;
import com.ace.ai.admin.repository.ChapterFileRepository;
import com.ace.ai.admin.repository.ChapterRepository;
import com.ace.ai.admin.repository.CourseRepository;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ChapterRepository chapterRepository;
    @Autowired
    ChapterFileRepository chapterFileRepository;


    public List<AdminCourseDTO> getCourseList(){
        List<Course> courseList = courseRepository.findAll();
        List<AdminCourseDTO> adminCourseDTOList = new ArrayList<>();
        for(Course course : courseList){
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            AdminCourseDTO adminCourseDTO = new AdminCourseDTO();
            adminCourseDTO.setId(course.getId());
            adminCourseDTO.setName(course.getName());
            adminCourseDTO.setCreatedDate(LocalDate.parse(course.getCreatedDate(),df));
            adminCourseDTO.setDeleteStatus(course.isDeleteStatus());
            adminCourseDTOList.add(adminCourseDTO);
        }
        return adminCourseDTOList;
    }

    public List<AdminChapterDTO> getChapterList(int courseId){
        List<Chapter> chapterList = chapterRepository.findByCourseId(courseId);
        List<AdminChapterDTO> adminChapterDTOList = new ArrayList<>();
        for(Chapter chapter : chapterList){
           
            AdminChapterDTO adminChapterDTO = new AdminChapterDTO();
            adminChapterDTO.setTotalFile(chapterFileRepository.findByChapterIdAndDeleteStatus(chapter.getId(),true).size());
            // adminChapterDTO.setcourseId(courseId);
            adminChapterDTO.setId(chapter.getId());
            adminChapterDTO.setName(chapter.getName());
            adminChapterDTO.setDeleteStatus(chapter.getDeleteStatus());
            adminChapterDTOList.add(adminChapterDTO);
        }
        return adminChapterDTOList;
    }

    public void saveAllFilesList(ChapterFile file) {
        // Save all the files into database
    
        
           chapterFileRepository.save(file);

        
    }
    public Chapter saveChapter(Chapter chapter){
        
        return chapterRepository.save(chapter);
    }

    public void editChapter(int chapterId,String chapterName){
        Chapter chapter = new Chapter();
        chapter.setId(chapterId);
        chapter.setName(chapterName);
        chapterRepository.save(chapter);
    }

    public List<Course> getAllCourse(){
        return courseRepository.findAll();
        
    }

    public List<AdminChapterDTO> getCourseDetail(int courseId){
       List<AdminChapterDTO> chapterListDTO = new ArrayList<>();
       List<Chapter> chapterList = chapterRepository.findByCourseId(courseId);
       for(Chapter chapter : chapterList){
        AdminChapterDTO chapterDTO = new AdminChapterDTO();
            chapterDTO.setId(chapter.getId());
            chapterDTO.setName(chapter.getName());
            chapterListDTO.add(chapterDTO);
       }

        return chapterListDTO;
    }

    public List<ChapterFileDTO> getChpaterFile(int chapterId){
        List<ChapterFileDTO> chapterFileListDTO  = new ArrayList<>();
        List<ChapterFile> chapterFileList = chapterFileRepository.findByChapterIdAndDeleteStatus(chapterId,false);
        for(ChapterFile chapterFile : chapterFileList ){
            ChapterFileDTO chapterFileDTO = new ChapterFileDTO();
            chapterFileDTO.setId(chapterFile.getId());
            chapterFileDTO.setName(chapterFile.getName());
            chapterFileDTO.setFileType(chapterFile.getFileType());
            chapterFileDTO.setDeleteStatus(chapterFile.getDeleteStatus());
            chapterFileListDTO.add(chapterFileDTO);
        }

        return chapterFileListDTO;
    }

    public void saveCourse(String courseName){
        Course course = new Course();
        course.setName(courseName);
        course.setCreatedDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        courseRepository.save(course);
    }

    public void editCourse(String courseName,int courseId){
        Course course = new Course();
        course.setId(courseId);
        course.setName(courseName);
        courseRepository.save(course);
    }

    public int getChapterId(String chapterName){
        Chapter chapter = chapterRepository.findByName(chapterName);
        return chapter.getId();
    }

    public int getChapterFileCount(int chapterId){
        return chapterFileRepository.findByChapterIdAndDeleteStatus(chapterId,true).size();
    }

    public Course getById(int id){
        return courseRepository.getById(id);
    }

}
