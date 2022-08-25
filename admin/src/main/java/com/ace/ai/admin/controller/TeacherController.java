package com.ace.ai.admin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ace.ai.admin.datamodel.Teacher;
import com.ace.ai.admin.dtomodel.TeacherDTO;
import com.ace.ai.admin.repository.TeacherRepository;
import com.ace.ai.admin.service.TeacherService;


@Controller
@RequestMapping("/admin")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

  @Autowired
  private TeacherRepository teacherRepository;

  @GetMapping("/addTeacher")
   public ModelAndView setupAddTeacher(ModelMap model) {
    return new ModelAndView("A004-01","teacherDto",new TeacherDTO());
  }
  @GetMapping("/addTeacherSuccess")
   public ModelAndView setupAddTeacherSuccess(ModelMap model) {
    model.addAttribute("msg","Register Successfully !!!");
    return new ModelAndView("A004-01","teacherDto",new TeacherDTO());
  }

  @GetMapping("/addTeacherFail")
   public ModelAndView setupAddTeacherFail(ModelMap model) {
    model.addAttribute("error","Teacher Code Exists in Database");
    return new ModelAndView("A004-01","teacherDto",new TeacherDTO());
  }
  @PostMapping("/addTeacherPost")
       public String addTeacher(@ModelAttribute("teacherDto") TeacherDTO teacherDto, ModelMap model) throws IllegalStateException, IOException {
       Teacher bean=new Teacher();
       bean.setCode(teacherDto.getCode());
       bean.setName(teacherDto.getName());
       bean.setPassword(teacherDto.getPassword());
       String fileName=StringUtils.cleanPath(teacherDto.getPhoto().getOriginalFilename());
       bean.setPhoto(fileName);
       boolean code=teacherService.existsByCode(bean.getCode());
        if(code == true){
          model.addAttribute("error","Teacher Code Exists in Database");
          return "redirect:/admin/addTeacherFail";
        }
         else{
          Teacher savedTeacher=teacherRepository.save(bean);

          String uploadDir="./assets/img/"+ savedTeacher.getCode();

          Path uploadPath = Paths.get(uploadDir);

          if(!Files.exists(uploadPath)){
          Files.createDirectories(uploadPath);
       }

      try( InputStream inputStream=teacherDto.getPhoto().getInputStream()){
       Path filePath=uploadPath.resolve(fileName);
       System.out.println(filePath.toFile().getAbsolutePath());
       Files.copy(inputStream, filePath ,StandardCopyOption.REPLACE_EXISTING);
      }catch (IOException e){
          throw new IOException("Could not save upload file: " + fileName);
      }
      model.addAttribute("msg","Register Successfully !!!");
       return "redirect:/admin/addTeacherSuccess";
    }
}

  @GetMapping("/teacherList")
    public String showTeacher(ModelMap model){
      List<Teacher> teacherList=teacherRepository.findByDeleteStatus(false);
      // int count = teacherList.size();
      System.out.println(teacherList);
      model.addAttribute("teacherList",teacherList);
      return "A004";
    }

  @GetMapping("/updateTeacher")
   public ModelAndView setupUpdateTeacher(ModelMap model,@RequestParam("id")Integer id,  HttpServletRequest request) {
      Teacher bean = teacherService.getId(id);
      TeacherDTO teacherDto=new TeacherDTO();
      teacherDto.setId(bean.getId());
      teacherDto.setCode(bean.getCode());
      teacherDto.setName(bean.getName());
      teacherDto.setPassword(bean.getPassword());
      request.setAttribute("photo", bean.getImagePath());
      return new ModelAndView("A004-02","teacherDto",teacherDto);
   }
   @GetMapping("/updateTeacherSuccess")
   public ModelAndView updateTeacherSuccess(ModelMap model) {
    model.addAttribute("msg","Update Successfully !!!");
    return new ModelAndView("A004-01","teacherDto",new TeacherDTO());
  }

   @PostMapping("/updateTeacherPost")
    public String updateTeacher(@ModelAttribute("teacherDto") TeacherDTO teacherDto,ModelMap model) throws IOException{

      Teacher bean=new Teacher();
      bean.setId(teacherDto.getId());
      bean.setCode(teacherDto.getCode());
      bean.setName(teacherDto.getName());
      bean.setPassword(teacherDto.getPassword());
      
        Teacher teacher = teacherService.getId(bean.getId());

        if(teacherDto.getPhoto().getOriginalFilename().isBlank()){
            bean.setPhoto(teacher.getPhoto());
            teacherRepository.save(bean);
            model.addAttribute("msg","Update Successfully !!!");
            return "redirect:/admin/updateTeacherSuccess";
        }
        else{

            Path path = Paths.get("./assets/img/"+teacher.getCode()+"/"+teacher.getPhoto());
            Files.delete(path);
            String fileName=StringUtils.cleanPath(teacherDto.getPhoto().getOriginalFilename());
            bean.setPhoto(fileName);
          
            Teacher savedTeacher=teacherRepository.save(bean);
            String uploadDir="./assets/img/"+ savedTeacher.getCode();
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
            try {
              Files.createDirectories(uploadPath);
            } catch (IOException e) {
              
              e.printStackTrace();
            }
            }
          try( InputStream inputStream=teacherDto.getPhoto().getInputStream()){
            Path filePath=uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath ,StandardCopyOption.REPLACE_EXISTING);
          }catch (IOException e){
              try {
                throw new IOException("Could not save upload file: " + fileName);
              } catch (IOException e1) {
                
                e1.printStackTrace();
              }
          } 
          
          model.addAttribute("msg","Update Successfully !!!");
          return "redirect:/admin/updateTeacherSuccess";
        }
        
      }

      @GetMapping("/deleteTeacher/{id}")
      public String deleteTeacher(@PathVariable("id")Integer id) {
        Teacher teacher = teacherService.getId(id);
        teacher.setDeleteStatus(true);
        teacherService.saveTeacher(teacher);
        return "redirect:/admin/teacherList";
      }
      
    }
   

