package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.StudentRepository;
import sit.tuvarna.bg.first_app.tables.Student;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;

    public void addStudent(Student student){
        studentRepository.save(student);
    }

    public void addStudents(List<Student> students){
        studentRepository.saveAll(students);
    }

    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }

    public void updateStudent(Long id,Student student){
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        if (student.getFn() != null)
            existingStudent.setFn(student.getFn());

        if (student.getDepartment() != null)
            existingStudent.setDepartment(student.getDepartment());

        if (student.getCollage() != null)
            existingStudent.setCollage(student.getCollage());

        if (student.getRoom() != null)
            existingStudent.setRoom(student.getRoom());

        studentRepository.save(existingStudent);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
